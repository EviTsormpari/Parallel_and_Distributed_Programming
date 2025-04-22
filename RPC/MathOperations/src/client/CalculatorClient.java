

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;


public class CalculatorClient {

	private static final String HOST = "localhost";
	private static final int PORT = Registry.REGISTRY_PORT; // 1099
	
	public static void main(String[] args) {
				
		try {
			// Locate rmi registry
			Registry registry = LocateRegistry.getRegistry(HOST, PORT);
			
			// Look up for a specific name and get remote reference (stub)
			String rmiObjectName = "MyCalculator";
			Calculator ref = (Calculator)registry.lookup(rmiObjectName);
			
			
			Scanner in = new Scanner(System.in);
			System.out.print("Enter message to send to server: ");
			String theOutput = in.nextLine();
			
			// Do remote method invocation
			String result = ref.calculate(theOutput);
			System.out.println("The result of the calculation is: " + result);
		} catch (RemoteException re) {
			System.out.println("Remote Exception");
			re.printStackTrace();
		} catch (Exception e) {
			System.out.println("Other Exception");
			e.printStackTrace();
		}
	}
}
