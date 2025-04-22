import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/*
 * This program estimates the value of pi using numerical integration
 * with the midpoint rule. It divides the integration task among multiple threads,
 * and each thread computes a portion of the integral. A parallel reduction tree 
 * is used to combine the results efficiently, using a cyclic barrier to synchronize
 * the threads at each level of the reduction. At each level j of the tree, only threads with ID divisible by 2^j
* will perform the addition. This ensures that threads merge results
*in a hierarchical tree-like structure.
 */

public class NumIntParTree {

    public static void main(String[] args) {

        long numSteps = 10000;
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
			} catch (InterruptedException e) {}
		}

        //Calculate pi
        // The final result of pi is stored in tsum[0] after the reduction
        double pi = tsum[0] * step;

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
        private int numThreads;

        // barrier
	    static CyclicBarrier barrier; 

        // constructor
        public NumIntThread(int id, int numThreads, long numSteps, double step, double[] sum)
        {
            this.step = step;
            this.array = sum;
            myId = id;
            this.numThreads = numThreads;
            mySum = 0.0;
            myId = id;

            myStart = myId * ((int)numSteps/ numThreads);
            myStop = myStart + ((int)numSteps / numThreads);
            if (myId == (numThreads - 1)) myStop = (int)numSteps;

            // barrier
		    barrier = new CyclicBarrier(numThreads);
        }
        
        // thread code
        public void run()
        {

            for(int i = myStart; i < myStop; i++) {
                double x = ((double)i+0.5)*step;
                mySum += 4.0/(1.0+x*x);
            }

            // Store local sum in shared array (outside the loop to avoid false sharing)
            array[myId] = mySum;
            // Perform parallel reduction tree to combine results
            reduction_tree();
        }

        void reduction_tree()
	    {
            int tree_depth = (int)(Math.log(numThreads) / Math.log(2));
            
            for (int j=1; j<=tree_depth; j++) {
                // barrier wait
                // Synchronize all threads at each level of the tree
                try {

                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                
                // Only threads with ID divisible by 2^j will perform the addition at this level
                if ((myId % (int)Math.pow(2, j)) == 0) {
                    array[myId] += array[myId + (int)Math.pow(2, j-1)];
                    System.out.println("j ="+j+" myId = "+myId+" sums ="+array[myId]);
                }
            }			
	    }
    }
