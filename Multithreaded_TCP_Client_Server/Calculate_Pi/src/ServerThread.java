import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

class ServerThread extends Thread
{
	private Socket dataSocket;
	private InputStream is;
   	private BufferedReader in;
	private OutputStream os;
   	private PrintWriter out;
	private static final String EXIT = "-1";
	private HashMap<Double,Double> myMap;

   	public ServerThread(Socket socket, HashMap<Double,Double> map)
   	{
   		myMap = map;
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
			outmsg = app.processRequest(inmsg, myMap); //Server's thread processes the request and creates the response.
			while(!outmsg.equals(EXIT)) {
				out.println(outmsg); //Server's thread sends the response to the client.
				inmsg = in.readLine(); //Server's thread receives the new request from the client.
				outmsg = app.processRequest(inmsg, myMap); //Server's thread processes the request and creates the new response.
			}		

			dataSocket.close();
			System.out.println("Data socket closed");

		} catch (IOException e)	{		
	 		System.out.println("I/O Error " + e);
		}
	}	
}	
			
		
