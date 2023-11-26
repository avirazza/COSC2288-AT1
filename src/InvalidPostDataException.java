// Custom exception class for invalid post data
public class InvalidPostDataException extends Exception {
	public InvalidPostDataException(String message) {
		super(message);
	}
}
