import java.io.*;
import java.text.*;
import java.util.*;
import java.util.stream.Collectors;

class SocialMediaPost {
	private int id;
	private String content;
	private String author;
	private int likes;
	private int shares;
	private Date dateTime;
	private int mainPostId;

	public SocialMediaPost(int id, String content, String author, int likes, int shares, String dateTime,
		    int mainPostId) throws InvalidPostDataException, InvalidDateTimeFormatException {
		    if (id < 0 || likes < 0 || shares < 0 || mainPostId < 0) {
		        throw new InvalidPostDataException("The Post can't contain negative data");
		    }

		    // Validate date-time format
		    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		    try {
		        this.dateTime = dateFormat.parse(dateTime);
		    } catch (ParseException e) {
		        throw new InvalidDateTimeFormatException("Invalid date-time format. Please use 'dd/MM/yyyy HH:mm'.");
		    }

		    this.id = id;
		    this.content = content;
		    this.author = author;
		    this.likes = likes;
		    this.shares = shares;
		    this.mainPostId = mainPostId;
		}

	public int getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public String getAuthor() {
		return author;
	}

	public int getLikes() {
		return likes;
	}

	public int getShares() {
		return shares;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public int getMainPostId() {
		return mainPostId;
	}

	@Override
	public String toString() {
		return id + " | " + content + " | " + likes;
	}

}
