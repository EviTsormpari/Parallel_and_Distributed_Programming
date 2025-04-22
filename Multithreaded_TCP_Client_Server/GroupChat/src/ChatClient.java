import java.net.*;
import java.io.*;

// Spawn a different thread for receiving messages
// Due to multiple threads used, simultaneous communication is now possible

public class ChatClient {
	private static final int PORT = 1234;
        //private static final InetAddress HOST = InetAddress.getLocalHost();
        private static final String HOST = "localhost";

	public static void main(String args[]) throws IOException
	{
		//Attempt to connect with the server's port. When the connection request from the server is accepted, the connection is established and the client's data socket is connected to the server's data socket.
		Socket dataSocket = new Socket(HOST,PORT);
                System.out.println("Connection to " + HOST + " established");

		//Create a thread to handle sending messages
		SendThread send = new SendThread(dataSocket);
		Thread thread = new Thread(send);
		thread.start();
		//Create a thread to handle receiving messages
		ReceiveThread receive = new ReceiveThread(dataSocket);
		Thread thread2 = new Thread(receive);
		thread2.start();
	}
}	

class SendThread implements Runnable{

	private Socket dataSocket;
        private OutputStream os;
        private PrintWriter out;
	
	public SendThread(Socket soc) throws IOException {
		dataSocket = soc;
		os = dataSocket.getOutputStream();
		out = new PrintWriter(os,true);
	}
	
	public void run() {
		try{
            String outmsg;
            ChatClientProtocol app = new ChatClientProtocol();
			outmsg = app.sendMessage(); //Prepare the message
			while(!outmsg.equals("CLOSE")) {
				out.println(outmsg); //Send the message
				outmsg = app.sendMessage(); //Prepare the new message
			}	
			out.println(outmsg); //Send the message
			dataSocket.close();
			
		}catch (IOException e){}
	}
	
}

class ReceiveThread implements Runnable{

	private Socket dataSocket;
        private InputStream is;
        private BufferedReader in;
	
	public ReceiveThread(Socket soc) throws IOException {
		dataSocket = soc;
                is = dataSocket.getInputStream();
		in = new BufferedReader(new InputStreamReader(is));
	}
	
	public void run() {
		try{
			String inmsg;
            ChatClientProtocol app = new ChatClientProtocol();
            inmsg = app.receiveMessage(in.readLine()); //Receive the message
			while(inmsg != null) {
				inmsg = app.receiveMessage(in.readLine()); //Receive the message
			}
		}catch (IOException e){}	
	}
	
}

