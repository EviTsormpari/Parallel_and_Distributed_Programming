/*
 * This Java program demonstrates the use of multithreading and synchronization with the synchronized keyword to safely update a shared array. 
 * 
 * The program creates multiple threads (CounterThread), each of which increments the elements of a shared static integer array in a nested loop. 
 * For each index i, the array element at position i is incremented i times.
 */

public class SharedCounterArray_sync {
  
    

    public static void main(String[] args) {

		 int end = 1000;
    	 int[] array = new int[end];
    	 int numThreads = 4;

		CounterThread threads[] = new CounterThread[numThreads];
		
		for (int i = 0; i < numThreads; i++) {
			threads[i] = new CounterThread(end, array);
			threads[i].start();
		}
	
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {}
		} 
		check_array (end, array, numThreads);
    }
     
    static void check_array (int end, int[] array, int numThreads) {  
		int i, errors = 0;

		System.out.println ("Checking...");

        for (i = 0; i < end; i++) {
			if (array[i] != numThreads*i) {
				errors++;
				System.out.printf("%d: %d should be %d\n", i, array[i], numThreads*i);
			}         
        }
        System.out.println (errors+" errors.");
    }


    static class CounterThread extends Thread {
  	
		private int end;
		private int[] array;

       public CounterThread(int end, int[] array) {
		this.end = end;
		this.array = array;
       }
  	
       public void run() {
  
            for (int i = 0; i < end; i++) {
				for (int j = 0; j < i; j++) {
					synchronized (array) {
						array[i]++;	
					}
            } 	}
		}            	
    }
	
}
  
