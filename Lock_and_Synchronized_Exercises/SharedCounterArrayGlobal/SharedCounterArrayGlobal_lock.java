import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * This Java program demonstrates the use of multithreading and synchronization with ReentrantLock to safely update a shared static array. 
 * 
 * The program creates multiple threads (CounterThread), each of which increments the elements of a shared static integer array in a nested loop. 
 * For each index i, the array element at position i is incremented i times.
 */

public class SharedCounterArrayGlobal_lock {
  
    static int end = 1000;
    static int[] array = new int[end];
	static Lock lock = new ReentrantLock();
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
					lock.lock();
					try{
						array[i]++;
					} finally {
						lock.unlock();
					}
							
            } 	}
		}            	
    }
}
  
