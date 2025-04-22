/*
 * This Java program demonstrates the use of multithreading and synchronization 
 * with the synchronized keyword to safely update a shared array, with threads running 
 * in a while loop and accessing a shared counter.
 * 
 * The program creates multiple threads (CounterThread), and each thread invokes 
 * the `inc` method of the SharedCounter object to increment elements of a shared 
 * integer array. The `SharedCounter` class manages the logic for incrementing 
 * the counter and updating the array, ensuring thread-safe operations by using 
 * a `synchronized` to synchronize access to the shared `n` counter and the array.
 */

public class SharedCounterArrayWhile_sync {


    public static void main(String[] args) {

		 int end = 10000;
    	 int[] array = new int[end];
		 SharedCounter counter = new SharedCounter(end, array);
    	 int numThreads = 4;

        CounterThread threads[] = new CounterThread[numThreads];
	
		for (int i = 0; i < numThreads; i++) {
			threads[i] = new CounterThread(counter);
			threads[i].start();
		}
	
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {}
		} 
        counter.check_array (end, array);
    }
     

	static class SharedCounter {

		int n;
		private int end;
		private int[] array;
	  
		public SharedCounter (int end, int[] array){
			this.n = 0;
			this.end = end;
			this.array = array;
		}
	
		public synchronized int get() {
			   return n;
		}
		 
		public synchronized int inc() {
				if(n>=end) return 0;
				array[n] ++;
				n++;
				return 1;
		}

		public void check_array (int end, int[] array)  {
			int i, errors = 0;
	
			System.out.println ("Checking...");
	
			for (i = 0; i < end; i++) {
				if (array[i] != 1) {
					errors++;
					System.out.printf("%d: %d should be 1\n", i, array[i]);
				}         
			}
			System.out.println (errors+" errors.");
		}
		   
	}

    static class CounterThread extends Thread {
  	
	   private SharedCounter counter;

       public CounterThread(SharedCounter counter) {
			this.counter = counter;
       }
  	
       public void run() {
            while (true) {
				if(counter.inc() == 0) break;
            } 
		}            	
    }
}
  
