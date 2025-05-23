import java.net.*;
import java.io.*;

public class SumWorkerTCP {
	private static final String HOST = "localhost";
	private static final int PORT = 1234;
	private static final int numWorkers = 4;
	
	public static void main(String args[]) throws IOException {

		//Attempt to connect with the master's port. When the connection request from the server is accepted, the connection is established and the worker's data socket is connected to the master's data socket.
		Socket dataSocket = new Socket(HOST, PORT);
		
		InputStream is = dataSocket.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		OutputStream os = dataSocket.getOutputStream();
		PrintWriter out = new PrintWriter(os,true);
		       	
		String inmsg, outmsg;
		WorkerProtocol app = new WorkerProtocol(numWorkers);
		
		inmsg = in.readLine(); //Receives a request from the master
		outmsg = app.compute(inmsg); //Process the request
		out.println(outmsg); //Send the reply
		
		dataSocket.close();
	}
}			

