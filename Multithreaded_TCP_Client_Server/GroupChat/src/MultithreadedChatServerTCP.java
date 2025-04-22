import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class MultithreadedChatServerTCP {
	private static final int PORT = 1234;
		
	public static void main(String args[]) throws IOException {

		//Each thread is assosiated with a thread so each thread knows its assigned connection
		HashMap<Integer,Socket> sockets = new HashMap<>();

		//Server socket which listens at server's port and waits for connection requests from clients sockets.
		ServerSocket connectionSocket = connectionSocket = new ServerSocket(PORT);
		int id = 0; //Thread ID
		
		while (true) {	

			System.out.println("Server is waiting first client in port: " + PORT);
			//When there is a connection request from the client, a data socket is created which connects to the client's socket and the connection is established.
			Socket dataSocket = connectionSocket.accept();
			System.out.println("Received request from " + dataSocket.getInetAddress());
			//System.out.println("Server is waiting second client in port: " + PORT);
			//Socket dataSocket2 = connectionSocket.accept();
			//System.out.println("Received request from " + dataSocket2.getInetAddress());
			
			//Mutual exclusion to add the pair to the shared hashmap
			synchronized(sockets) {
				sockets.put(id,dataSocket);
			}

			ServerThread sthread = new ServerThread(sockets, id);
			sthread.start();
			id++;
		}
	}
}


