import java.io.*;
import java.net.*;

class ServerThread extends Thread
{
	private Socket dataSocket;
	private InputStream is;
   	private BufferedReader in;
	private OutputStream os;
   	private PrintWriter out;
	private static final String EXIT = "CLOSE";

   	public ServerThread(Socket socket)
   	{
		dataSocket = socket;
		try {
			is = dataSocket.getInputStream();
			in = new BufferedReader(new InputStreamReader(is));
			os = dataSocket.getOutputStream();
			out = new PrintWriter(os,true);
		}
		catch (IOException e)	{		
	 		System.out.println("I/O Error " + e);
		}
	}

	public void run()
	{
   		String inmsg, outmsg;
		
		try {
			inmsg = in.readLine(); //Server's thread receives the request from the client.
			ServerProtocol app = new ServerProtocol();
			outmsg = app.tringProcessRequestandProduceReply(inmsg); //Server's thread creates the response.
			while(!outmsg.equals(EXIT)) {
				out.println(outmsg); //Server's thread sends the response to the client.
				inmsg = in.readLine(); //Server's thread receives the new request from the client.
				outmsg = app.tringProcessRequestandProduceReply(inmsg); //Server's thread creates the new response.
			}		

			dataSocket.close();
			System.out.println("Data socket closed");

		} catch (IOException e)	{		
	 		System.out.println("I/O Error " + e);
		}
	}	
}	
			
		
