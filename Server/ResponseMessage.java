import java.io.Serializable;
import java.util.ArrayList;

public class ResponseMessage implements Serializable {

	String headerDetails;
	String statusCode;
	String body;
	ArrayList<Book> bookList = new ArrayList<Book>();

	public ResponseMessage(String headerDetails, String statusCode, String body, ArrayList<Book> bookList) {
		this.headerDetails = headerDetails;
		this.statusCode = statusCode;
		this.body = body;
		this.bookList = bookList;
	}

	public String getResponse() {
		
		String response;
		
		response = "Response received\n";
		response = response + "-------------------------------------\n";
		response = response + headerDetails + "\n";
		response = response + "Status code: " + this.statusCode + "\n";
		response = response + this.body + "\n";
		response = response + "------------------------------------\n";
		
		return response;

	}

	public String getBookList() {
		
		String response = null;
		
		if(this.bookList == null) {
			response = "Book list was null, try submitting books and trying again";
			return response;
		}else if(this.bookList.size() == 0) {
			response = "No books sent back, try submitting books and trying again";
			return response;
		}
		
		for(int i = 0; i < bookList.size(); i++) {
			if(response == null) {
				response = "ISBN: " + String.valueOf(this.bookList.get(i).ISBN) + "\n";
			}else {
				response = response + "ISBN: " + String.valueOf(this.bookList.get(i).ISBN) + "\n";
			}
			response = response + "Title: " + String.valueOf(this.bookList.get(i).title) + "\n" ;
			response = response + "Author: " + String.valueOf(this.bookList.get(i).author) + "\n";
			response = response + "Publisher: " + String.valueOf(this.bookList.get(i).publisher) + "\n";
			response = response + "Publication Year: " + String.valueOf(this.bookList.get(i).publicationYear) + "\n";
			response = response + "------------------------------------\n";
		}
		
		return response;
		
	}
	
	public String getBibtexFormat() {
		
		String response = null;
		
		if(this.bookList == null) {
			response = "Book list was null, try submitting books and trying again";
			return response;
		}else if(this.bookList.size() == 0) {
			response = "No books sent back, try submitting books and trying again";
			return response;
		}
		
		for(int i = 0; i < bookList.size(); i++) {
			String authorFirst = this.bookList.get(i).author.split(",")[0];
			if(response == null) {
				response = "@Book {" + authorFirst + this.bookList.get(i).publicationYear + ",\n";
			}else {
				response = response + "@Book {" + authorFirst + this.bookList.get(i).publicationYear + ",\n";
			}
			response = response + "\t AUTHOR = {" + this.bookList.get(i).author + "},\n";
			response = response + "\t TITLE = {" + this.bookList.get(i).title + "},\n";
			response = response + "\t PUBLISHER = {" + this.bookList.get(i).publisher + "},\n";
			response = response + "\t YEAR = {" + this.bookList.get(i).publicationYear + "},\n";
			response = response + "\t ISBN = {" + this.bookList.get(i).ISBN + "},\n";
			response = response + "}";
		}
		
		return response;
		
	}

}
