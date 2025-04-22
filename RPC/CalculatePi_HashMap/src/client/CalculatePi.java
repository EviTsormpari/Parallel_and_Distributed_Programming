

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

// This interface is used by the client to interact with the server.
// It declares the same methods as the server-side interface,
// allowing the client to call them "as if" they were local.
// The client uses this to type-cast the stub object it receives.

public interface CalculatePi extends Remote{

	//Signature of the remote method
	public String calcPi(String NumSteps, HashMap<Double,Double> map) throws RemoteException;
}
