
// This program implements the Sieve of Eratosthenes algorithm to find all prime numbers up to a given limit (size),
// using multithreading with cyclic workload distribution among threads to parallelize the computation.

class SieveOfEratosthenesCyclic
{
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

        MyThread threads[] = new MyThread[numThreads];
        int limit = (int)Math.sqrt(size) + 1;

        for(int i = 0; i < numThreads; i++) 
		{
			threads[i] = new MyThread(i, numThreads, size, limit, prime);
			threads[i].start();
		}
		
        // wait for threads to terminate            
		for(int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {}
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

}

class MyThread extends Thread {
    private int myrank;
    private int numThreads;
    private int size;
    private int limit;
    private boolean[] prime;

    public MyThread(int myrank, int numThreads, int size, int limit, boolean[] prime) {
        this.numThreads = numThreads;
        this.size = size;
        this.limit = limit;
        this.prime = prime;
		this.myrank = myrank;

    }

    public void run() {

        for (int p = myrank; p <= limit; p+= numThreads) 
		{
			// If prime[p] is not changed, then it is a prime
			if(prime[p] == true)
			{
				// Update all multiples of p
				for (int i = p*p; i <= size; i += p)
					prime[i] = false;
			}
		}
    }
}
