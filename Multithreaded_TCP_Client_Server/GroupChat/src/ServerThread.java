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
	private Socket myDataSocket;
	private Socket otherDataSocket;
	private InputStream is;
   	private BufferedReader in;
	private OutputStream os;
   	private PrintWriter out;
   	private HashMap<Integer,Socket> sockets;
   	private int myId;
	private static final String EXIT = "CLOSE";

   	public ServerThread(HashMap<Integer,Socket> sockets, int id)
   	{
   			this.sockets = sockets;
   			myId = id;

			//Find the datasockets
   			synchronized(sockets) {
   	      		myDataSocket = sockets.get(myId); //Get the socket for this client.
   				otherDataSocket = sockets.get(myId); //Initially set to the same, but will be updated later.
   			}

      		try {
			//Initialize input/output streams for data communication.
			is = myDataSocket.getInputStream();
			in = new BufferedReader(new InputStreamReader(is));
			os = otherDataSocket.getOutputStream();
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
			inmsg = in.readLine(); //Receive the first message from the client
			ServerProtocol app = new ServerProtocol();
			outmsg = app.processRequest(inmsg); //Process the message
			while(!outmsg.equals(EXIT)) {
				synchronized(sockets){
					// Loop through all clients and send the processed message to each (except the sender). Such as group chat
					for(Integer id: sockets.keySet()) {
						if(id!=myId) {
							System.out.println(id+" "+myId);
							os = sockets.get(id).getOutputStream(); //Get the output stream for the other client.
							out = new PrintWriter(os,true);// Create a PrintWriter for sending messages.
							out.println(outmsg);  //Send the message to the other client.
						}
					}
				}
				inmsg = in.readLine(); //Read the next message from the client
				outmsg = app.processRequest(inmsg);	//Process the new message	
			}	

			myDataSocket.close();
			System.out.println("Data socket closed");
			
			//Remove the client from the socket map after the session ends.
			synchronized(sockets) {
				sockets.remove(myId);
			}

		} catch (IOException e)	{		
	 		System.out.println("I/O Error " + e);
		}
	}	
}	
			
		
