import java.net.*;
import java.io.*;

public class ClientProtocol {

	BufferedReader user = new BufferedReader(new InputStreamReader(System.in));
	
	public String prepareRequest() throws IOException {
	
		String theOutput = null;
		
		System.out.println("Menu:");
		System.out.print("1. Metatropi se peza\n2. Metatropi se kefalaia\n3. Kwdikopoihsh Ceaser\n4. Apokwdikopoihsh Ceaser\n");
		System.out.print("Enter message to send to server:<number> <input> <offset (only for options 3 and 4)>: ");
		theOutput = user.readLine();
		while(Check(theOutput) == false) {
			System.out.println("Menu:");
			System.out.print("1. Metatropi se peza\n2. Metatropi se kefalaia\n3. Kwdikopoihsh Ceaser\n4. Apokwdikopoihsh Ceaser\n");
			System.out.print("Enter message to send to server:<number> <input> <offset (only for options 3 and 4)>: ");
			theOutput = user.readLine();
		}
		return theOutput;
	}
	
	
	//Validation to ensure the user's input has the correct format.
	private boolean Check(String message) {
		String[] part = message.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
		if(!(part[0].equals("1")) && !(part[0].equals("2")) && !(part[0].equals("3")) && !(part[0].equals("4")) && !(part[0].equals("CLOSE"))){
			return false;
		} else if ((part.length < 3) && (part[0].equals("3") || part[0].equals("3"))) return false;
		
		return true;
	}


	public void processReply(String theInput) throws IOException {
	
		System.out.println("Message received from server:" + theInput);
	}
}
