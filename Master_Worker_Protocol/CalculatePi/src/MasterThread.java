import java.io.*;
import java.net.*;

class MasterThread extends Thread
{
	private Socket dataSocket;
	private int myId;
	private InputStream is;
   	private BufferedReader in;
	private OutputStream os;
   	private PrintWriter out;
	private Sum mySum;
	private double step;
	

   	public MasterThread(Socket socket, int id, Sum s, double step)
   	{
		dataSocket = socket;
		myId = id;
		this.step = step;
		try {
			is = dataSocket.getInputStream();
			in = new BufferedReader(new InputStreamReader(is));
			os = dataSocket.getOutputStream();
			out = new PrintWriter(os,true);
			mySum = s;
		}
		catch (IOException e)	{		
	 		System.out.println("I/O Error " + e);
		}
	}

	public void run()
	{
   		String inmsg, outmsg;
		
		try {
			MasterProtocol app = new MasterProtocol(mySum, myId, step);
			outmsg = app.prepareRequest(); //Master thread prepares request
			out.println(outmsg); //Master thread sends the request
			inmsg = in.readLine(); //Master thread receives a reply
			app.processReply(inmsg); //Master thread processes the reply
			dataSocket.close();	

		} catch (IOException e)	{		
	 		System.out.println("I/O Error " + e);
		}
	}	
}	
			
		
