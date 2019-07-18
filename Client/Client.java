import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;

public class Client {

	static ObjectOutputStream out;
	static ObjectInputStream in;
	static boolean validISBN;
	static boolean validSubmit;
	static Socket socket;
	private static String protocolName = "Bibliography List Management Protocol";
	private static String hostname;
	private static String ipAddress;
	private static String headerDetails;
	private static int confirmDelete;
	static RequestMessage request;
	static ResponseMessage response;

	public static void main(String[] args) throws Exception {

		GUI client = new GUI();
		/*
		 * Add action listeners
		 */
		client.connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent connectEvent) {
				try {
					connection(client, client.getAddress());
				} catch (UnknownHostException e) {
					client.setError("Error 301 - Could not connect.");
				} catch (Exception e) {
					e.printStackTrace();
					client.setError(
							"Unknown error occurred on connection\nCheck ip address and port\nConnection timed out");
				}
			}
		});

		client.disconnectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent disconnectEvent) {

				try {
					out.close();
					in.close();
					socket.close();
					client.setHeader("");
					client.setError("200 - successfully disconnected");
				} catch (IOException e) {
					System.out.println("Error closing in, out and socket");
					e.printStackTrace();
				}
			}
		});

		client.sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent sendEvent) {
				send(client);
			}
		});
	}

	private static boolean validateISBN(GUI client, String ISBN) {
		validISBN = false;

		try {
			long i = Long.parseLong(ISBN.trim());
			String num = String.valueOf(i);
			char[] digits = num.toCharArray();

			if (digits.length == 13) {
				int runningTotal = 0;
				for (int j = 0; j < 13; j++) {
					if (j % 2 == 0) {
						runningTotal += Character.getNumericValue(digits[j]);
					} else {
						runningTotal += (Character.getNumericValue(digits[j]) * 3);
					}
				}
				if (runningTotal % 10 == 0) {
					validISBN = true;
				} else {
					client.setError("Error 300 - ISBN does not pass validation.");
				}
			}

			else {
				client.setError("Error 300 - ISBN does not pass validation.");
			}

		} catch (NumberFormatException nfe) {
			client.setError("ISBN not a valid numerical value.");
		}
		return validISBN;
	}

	private static void connection(GUI client, String address) throws Exception {

		String ipAddress = address.split(":")[0];
		client.setHeader(address);
		int port = Integer.parseInt(address.split(":")[1]);

		socket = new Socket(ipAddress, port);
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());

		ResponseMessage response = (ResponseMessage) in.readObject();
		String responseMsg = response.getResponse();
		client.setError("");
		client.setResponse(responseMsg);
		
		response = null;
		responseMsg = null;

		client.connectButton.setEnabled(false);
		client.disconnectButton.setEnabled(true);
		client.sendButton.setEnabled(true);

		client.redraw();

		return;
	}

	private static RequestMessage createRequest(GUI client) {

		try {
			hostname = InetAddress.getLocalHost().getHostName();
			ipAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		headerDetails = protocolName + "\n" + "Hostname: " + hostname + "\n" + "IP Address: " + ipAddress;

		RequestMessage message = new RequestMessage(headerDetails, client.getRequestAction(), client.getRequestISBN(),
				client.getRequestTitle(), client.getRequestAuthor(), client.getRequestPublisher(),
				client.getRequestYear());

		if (message.ISBN.equals("")) {
			message.ISBN = null;
		}

		if (message.title.equals("")) {
			message.title = null;
		}

		if (message.author.equals("")) {
			message.author = null;
		}

		if (message.publisher.equals("")) {
			message.publisher = null;
		}

		if (message.publicationYear.equals("")) {
			message.publicationYear = null;
		}

		return message;

	}

	private static void send(GUI client) {
		
		validSubmit = false;
		client.setResponse("");

		// check what command is
		if (client.getRequestAction().equals("SUBMIT")) {

			// all fields must be present for SUBMIT
			if (client.getRequestISBN().equals("") || client.getRequestTitle().equals("")
					|| client.getRequestAuthor().equals("") || client.getRequestPublisher().equals("")
					|| client.getRequestYear().equals("")) {
				client.setError("All fields must be included");
			} else {
				if (validateISBN(client, client.getRequestISBN())) {
					validSubmit = true;
				}
			}
		} else if (client.getRequestAction().equals("UPDATE")) {

			// at least ISBN must be included
			if (client.getRequestISBN().equals("")) {
				client.setError("ISBN field must be included");
			} else {
				if (validateISBN(client, client.getRequestISBN())) {
					validSubmit = true;
				}
			}
		}

		else if (client.getRequestAction().equals("REMOVE")) {

			validSubmit = true;

			confirmDelete = JOptionPane.showConfirmDialog(client.clientFrame, "Are you sure you want to delete?",
					"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (confirmDelete == JOptionPane.NO_OPTION) {
				client.setRequestISBN("");
				client.setRequestTitle("");
				client.setRequestAuthor("");
				client.setRequestPublisher("");
				client.setRequestYear("");
				return;
			} else if (confirmDelete == JOptionPane.YES_OPTION) {
				validSubmit = true;
			}
		}

		else if (client.getRequestAction().equals("GET")) {
			validSubmit = true;

		}

		else {
			client.setError("Unknown error occured. Please try again.");
		}

		if (validSubmit) {
			request = createRequest(client);

			try {
				out.writeObject(request);
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				response = (ResponseMessage) in.readObject();
				String responseMsg = response.getResponse();
				client.setResponse(responseMsg);

				if (client.getRequestAction().equals("GET")) {
					if (client.getBibtex()) {
						responseMsg = responseMsg + response.getBibtexFormat();
						client.setResponse(responseMsg);
					} else {
						responseMsg = responseMsg + response.getBookList();
						client.setResponse(responseMsg);
					}
					responseMsg = null;
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	
	
		return;
	}

}
