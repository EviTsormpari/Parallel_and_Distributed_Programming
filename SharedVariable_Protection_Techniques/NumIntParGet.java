
/*
 * This program estimates the value of π (pi) using numerical integration
 * and the midpoint rectangle rule. The integral of the function:
 * 
 *     f(x) = 4 / (1 + x²) from 0 to 1
 * 
 * is equal to π. The program divides the integration range into small steps,
 * distributes the work among multiple threads, and uses a local variable
 * to calculate the partial sum of each thread. Then, in the main function,
 * a local 'total sum' is created which is calculated by adding up the partial sums from the threads.
 */

public class NumIntParGet {

    public static void main(String[] args) {

        long numSteps = 10000;
        double sum = 0.0;
        int numThreads = Runtime.getRuntime().availableProcessors();

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
			threads[i] = new NumIntThread(i, numThreads, numSteps,step);
			threads[i].start();
		}

        // wait for threads to terminate and collect result    
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
				sum = sum + threads[i].get();
			} catch (InterruptedException e) {}
		}

        //Calculate pi
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

        // constructor
        public NumIntThread(int id, int numThreads, long numSteps, double step)
        {
            this.step = step;

            mySum = 0.0;
            myId = id;
            myStart = myId * ((int)numSteps/ numThreads);
            myStop = myStart + ((int)numSteps / numThreads);
            if (myId == (numThreads - 1)) myStop = (int)numSteps;
        }
        
        public double get() 
        {
            return mySum;
        }   

        // thread code
        public void run()
        {
            for(int i = myStart; i < myStop; i++) {
                double x = ((double)i+0.5)*step;
                mySum += 4.0/(1.0+x*x);
            }
        }
    }
