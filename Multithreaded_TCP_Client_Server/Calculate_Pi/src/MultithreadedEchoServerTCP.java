import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class MultithreadedEchoServerTCP {
	private static final int PORT = 1234;
	
	public static void main(String args[]) throws IOException {

		//Server socket which listens at server's port and waits for connection requests from clients sockets.
		ServerSocket connectionSocket = new ServerSocket(PORT);
		//HashMap to store the steps and the corresponding pi value, so the server doesn't need to recalculate pi
		//if it has already been computed for those steps.
		HashMap<Double,Double> Map = new HashMap<>();
		
		while (true) {	

			System.out.println("Server is listening to port: " + PORT);
			// Wait for a client connection (blocking call)
			Socket dataSocket = connectionSocket.accept();
			System.out.println("Received request from " + dataSocket.getInetAddress());

			// Create a new thread to handle the client communication
			ServerThread sthread = new ServerThread(dataSocket, Map);
			sthread.start();
			
		}
	}
}


