

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class CalculatePiServer {

	private static final String HOST = "localhost";
	private static final int PORT = Registry.REGISTRY_PORT; // 1099
	
	public static void main(String[] args) throws Exception {
				
		// Should be first, especially if server is NOT localhost
		System.setProperty("java.rmi.server.hostname", HOST);
                
		// Remote object creation
		CalculatePi robj = new CalculatePiImpl();
		
		// Choose the appropriate method:
		// get if rmi registry is alreadt running, create otherwise 
		// Registry registry = LocateRegistry.getRegistry(HOST, PORT);
		Registry registry = LocateRegistry.createRegistry(PORT);
		
		// Bind remote object to a name and publish in rmi registry
                
		String rmiObjectName = "CalculatePi";
		registry.rebind(rmiObjectName, robj);
		System.out.println("Remote object bounded.");
		
		// Server is running until we press a key
		
		System.out.println("Press <Enter> for exit.");
		System.in.read();
		
		// Free space and clear rmi registry
		
		UnicastRemoteObject.unexportObject(robj, true);
		registry.unbind(rmiObjectName);
		System.out.println("Remote object unbounded.");
	}
}
