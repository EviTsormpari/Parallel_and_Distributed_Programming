import java.net.*;
import java.io.*;

public class MultithreadedSumMasterTCP {
	private static final int PORT = 1234;
	private static final int numWorkers = 4;
	public static Sum n = new Sum(10000);


	public static void main(String args[]) throws IOException {

		//Server socket which listens at master's port and waits for connection request from worker's socket
		ServerSocket connectionSocket = new ServerSocket(PORT);
		MasterThread mthread[] = new MasterThread[numWorkers];
		
		double step = 1.0 / (double)n.getAnum();
		
		for (int i=0; i<numWorkers; i++) {	

			//When there is a connection request from a worker, a data socket is created which connects to the worker's socket and the connection is established.
			Socket dataSocket = connectionSocket.accept();
			mthread[i] = new MasterThread(dataSocket, i, n, step);
			mthread[i].start();
		}
		System.out.println("All Started");
		
		for (int i=0; i<numWorkers; i++) {
			try {
				mthread[i].join(); //Wait until all threads are done to have the right result
			} catch (InterruptedException e) {}
		}
		 
		n.printResult(step); 
		 	
	}
}


