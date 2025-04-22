import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerProtocol {

	
	public Double calculatePi(double steps) {
		 double mySum=0;
		 double step = 1.0 / (double)steps;
		 
		 for(int i=0; i<steps;i++) {
            double x = ((double)i+0.5)*step;
            mySum += 4.0/(1.0+x*x);
		 }
		 
		 double pi = mySum * step;
		 return pi;
	}
	
	public synchronized boolean isKey(Double key,HashMap<Double, Double> map) {
		return map.containsKey(key);
	}
	
	public synchronized void addPair(Double key, Double value, HashMap<Double, Double> map) {
		map.put(key, value);
	}

	public String processRequest(String numSteps, HashMap<Double, Double> map) {
		System.out.println("Received message from client: " + numSteps);
		
		if(numSteps.equals("-1")) {
			return numSteps;
		}
		
		double pi;
		double steps = Double.parseDouble(numSteps);
		if(isKey(steps, map)) {
			pi = map.get(steps);
			System.out.println("Value already calculated.");
		} else {
			pi = calculatePi(steps);
			addPair(steps, pi, map);
		}
		
		 System.out.println("Send message to client: " + pi);
		 return String.valueOf(pi);
	}
}