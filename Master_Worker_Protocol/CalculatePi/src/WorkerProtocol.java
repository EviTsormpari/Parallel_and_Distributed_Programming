import java.net.*;
import java.io.*;

public class WorkerProtocol {

	private int numWorkers;
	
	public WorkerProtocol(int num) {
		numWorkers = num;
	}	

	public String compute(String theInput) throws IOException {
	
		String[] splited = theInput.split("\\s+");
		int range = Integer.parseInt(splited[0]);
		int id = Integer.parseInt(splited[1]);
		double step = Double.parseDouble(splited[2]);
		
		System.out.println("Worker "+ id +" calculates " + range);
		int block = range / numWorkers;
		int start = id * block;
		int stop = start + block;
		if (id == numWorkers-1) stop = range;
		System.out.println("Worker "+ id +" sums from " + start + "to " +stop);
		
		double mySum = 0;
        for(int i = start; i < stop; i++) {
            double x = ((double)i+0.5)*step;
            mySum += 4.0/(1.0+x*x);
        }
			
		String theOutput = String.valueOf(mySum);	
		System.out.println("Worker "+ id +" result " + theOutput);
		
		return theOutput;
	}
}
