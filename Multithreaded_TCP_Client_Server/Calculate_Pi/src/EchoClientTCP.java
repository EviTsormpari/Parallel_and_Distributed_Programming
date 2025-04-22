import java.net.*;
import java.io.*;
public class EchoClientTCP {
	private static final String HOST = "localhost";
	private static final int PORT = 1234;
	private static final String EXIT = "-1";

	public static void main(String args[]) throws IOException {

		//Attempt to connect with the server's port. When the connection request from the server is accepted, the connection is established and the client's data socket is connected to the server's data socket.
		Socket dataSocket = new Socket(HOST, PORT);
		
		//Creation of input and output rows to and from the data socket.
		InputStream is = dataSocket.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		OutputStream os = dataSocket.getOutputStream();
		PrintWriter out = new PrintWriter(os,true);
		       	
		System.out.println("Connection to " + HOST + " established");

		String inmsg, outmsg;
		ClientProtocol app = new ClientProtocol();
		
		outmsg = app.prepareRequest(); //Client prepares the request.
		while(!outmsg.equals(EXIT)) {
			out.println(outmsg);  //Client sends the request to the server.
			inmsg = in.readLine(); //Client receives the server's response.
			app.processReply(inmsg); //Client processes the response.
			outmsg = app.prepareRequest(); //Client prepares the new request.
		}
		out.println(outmsg);

		dataSocket.close();
		System.out.println("Data Socket closed");

	}
}			

