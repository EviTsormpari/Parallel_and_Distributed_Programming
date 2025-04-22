

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CalculatePiImpl extends UnicastRemoteObject implements CalculatePi{

	private static final long serialVersionUID = 1;

	public CalculatePiImpl() throws RemoteException {
	}
	
	public String calcPi(String numSteps) throws RemoteException {
		if(numSteps.equals("-1")) {
			return numSteps;
		}
		
		Double steps = Double.parseDouble(numSteps);
		
		 double mySum=0;
		 double step = 1.0 / (double)steps;
		 
		 for(int i=0; i<steps;i++) {
             double x = ((double)i+0.5)*step;
             mySum += 4.0/(1.0+x*x);
		 }
		 
		 double pi = mySum * step;
		 System.out.println("Send message to client: " + pi);
		 return String.valueOf(pi);
	}
}
