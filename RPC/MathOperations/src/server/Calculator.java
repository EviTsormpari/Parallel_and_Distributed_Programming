
import java.rmi.Remote;
import java.rmi.RemoteException;

// This interface is implemented by the server.
// It defines the actual remote methods that the server offers.
// It acts as the "contract" for what services the server provides remotely.

public interface Calculator extends Remote{
	
		//Signature of the remote method
		public String calculate(String input) throws RemoteException;

}
