import java.net.*;
import java.io.*;

public class EchoServerTCP {
	private static final int PORT = 1234;
	private static final String EXIT = "!";

	public static void main(String args[]) throws IOException {

		//Server socket which listens at server's port and waits for connection request from client's socket
		ServerSocket connectionSocket = new ServerSocket(PORT);
		System.out.println("Server is listening to port: " + PORT);

		//When there is a connection request from the client, a data socket is created which connects to the client's socket and the connection is established.
		Socket dataSocket = connectionSocket.accept();
		System.out.println("Received request from " + dataSocket.getInetAddress());

		//Creation of input and output rows to and from the data socket.
		InputStream is = dataSocket.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		OutputStream os = dataSocket.getOutputStream();
		PrintWriter out = new PrintWriter(os,true);
		
		String inmsg, outmsg;
		inmsg = in.readLine(); //Server receives the request from the client.
		ServerProtocol app = new ServerProtocol();
		outmsg = app.tringProcessRequestandProduceReply(inmsg); //Server creates the response.
		while(!outmsg.equals(EXIT)) {
			out.println(outmsg); //Server sends the response to the client.
			inmsg = in.readLine(); //Server receives the new request from the client.
			outmsg = app.tringProcessRequestandProduceReply(inmsg); //Server creates the new response.
		}

		dataSocket.close();
		connectionSocket.close();
		System.out.println("Data socket closed");	

		
	}
}			

