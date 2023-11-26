import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
	public static void main(String[] args)
			throws InvalidPostDataException, InvalidDateTimeFormatException, IOException, PostNotFoundException {
		
		//retrieve the posts file
		SocialMediaAnalyzer analyzer = new SocialMediaAnalyzer("posts.csv");
		//initialize scanner
		Scanner scanner = new Scanner(System.in);

		System.out.println("Welcome to Social Media Analyzer!");

		//loop to prompt for options:
		while (true) {
			System.out.println();
			System.out.println("1) Add a social media post");
			System.out.println("2) Delete an existing social media post");
			System.out.println("3) Retrieve a social media post");
			System.out.println("4) Retrieve all replies of a particular social media post");
			System.out.println("5) Retrieve the top N posts and replies with most likes");
			System.out.println("6) Retrieve the top N posts and replies with most shares");
			System.out.println("7) Exit");

			System.out.print("Please select: ");

			int choice = -1; // Initialize with an invalid choice

			//check to see if a valid choice was made
			try {
				choice = scanner.nextInt();

				if (choice < 1 || choice > 7) {
					throw new IllegalArgumentException("Invalid choice. Please select a number between 1 and 7.");
				}

				switch (choice) {
				case 1:
					try {
						addSocialMediaPost(analyzer, scanner);
					} catch (InvalidPostDataException e) {
						System.err.println("Error: " + e.getMessage()); // Print error message
					}
					break;
				case 2:
					deleteSocialMediaPost(analyzer, scanner);
					break;
				case 3:
					retrieveSocialMediaPost(analyzer, scanner);
					break;
				case 4:
					retrieveAllReplies(analyzer, scanner);
					break;
				case 5:
					retrieveTopPostsByLikes(analyzer, scanner);
					break;
				case 6:
					retrieveTopPostsByShares(analyzer, scanner);
					break;
				case 7:
					System.out.println("Thanks for using Social Media Analyzer!");
					scanner.close();
					System.exit(0);
				default:
					System.out.println("Invalid choice. Please try again.");
				}
			} catch (InputMismatchException e) {
				System.err.println("Error: Invalid input. Please enter a valid number.");
				scanner.nextLine(); // Consume the invalid input
			} catch (IllegalArgumentException e) {
				System.err.println("Error: " + e.getMessage()); // Print a user-friendly error message
			}
		}
	}

	private static void addSocialMediaPost(SocialMediaAnalyzer analyzer, Scanner scanner)
			throws InvalidPostDataException, InvalidDateTimeFormatException {

		//id
		System.out.print("Please provide the post ID: ");
		int id = readNonNegativeInt(scanner);

		// Check if the ID is already in use
		if (analyzer.postIdExists(id)) {
			System.err.println("Error: Post ID already exists. Please provide a unique ID.");
			return; 
		}

		// Consume the newline character
		scanner.nextLine();

		//content
		System.out.print("Please provide the post content: ");
		String content = scanner.nextLine();

		//author
		System.out.print("Please provide the post author: ");
		String author = scanner.nextLine();

		//likes. cannot be < 0
		System.out.print("Please provide the number of likes of the post: ");
		int likes = readNonNegativeInt(scanner);

		//shares. cannot be < 0
		System.out.print("Please provide the number of shares of the post: ");
		int shares = readNonNegativeInt(scanner);

		// Consume the newline character
		scanner.nextLine();
		
		//parse dateTime
		String dateTime = null;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		//reprompt until valid dateTime
		while (dateTime == null) {
			System.out.print("Please provide the date and time of the post in the format of DD/MM/YYYY HH:MM: ");
			String userInput = scanner.nextLine();

			try {
				// Attempt to parse the user input as a valid date and time
				Date parsedDate = dateFormat.parse(userInput);
				dateTime = dateFormat.format(parsedDate); // Format the date and time
			} catch (java.text.ParseException e) {
				// Print the error message and continue the loop
				System.out.println("Invalid date-time format. Please use 'DD/MM/YYYY HH:MM'.");
			}
		}

		// get the main post id
		int mainPostId;

		System.out.print("If it’s a reply, please provide the main post ID or 0 if this is a main post: ");

		//make sure its either 0 for initial, or ties to an existing comment
		while (true) {
			mainPostId = readNonNegativeInt(scanner);

			// Check if the referred ID isnt being used
			if (!analyzer.postIdExists(mainPostId) && mainPostId != 0) { // Not a main post
				System.err.println("Error: Post ID not found. Please refer an existing post, or 0 for initial post.");
			} else {
				// Valid ID provided, break out of the loop
				break;
			}
		}

		analyzer.addPost(new SocialMediaPost(id, content, author, likes, shares, dateTime, mainPostId));
	}

	//try deleting post
	private static void deleteSocialMediaPost(SocialMediaAnalyzer analyzer, Scanner scanner)
			throws PostNotFoundException {
		System.out.print("Please provide the post ID to delete: ");
		int postId = scanner.nextInt();
		try {
		analyzer.deletePost(postId);
		}catch (PostNotFoundException e) {
			System.out.println("Post Not Found.");
		}
		
	}

	//try retrieving a post
	private static void retrieveSocialMediaPost(SocialMediaAnalyzer analyzer, Scanner scanner) {
		System.out.print("Please provide the post ID to retrieve: ");
		int postId = scanner.nextInt();
		SocialMediaPost post = analyzer.retrievePost(postId);
		if (post != null) {
			System.out.println("Retrieved post: " + post);
		} else {
			System.out.println("Post with ID " + postId + " does not exist.");
		}
	}

	//try retrieving replies to a post
	private static void retrieveAllReplies(SocialMediaAnalyzer analyzer, Scanner scanner) {
		System.out.print("Please provide the main post ID to retrieve replies: ");
		int mainPostId = scanner.nextInt();
		List<SocialMediaPost> replies = analyzer.retrieveReplies(mainPostId);
		if (!replies.isEmpty()) {
			System.out.println("Replies to post " + mainPostId + ":");
			for (SocialMediaPost reply : replies) {
				System.out.println(reply);
			}
		} else {
			System.out.println("No replies found for post " + mainPostId);
		}
	}

	//get top posts by getting input
	private static void retrieveTopPostsByLikes(SocialMediaAnalyzer analyzer, Scanner scanner) {
		System.out.print("Please specify the number of posts to retrieve (N): ");
		int n = scanner.nextInt();
		//call the topPostsByLikes with the input
		List<SocialMediaPost> topPosts = analyzer.retrieveTopNPostsByLikes(n);
		//return list if not empty
		if (!topPosts.isEmpty()) {
			System.out.println("The top-" + n + " posts with the most likes are:");
			for (SocialMediaPost post : topPosts) {
				System.out.println(post);
			}
		} else {
			System.out.println("No posts found.");
		}
	}

	//same as with likes.
	private static void retrieveTopPostsByShares(SocialMediaAnalyzer analyzer, Scanner scanner) {
		System.out.print("Please specify the number of posts to retrieve (N): ");
		int n = scanner.nextInt();
		List<SocialMediaPost> topPosts = analyzer.retrieveTopNPostsByShares(n);
		if (!topPosts.isEmpty()) {
			System.out.println("The top-" + n + " posts with the most shares are:");
			for (SocialMediaPost post : topPosts) {
				System.out.println(post);
			}
		} else {
			System.out.println("No posts found.");
		}
	}

	//make sure ID, likes shares and mainId arent negative values and are ints
	private static int readNonNegativeInt(Scanner scanner) {
		int value = -1; // Initialize with an invalid value
		while (value < 0) {
			try {
				value = scanner.nextInt();
				if (value < 0) {
					throw new IllegalArgumentException("Value must be non-negative.");
				}
			} catch (InputMismatchException e) {
				System.err.println("Error: Invalid input. Please enter a valid number.");
				scanner.nextLine(); // Consume the invalid input
			} catch (IllegalArgumentException e) {
				System.err.println("Error: " + e.getMessage());
			}
		}
		return value;
	}
}