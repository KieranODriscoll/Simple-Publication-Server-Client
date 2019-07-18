import java.io.Serializable;

public class RequestMessage implements Serializable {

	String headerDetails;
	String action;
	String ISBN;
	String title;
	String author;
	String publisher;
	String publicationYear;

	public RequestMessage(String headerDetails, String action, String ISBN, String title, String author,
			String publisher, String publicationYear) {
		this.headerDetails = headerDetails;
		this.action = action;
		this.ISBN = ISBN;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.publicationYear = publicationYear;
	}

	public void printRequest() {
		System.out.println("Response received");
		System.out.println("-------------------------------------");
		System.out.println(headerDetails);
		System.out.println("Action: " + this.action.toString());
		System.out.println("ISBN: " + String.valueOf(this.ISBN));
		System.out.println("Title: " + String.valueOf(this.title));
		System.out.println("Author: " + String.valueOf(this.author));
		System.out.println("Publisher: " + String.valueOf(this.publisher));
		System.out.println("Publication Year: " + String.valueOf(this.publicationYear));
		System.out.println("-------------------------------------");
	}
}
