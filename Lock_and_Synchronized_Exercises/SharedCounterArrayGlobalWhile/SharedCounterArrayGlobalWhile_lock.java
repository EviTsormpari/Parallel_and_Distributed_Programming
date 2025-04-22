import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * This Java program demonstrates the use of multithreading and synchronization 
 * with ReentrantLock to safely update a shared static array, with threads running 
 * in a while loop.
 * 
 * The program creates multiple threads (CounterThread), and each thread runs a 
 * while loop where it increments the elements of a shared static integer array. 
 * Each thread increments the array element at the index `counter` and then 
 * moves to the next index until the `counter` reaches the end of the array.
 */

public class SharedCounterArrayGlobalWhile_lock {
  
    static int end = 10000;
    static int counter = 0;
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
			if (array[i] != 1) {
				errors++;
				System.out.printf("%d: %d should be 1\n", i, array[i]);
			}         
		}
        System.out.println (errors+" errors.");
    }


    static class CounterThread extends Thread {
  	
       public CounterThread() {
       }
  	
       public void run() {
       
            while (true) {
				
				lock.lock();
				try{
					if (counter >= end) 
                		break;
					array[counter]++;
					counter++;
				} finally {
					lock.unlock();
				}
            			
            } 
		}            	
    }
}
  
