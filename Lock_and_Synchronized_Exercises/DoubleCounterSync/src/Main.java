
public class Main {

	public static void main(String[] args) {
		
		int end = 10000;
		DoubleCounter counter = new DoubleCounter(end);
		int numThreads = 4;
		
		CounterThread[] threads = new CounterThread[numThreads];
		
		for (int i = 0; i < numThreads; i++) {
			threads[i] = new CounterThread(counter, end);
			threads[i].start();
		}
		
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {}
		} 
		
		counter.check_array(numThreads);

	}

}
