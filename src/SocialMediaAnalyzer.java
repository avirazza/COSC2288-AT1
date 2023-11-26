import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class SocialMediaAnalyzer {
	private List<SocialMediaPost> posts;

	public SocialMediaAnalyzer(String csvFilePath)
			throws InvalidPostDataException, InvalidDateTimeFormatException, IOException {
		this.posts = new ArrayList<>();
		loadPostsFromCSV(csvFilePath);
	}

	public void addPost(SocialMediaPost post) throws InvalidPostDataException {
		try {
			validatePostData(post);
			posts.add(post);
			System.out.println("The post has been added to the collection!");
		} catch (InvalidPostDataException e) {
			throw new InvalidPostDataException("Error: " + e.getMessage()); 
		}
	}

	public void deletePost(int postId) throws PostNotFoundException {
		boolean removed = posts.removeIf(post -> post.getId() == postId);
		if (removed) {
			System.out.println("Post with ID " + postId + " has been deleted.");
		} else {
			throw new PostNotFoundException("Post with ID " + postId + " does not exist in the collection.");
		}
	}

	public SocialMediaPost retrievePost(int postId) {
		for (SocialMediaPost post : posts) {
			if (post.getId() == postId) {
				return post;
			}
		}
		return null;
	}

	public List<SocialMediaPost> retrieveReplies(int mainPostId) {
		return posts.stream().filter(post -> post.getMainPostId() == mainPostId).collect(Collectors.toList());
	}

	public List<SocialMediaPost> retrieveTopNPostsByLikes(int n) {
		return posts.stream().sorted(Comparator.comparingInt(SocialMediaPost::getLikes).reversed()).limit(n)
				.collect(Collectors.toList());
	}

	public List<SocialMediaPost> retrieveTopNPostsByShares(int n) {
		return posts.stream().sorted(Comparator.comparingInt(SocialMediaPost::getShares).reversed()).limit(n)
				.collect(Collectors.toList());
	}

	// Create a custom validation method for post data
	private void validatePostData(SocialMediaPost post) throws InvalidPostDataException {
		// check if likes, shares are non-negative
		if (post.getLikes() < 0 || post.getShares() < 0) {
			throw new InvalidPostDataException("Likes and shares must be non-negative.");
		}

	}

	// check if id exists; used for initial id and referred id
	public boolean postIdExists(int postId) {
		for (SocialMediaPost post : posts) {
			if (post.getId() == postId) {
				return true; // ID already exists
			}
		}
		return false; // ID does not exist
	}

	// load posts from whichever csv file is input
	private void loadPostsFromCSV(String csvFile) throws InvalidPostDataException, InvalidDateTimeFormatException {
	    BufferedReader reader = null;
	    String line;
	    try {
	        reader = new BufferedReader(new FileReader(csvFile));
	        String header = reader.readLine(); // Read the header line and skip

	        // make sure that headers are correct
	        if (!header.equals("ID,content,author,likes,shares,date-time,main_post_id")) {
	            throw new IllegalArgumentException("Invalid CSV file format. The header is incorrect.");
	        }

	        // split data along the headers
	        while ((line = reader.readLine()) != null) {
	            try {
	                String[] parts = line.split(",");
	                if (parts.length == 7) {
	                    int id = Integer.parseInt(parts[0]);
	                    String content = parts[1];
	                    String author = parts[2];
	                    int likes = Integer.parseInt(parts[3]);
	                    int shares = Integer.parseInt(parts[4]);
	                    String dateTime = parts[5];
	                    int mainPostId = Integer.parseInt(parts[6]);
	                    SocialMediaPost post = new SocialMediaPost(id, content, author, likes, shares, dateTime,
	                            mainPostId);
	                    // add
	                    posts.add(post);
	                } else {
	                    // Handle improperly formatted data
	                    System.err.println("Skipping invalid data: " + line);
	                }
	            } catch (NumberFormatException e) {
	                // Handle parsing errors for likes, shares, and IDs
	                System.err.println("Skipping invalid data: " + line);
	            }
	        }
	    } catch (IOException e) {
	        // Handle IO errors
	        System.err.println("Error reading CSV file: " + e.getMessage());
	    } finally {
	        try {
	            if (reader != null) {
	                reader.close();
	            }
	        } catch (IOException e) {
	            // Handle closing errors
	            System.err.println("Error closing file: " + e.getMessage());
	        }
	    }
	}


}
