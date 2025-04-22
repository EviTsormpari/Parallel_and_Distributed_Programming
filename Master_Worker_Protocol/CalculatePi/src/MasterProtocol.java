import java.net.*;
import java.io.*;

public class MasterProtocol {

	private Sum mySum;
	private int myId;
	private double step;

	public MasterProtocol (Sum s, int id, double step) {
		
		mySum = s;
		myId = id;
		this.step = step;
	}

	public String prepareRequest() {
	 
		String theOutput = mySum.printInit() + " " + String.valueOf(myId) + " " + String.valueOf(step);
		return theOutput;
	}
	
	public void processReply(String theInput) {
	
		double repl = Double.parseDouble(theInput);
		mySum.addTo(repl);
		
	}	
}
