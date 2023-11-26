import org.junit.Test;
import static org.junit.Assert.*;

public class SocialMediaPostTest {

	@Test
	public void testCreateValidSocialMediaPost() throws InvalidPostDataException, InvalidDateTimeFormatException {
		// Test creating a valid SocialMediaPost instance
		SocialMediaPost post = new SocialMediaPost(1, "Test Content", "Test Author", 100, 50, "01/01/2023 12:00", 0);
		assertNotNull(post);
	}

	@Test(expected = InvalidPostDataException.class)
	public void testCreateInvalidSocialMediaPostWithNegativeLikes()
	        throws InvalidPostDataException, InvalidDateTimeFormatException {
	    // Test creating a SocialMediaPost with negative likes (invalid)
	    SocialMediaPost post = new SocialMediaPost(2, "Invalid Content", "Invalid Author", -10, 0,
	            "02/02/2023 14:00", 0);
	}
	
	@Test(expected = InvalidPostDataException.class)
	public void testCreateInvalidSocialMediaPostWithNegativeShares()
	        throws InvalidPostDataException, InvalidDateTimeFormatException {
	    // Test creating a SocialMediaPost with negative likes (invalid)
	    SocialMediaPost post = new SocialMediaPost(2, "Invalid Content", "Invalid Author", 10, -10,
	            "02/02/2023 14:00", 0);
	}


	@Test
	public void testCreateInvalidSocialMediaPostWithInvalidDateTime() throws InvalidPostDataException {
	    // Test creating a SocialMediaPost with an invalid date-time format (invalid)
	    try {
	        SocialMediaPost post = new SocialMediaPost(3, "Invalid Date", "Invalid Author", 100, 50, "Invalid Date", 0);
	        fail("Expected InvalidDateTimeFormatException.");
	    } catch (InvalidDateTimeFormatException e) {
	        // Expected exception, do nothing
	    }
	}

}
