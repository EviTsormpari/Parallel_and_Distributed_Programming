import java.net.*;
import java.util.Scanner;
import java.io.*;

public class ClientProtocol {

	BufferedReader user = new BufferedReader(new InputStreamReader(System.in));
	
	public String prepareRequest() throws IOException {
	
		Scanner scan = new Scanner(System.in);
		
		
		System.out.print("Enter a number to send to server:");
		while (!scan.hasNextInt()) {
		    System.out.print("Invalid input. Please enter an integer: ");
		    scan.nextLine(); 
		  }
		
		int number = scan.nextInt();
		return String.valueOf(number);
	}

	public void processReply(String theInput) throws IOException {
	
		System.out.println("Message received from server: " + theInput);
	}
}
