import java.io.*;
import java.net.*;

public class EchoClientTCP { 
   private static final String HOST = "localhost";
   private static final int PORT = 1234;
   private static final String EXIT = "CLOSE";

   public static void main(String[] arg) throws IOException, ClassNotFoundException {
        
	//Attempt to connect with the server's port. When the connection request from the server is accepted, the connection is established and the client's data socket is connected to the server's data socket.
	 Socket socketConnection = new Socket(HOST, PORT);

	 // Create input and output streams to receive/send serialized objects
	 ObjectOutputStream clientOutputStream = new
		ObjectOutputStream(socketConnection.getOutputStream());
	 ObjectInputStream clientInputStream = new 
		ObjectInputStream(socketConnection.getInputStream());

	 ClientProtocol app = new ClientProtocol();
	 String outmsg;
	 outmsg = app.prepareRequest(); //Client prepares the request.
     
	 //Regular expression to seperate the number from the letters.
	 String[] part = outmsg.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");

	 Request req;
	 //Validation
	 if(part[0].equals("1") || part[0].equals("2")) {
		 req = new Request(part[0], part[1], " "); 
	 } else {
		 req = new Request(part[0], part[1], part[2]);
	 }

	 clientOutputStream.writeObject(req); //Client sends the request object to the server.
	 Reply rep = (Reply)clientInputStream.readObject(); //Client receives the reply object from the server.

	 //System.out.println("Reply opcode = " + rep .getOpcode());
	 System.out.println("Reply value = " + rep .getText());

	 
	 clientOutputStream.close();
	 clientInputStream.close();

	 socketConnection.close();
   }
}			

