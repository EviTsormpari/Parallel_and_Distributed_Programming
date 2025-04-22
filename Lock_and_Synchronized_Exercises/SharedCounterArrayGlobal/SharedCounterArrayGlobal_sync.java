
/*
 * This Java program demonstrates the use of multithreading and synchronization with the synchronized keyword to safely update a shared static array. 
 * 
 * The program creates multiple threads (CounterThread), each of which increments the elements of a shared static integer array in a nested loop. 
 * For each index i, the array element at position i is incremented i times.
 */

public class SharedCounterArrayGlobal_sync {
  
    static int end = 1000;
    static int[] array = new int[end];
    static int numThreads = 4;

    public static void main(String[] args) {

		CounterThread threads[] = new CounterThread[numThreads];
		
		for (int i = 0; i < numThreads; i++) {
			threads[i] = new CounterThread();
			threads[i].start();
		}
	
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {}
		} 
		check_array ();
    }
     
    static void check_array ()  {
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
  	
       public CounterThread() {
       }
  	
       public void run() {
  
            for (int i = 0; i < end; i++) {
				for (int j = 0; j < i; j++) {
					synchronized(array) { //KALI ME8ODOS KA8WS O PINAKAS EINAI ANTIKEIMENO KAI MPOREI NA KLEIDW8EI ME sync, DEN 8A I8ELE synchronized(CounterThread.class)
						array[i]++;	
            } 	}	}
		}            	
    }
}
  
