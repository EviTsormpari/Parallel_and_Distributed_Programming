

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

// This interface is implemented by the server.
// It defines the actual remote methods that the server offers.
// It acts as the "contract" for what services the server provides remotely.

public interface CalculatePi extends Remote{

	//Signature of the remote method
	public String calcPi(String NumSteps, HashMap<Double,Double> map) throws RemoteException;
}
