import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class BLMPRequest implements Runnable {

	Socket socket;
	private static ArrayList<Book> bookList = new ArrayList<Book>();
	RequestMessage requestMessage;
	private static String protocolName = "Bibliography List Management Protocol";
	private static String hostname;
	private static String ipAddress;
	private static String headerDetails;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public BLMPRequest(Socket socket) {
		this.socket = socket;
		this.requestMessage = null;
	}

	@Override
	public void run() {

		try {
			hostname = InetAddress.getLocalHost().getHostName();
			ipAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		headerDetails = protocolName + "\n" + "Hostname: " + hostname + "\n" + "IP Address: " + ipAddress;

		try {

			try {
				out = new ObjectOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				System.out.println("IOError with ObjectOutputStream");
				return;
			}
			ResponseMessage response = new ResponseMessage(headerDetails, "100", "Connection established successfully",
					null);

			try {
				out.writeObject(response);
			} catch (IOException e) {
				System.out.println("IOError when writing to socket");
				return;
			}

			handleRequest();

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}

		try {
			out.close();
			in.close();
			socket.close();
		} catch (Exception e) {
			System.out.println("Error closing streams or socket");
		}

		return;

	}

	public void handleRequest() throws Exception {

		try {
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.out.println("IOError with Object Streams");
			return;
		}

		while (true) {

			try {
				requestMessage = (RequestMessage) in.readObject();
				requestMessage.printRequest();
			} catch (EOFException e) {
				System.out.println("Connection Terminated");
				in.close();
				socket.close();
				out.close();
				return;
			} catch (SocketException e) {
				System.out.println("Connection dropped");
				in.close();
				socket.close();
				out.close();
				return;
			}

			if (requestMessage.action.toUpperCase().equals("SUBMIT")) {
				submit();
			} else if (requestMessage.action.toUpperCase().equals("UPDATE")) {
				update();
			} else if (requestMessage.action.toUpperCase().equals("REMOVE")) {
				remove();
			} else if (requestMessage.action.toUpperCase().equals("GET")) {
				get();
			}
			
			out.flush();

		}

	}

	public void submit() throws IOException {
		String ISBN = requestMessage.ISBN;
		String title = requestMessage.title;
		String author = requestMessage.author;
		String publisher = requestMessage.publisher;
		String publicationYear = requestMessage.publicationYear;
		ResponseMessage response;
		String statusCode;
		String body;

		Book book = new Book(ISBN, title, author, publisher, publicationYear);

		try {
			synchronized (bookList) {
				if (!bookList.contains(book)) {
					bookList.add(book);
					statusCode = "101";
					body = "Book entry added successfully";
					response = new ResponseMessage(headerDetails, statusCode, body, null);
				} else {
					statusCode = "205";
					body = "ISBN already exists in the book list";
					response = new ResponseMessage(headerDetails, statusCode, body, null);
				}
			}
		} catch (Exception e) {
			statusCode = "201";
			body = "Book entry addition failed";
			response = new ResponseMessage(headerDetails, statusCode, body, null);

			try {
				out.writeObject(response);
			} catch (Exception e1) {
				System.out.println("Error writing object to output stream");
				return;
			}
		}

		try {
			out.writeObject(response);
		} catch (Exception e) {
			System.out.println("Error writing object to output stream");
			return;
		}

	}

	public void update() throws IOException {

		int index = 0;
		Book temp;
		ResponseMessage response;

		String ISBN = requestMessage.ISBN;
		String title = requestMessage.title;
		String author = requestMessage.author;
		String publisher = requestMessage.publisher;
		String publicationYear = requestMessage.publicationYear;

		Book book = new Book(ISBN, title, author, publisher, publicationYear);

		synchronized (bookList) {
			if (bookList.contains(book)) {
				index = bookList.indexOf(book);
				temp = bookList.get(index);
			} else {
				response = new ResponseMessage(headerDetails, "202", "Box was not found inn book list", null);

				try {
					out.writeObject(response);
				} catch (Exception e) {
					System.out.println("Error writing object to output stream");
					return;
				}

				return;
			}
		}

		if (title != null) {
			temp.title = title;
		}
		if (author != null) {
			temp.author = author;
		}
		if (publisher != null) {
			temp.publisher = publisher;
		}
		if (publicationYear != null) {
			temp.publicationYear = publicationYear;
		}

		try {
			synchronized (bookList) {
				bookList.set(index, temp);
			}
			response = new ResponseMessage(headerDetails, "102", "Successfully updated book entry", null);
			out.writeObject(response);
		} catch (IOException e) {
			System.out.println("Error writing object to output stream");
			return;
		} catch (Exception e) {
			response = new ResponseMessage(headerDetails, "202", "Error updating the book entry", null);
			try {
				out.writeObject(response);
			} catch (Exception e1) {
				System.out.println("Error writing object to output stream");
				return;
			}
		}

	}

	public void remove() {

		String ISBN = requestMessage.ISBN;
		String title = requestMessage.title;
		String author = requestMessage.author;
		String publisher = requestMessage.publisher;
		String publicationYear = requestMessage.publicationYear;

		Book book = new Book(ISBN, title, author, publisher, publicationYear);
		ResponseMessage response;

		if (bookList.size() == 0) {
			response = new ResponseMessage(headerDetails, "203", "No books in list - removal unsucessful", null);
			try {
				out.writeObject(response);
				return;
			} catch (Exception e) {
				System.out.println("Error writing object to output stream");
				return;
			}
		}

		if (ISBN == null && title == null & author == null && publisher == null && publicationYear == null) {
			synchronized (bookList) {
				for (int i = bookList.size() - 1; i >= 0; i--) {
					bookList.remove(i);
				}
			}
			response = new ResponseMessage(headerDetails, "103", "All books successfully removed", null);
			try {
				out.writeObject(response);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

		} else {

			for (int i = bookList.size(); i >= 0; i--) {
				try {
					synchronized (bookList) {
						bookList.remove(book);
					}
				} catch (Exception e) {
					response = new ResponseMessage(headerDetails, "203", "Error occurred removing book from list",
							null);
					try {
						out.writeObject(response);
					} catch (Exception e1) {
						System.out.println("Error writing object to output stream");
						return;
					}
				}
			}
			response = new ResponseMessage(headerDetails, "103", "Book successfully removed", null);
			try {
				out.writeObject(response);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}

	public void get() {

		String ISBN = requestMessage.ISBN;
		String title = requestMessage.title;
		String author = requestMessage.author;
		String publisher = requestMessage.publisher;
		String publicationYear = requestMessage.publicationYear;

		Book book = new Book(ISBN, title, author, publisher, publicationYear);
		ArrayList<Book> tempList = new ArrayList<Book>();
		ResponseMessage response;

		if (bookList.size() == 0) {
			response = new ResponseMessage(headerDetails, "204", "Book list has no entries", null);
			try {
				out.writeObject(response);
			} catch (IOException e1) {
				System.out.println("Error writing object to output stream");
			}

			return;

		}

		if (ISBN == null && title == null & author == null && publisher == null && publicationYear == null) {
			for (int i = 0; i < bookList.size(); i++) {
				tempList.add(bookList.get(i));
			}
			response = new ResponseMessage(headerDetails, "104", "All books successfully retreived", tempList);
			if (tempList.isEmpty()) {
				response = new ResponseMessage(headerDetails, "204", "No matching book found", null);
				try {
					out.writeObject(response);
					return;
				} catch (IOException e) {
					System.out.println("Error writing object to output stream");
					return;
				}
			}
			try {
				out.writeObject(response);
			} catch (IOException e) {
				response = new ResponseMessage(headerDetails, "204", "All books unsuccessfully retreived", null);

				e.printStackTrace();
			}
		} else {

			for (int i = 0; i < bookList.size(); i++) {
				if (book.equals(bookList.get(i))) {
					try {
						tempList.add(bookList.get(i));
					} catch (Exception e) {
						response = new ResponseMessage(headerDetails, "204", "Error adding book to get list", null);
						try {
							out.writeObject(response);
						} catch (IOException e1) {
							System.out.println("Error writing object to output stream");
							return;
						}
					}

				}
			}

			if (tempList.isEmpty()) {
				response = new ResponseMessage(headerDetails, "204", "No matching book found", null);
				try {
					out.writeObject(response);
				} catch (IOException e) {
					System.out.println("Error writing object to output stream");
					return;
				}
			} else {
				response = new ResponseMessage(headerDetails, "104", "Matching book(s) found", tempList);
				try {
					out.writeObject(response);
				} catch (IOException e) {
					System.out.println("Error writing object to output stream");
					return;
				}
			}
		}

	}

	public String getBookList() {

		String response = null;

		if (bookList == null) {
			response = "Book list was null, try submitting books and trying again";
			return response;
		} else if (bookList.size() == 0) {
			response = "No books sent back, try submitting books and trying again";
			return response;
		}

		for (int i = 0; i < bookList.size(); i++) {
			response = "ISBN: " + String.valueOf(bookList.get(i).ISBN) + "\n";
			response = response + "Title: " + String.valueOf(bookList.get(i).title) + "\n";
			response = response + "Author: " + String.valueOf(bookList.get(i).author) + "\n";
			response = response + "Publisher: " + String.valueOf(bookList.get(i).publisher) + "\n";
			response = response + "Publication Year: " + String.valueOf(bookList.get(i).publicationYear) + "\n";
			response = response + "------------------------------------\n";
		}

		return response;

	}

}
