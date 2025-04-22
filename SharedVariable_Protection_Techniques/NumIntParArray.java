
/*
 * This program estimates the value of π (pi) using numerical integration
 * and the midpoint rectangle rule. The integral of the function:
 * 
 *     f(x) = 4 / (1 + x²) from 0 to 1
 * 
 * is equal to π. The program divides the integration range into small steps,
 * distributes the work among multiple threads, and uses a shared array
 * to safely add each thread's partial sum at its corresponding index in the array.
 * Then calculate the total sum by adding up the partial sums of the threads in the shared array.
 */

public class NumIntParArray {

    public static void main(String[] args) {

        long numSteps = 10000;
        double sum = 0.0;
        int numThreads = Runtime.getRuntime().availableProcessors();

        // Array to store local sums from each thread
        double[] tsum = new double[numThreads];
		for(int i = 0; i < numThreads; i++)
			tsum[i] = 0.0;

        /* parse command line
        if (args.length != 1) {
		System.out.println("arguments:  number_of_steps");
                System.exit(1);
        }
        try {
		numSteps = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
		System.out.println("argument "+ args[0] +" must be long int");
		System.exit(1);
        }*/
        
        /* start timing */
        long startTime = System.currentTimeMillis();

        // create threads
		NumIntThread threads[] = new NumIntThread[numThreads];

        double step = 1.0 / (double)numSteps;

        // thread execution   
		for (int i = 0; i < numThreads; i++) 
		{
			threads[i] = new NumIntThread(i, numThreads, numSteps,step, tsum);
			threads[i].start();
		}

        // wait for threads to terminate and collect result    
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
				sum = sum + tsum[i];
			} catch (InterruptedException e) {}
		}

        //calculate pi
        double pi = sum * step;

        /* end timing and print result */
        long endTime = System.currentTimeMillis();
        System.out.printf(" program results with %d steps\n", numSteps);
        System.out.printf("computed pi = %22.20f\n" , pi);
        System.out.printf("difference between estimated pi and Math.PI = %22.20f\n", Math.abs(pi - Math.PI));
        System.out.printf("time to compute = %f seconds\n", (double) (endTime - startTime) / 1000);
    }

}

class NumIntThread extends Thread
    {
        public double mySum;
        private int myId;
        private int myStart;
        private int myStop;
        private double step;
        private double[] array;

        // constructor
        public NumIntThread(int id, int numThreads, long numSteps, double step, double[] sum)
        {
            this.step = step;
            this.array = sum;
            myId = id;

            mySum = 0.0;
            myId = id;
            myStart = myId * ((int)numSteps/ numThreads);
            myStop = myStart + ((int)numSteps / numThreads);
            if (myId == (numThreads - 1)) myStop = (int)numSteps;
        }
        
        // thread code
        public void run()
        {
            for(int i = myStart; i < myStop; i++) {
                double x = ((double)i+0.5)*step;
                mySum += 4.0/(1.0+x*x);
            }

            // We compute the local sum (mySum) inside the loop,
            // and store it once into the shared array outside the loop,
            // in order to avoid false sharing —
            // that is, repeated writes to shared memory within the loop,
            // which would hurt performance due to cache coherence    delays.
            array[myId] = mySum;
        }
    }