import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;

public class SocialMediaAnalyzerTest {
	private SocialMediaAnalyzer analyzer;


	@Before
	public void setUp() throws InvalidPostDataException, InvalidDateTimeFormatException, IOException {
		analyzer = new SocialMediaAnalyzer("posts.csv");
	}

	@Test
	public void testRetrievePostWithInvalidID()
			throws InvalidPostDataException, InvalidDateTimeFormatException, IOException {
		assertNull(analyzer.retrievePost(-1)); // Assuming negative IDs are invalid
	}

	@Test
	public void testAddPost() throws InvalidPostDataException, InvalidDateTimeFormatException {
		SocialMediaPost post = new SocialMediaPost(1, "Test content", "Test author", 10, 5, "01/01/2023 12:00", 0);
		analyzer.addPost(post);
		assertNotNull(analyzer.retrievePost(1));
	}

	@Test
	public void testDeletePost()
			throws InvalidPostDataException, InvalidDateTimeFormatException, PostNotFoundException {
		SocialMediaPost post = new SocialMediaPost(1, "Test content", "Test author", 10, 5, "01/01/2023 12:00", 0);
		analyzer.addPost(post);
		analyzer.deletePost(1);
		assertNull(analyzer.retrievePost(1));
	}

	@Test
	public void testRetrievePostValid() throws InvalidPostDataException, InvalidDateTimeFormatException {
		SocialMediaPost post = new SocialMediaPost(1, "Test content", "Test author", 10, 5, "01/01/2023 12:00", 0);
		analyzer.addPost(post);
		assertEquals(post, analyzer.retrievePost(1));
	}

	@Test
	public void testRetrievePostInvalid() {
		assertNull(analyzer.retrievePost(99));
	}

	@Test
	public void testRetrieveReplies() throws InvalidPostDataException, InvalidDateTimeFormatException {
		SocialMediaPost post1 = new SocialMediaPost(1, "Main post", "Author1", 10, 5, "01/01/2023 12:00", 0);
		SocialMediaPost post2 = new SocialMediaPost(2, "Reply 1", "Author2", 5, 2, "01/01/2023 12:30", 1);
		SocialMediaPost post3 = new SocialMediaPost(3, "Reply 2", "Author3", 3, 1, "01/01/2023 13:00", 1);

		analyzer.addPost(post1);
		analyzer.addPost(post2);
		analyzer.addPost(post3);

		assertEquals(2, analyzer.retrieveReplies(1).size());
	}

	@Test
	public void testRetrieveTopNPostsByLikes() throws InvalidPostDataException, InvalidDateTimeFormatException {
		SocialMediaPost post1 = new SocialMediaPost(1, "Post 1", "Author1", 10, 5, "01/01/2023 12:00", 0);
		SocialMediaPost post2 = new SocialMediaPost(2, "Post 2", "Author2", 5, 2, "01/01/2023 12:30", 0);
		SocialMediaPost post3 = new SocialMediaPost(3, "Post 3", "Author3", 15, 3, "01/01/2023 13:00", 0);

		analyzer.addPost(post1);
		analyzer.addPost(post2);
		analyzer.addPost(post3);

		assertEquals(3, analyzer.retrieveTopNPostsByLikes(3).size());
	}

	@Test
	public void testRetrieveTopNPostsByShares() throws InvalidPostDataException, InvalidDateTimeFormatException {
		SocialMediaPost post1 = new SocialMediaPost(1, "Post 1", "Author1", 10, 5, "01/01/2023 12:00", 0);
		SocialMediaPost post2 = new SocialMediaPost(2, "Post 2", "Author2", 5, 2, "01/01/2023 12:30", 0);
		SocialMediaPost post3 = new SocialMediaPost(3, "Post 3", "Author3", 15, 3, "01/01/2023 13:00", 0);

		analyzer.addPost(post1);
		analyzer.addPost(post2);
		analyzer.addPost(post3);

		assertEquals(3, analyzer.retrieveTopNPostsByShares(3).size());
	}
}
