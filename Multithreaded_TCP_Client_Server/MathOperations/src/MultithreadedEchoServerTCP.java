import java.net.*;
import java.io.*;

public class MultithreadedEchoServerTCP {
	private static final int PORT = 1234;
	
	public static void main(String args[]) throws IOException {

		//Server socket which listens at server's port and waits for connection requests from clients sockets.
		ServerSocket connectionSocket = new ServerSocket(PORT);
		
		while (true) {	

			System.out.println("Server is listening to port: " + PORT);
			// Wait for a client connection (blocking call)
			Socket dataSocket = connectionSocket.accept();
			System.out.println("Received request from " + dataSocket.getInetAddress());

			// Create a new thread to handle the client communication
			ServerThread sthread = new ServerThread(dataSocket);
			sthread.start();
		}
	}
}


