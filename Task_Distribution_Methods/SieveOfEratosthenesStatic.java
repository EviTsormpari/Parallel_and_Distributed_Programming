
// This program implements the Sieve of Eratosthenes algorithm to find all prime numbers up to a given limit (size),
// using multithreading with static workload distribution among threads to parallelize the computation.

class SieveOfEratosthenesStatic
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
		MyThread threads[] = new MyThread[numThreads];
		// get current time 
		long start = System.currentTimeMillis();

		int limit = (int)Math.sqrt(size) + 1;
		// thread execution   
		for (int i = 0; i < numThreads; i++) 
		{
			threads[i] = new MyThread(limit, size, i, prime, numThreads);
			threads[i].start();
		}
		
		// wait for threads to terminate   
		for (int i = 0; i < numThreads; i++) {
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
	private int size;
	private boolean[] prime;
	private int myStart, myStop;

	public MyThread(int limit, int size, int myId, boolean[] prime, int numThreads) {
		this.size = size;
		this.prime = prime;

		//starts from 2
		if(myId != 0) {
			myStart = myId * (limit/ numThreads);
		 } else {
		 	myStart = 2;
		 }
        myStop =myStart + (limit / numThreads);
        if (myId == (numThreads - 1)) myStop = limit;
	}


	public void run() {

		for (int p = myStart; p <= myStop; p++)
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
