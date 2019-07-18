import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

	public static void main(String[] args) throws Exception {

		
		if (args.length == 1) {
			port = Integer.parseInt(args[0]);
			System.out.println(port);
		} else if (args.length == 0) {
			System.out.println("No port entered");
			System.exit(1);
		} else {
			System.out.println("Too many args supplied");
			System.exit(1);
		}

		ServerSocket sSocket = new ServerSocket(port);

		while (true) {

			Socket connection = sSocket.accept();

			System.out.println("Socket Extablished");

			try {

				BLMPRequest request = new BLMPRequest(connection);

				Thread thread = new Thread(request);

				thread.start();

			} catch (Exception e) {
				System.out.println("Error Occurred");
				return;
			}

		}

	}

}