import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// This program implements the Sieve of Eratosthenes algorithm to find all prime numbers up to a given limit (size),
// using multithreading with dynamic workload distribution among threads to parallelize the computation.

public class SieveOfEratosthenesMasterWorker {

    static int totalTasks;
	static int numThreads;
	static int tasksAssigned = 1; // shared counter for tasks, 1 because we want to start from 2
	static Lock lock = new ReentrantLock();
    public static void main(String[] args)
	{  
		
		int size = 0;
		if (args.length != 1) {
			System.out.println("Usage: java SieveOfEratosthenes <size>");
			System.exit(1);
		}

		try {
			size = Integer.parseInt(args[0]);
		}
		catch (NumberFormatException nfe) {
	   		System.out.println("Integer argument expected");
    		System.exit(1);
		}
		if (size <= 0) {
				System.out.println("size should be positive integer");
	    		System.exit(1);
		}

		boolean[] prime = new boolean[size+1];

		for(int i = 2; i <= size; i++)
			prime[i] = true; 		

        int numThreads = Runtime.getRuntime().availableProcessors();
		// get current time 
		long start = System.currentTimeMillis();

        Thread[] threads = new Thread[numThreads];
        totalTasks = (int)Math.sqrt(size) + 1;

        //Create worker threads
        for(int i = 0; i < numThreads; i++) 
		{
			threads[i] = new Thread(new Worker(size, prime));
		}

        //Start worker threads
        for (int i = 0; i < numThreads; ++i)
		{
			threads[i].start();
		}
		
        // wait for threads to terminate            
		for(int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
                System.err.println("this should not happen");
            }
		}
               
		// get current time and calculate elapsed time
		long elapsedTimeMillis = System.currentTimeMillis()-start;

		int count = 0;
		for(int i = 2; i <= size; i++) 
			if (prime[i] == true) {
				//System.out.println(i); 
				count++;
			}
		System.out.println("number of primes "+count); 
		System.out.println("time in ms = "+ elapsedTimeMillis);
	}

	//Dynamic workload distribution
    private static int getTask()
	{
        lock.lock();
        try {
			if (++tasksAssigned <= totalTasks) 
				return tasksAssigned;
			else
				return -1;
        } finally {
        	lock.unlock() ;
        }			
	}

    private static class Worker implements Runnable {
  
        private int size;
        private boolean[] prime;
    
        public Worker(int size, boolean[] prime) {
            this.size = size;
            this.prime = prime;
        }
    
        public void run() {

            int element;
			
			while ((element = getTask()) >= 0)
			{
                    // If prime[p] is not changed, then it is a prime
                    if(prime[element] == true)
                    {
                        // Update all multiples of p
                        for (int i = element*element; i <= size; i += element)
                            prime[i] = false;
                    }
                }     
		}
    
    }
}



