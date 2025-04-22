import java.io.*;
import java.net.*;

public class EchoServerTCP {
   private static final int PORT = 1234;
   private static final String EXIT = "CLOSE";

   public static void main(String[] arg) throws IOException, ClassNotFoundException {

	// Create a ServerSocket to listen for incoming client connections
	 ServerSocket socketConnection = new ServerSocket(PORT);

	 System.out.println("Server Waiting");

	 //When there is a connection request from the client, a pipe socket is created which connects to the client's socket and the connection is established.
	 Socket pipe = socketConnection.accept();

	 // Create input and output streams to receive/send serialized objects
	 ObjectInputStream serverInputStream = new    
		ObjectInputStream(pipe.getInputStream());

	 ObjectOutputStream serverOutputStream = new 
		ObjectOutputStream(pipe.getOutputStream());

	 // Create an instance of the server's protocol handler
	 ServerProtocol serverProt = new ServerProtocol();

	 // Read the Request object sent by the client
	 Request req = (Request) serverInputStream.readObject();

	 // Process the request and generate a reply
	 Reply rep = serverProt.processRequest(req);

	 // Send the reply object back to the client
	 serverOutputStream.writeObject(rep);


	 serverInputStream.close();
	 serverOutputStream.close();
	 socketConnection.close();
	 pipe.close();

   }

}			

