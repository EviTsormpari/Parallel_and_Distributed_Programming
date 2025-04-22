
/*
 * This program approximates the value of π (pi) using numerical integration
 * of the function 4 / (1 + x^2) over the interval [0, 1].
 *
 * The computational workload is recursively divided:
 * - At each recursive step, the left subtask is executed in a new thread,
 * - while the right subtask runs sequentially in the current thread.
 *
 * This hybrid approach limits the number of concurrent threads,
 * balancing parallel performance with thread creation overhead.
 */

public class NumIntRecursiveHalf {

    public static void main(String[] args) {

        long numSteps = 100000000;
        int limit = 100000000 / 5;
        double sum = 0.0;

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
        double step = 1.0 / (double)numSteps;

        /* create and start thread 0 */
	    Recursive mytask = new Recursive(0, numSteps, limit, step);
        Thread mythread = new Thread(mytask);
	    mythread.start();

       /* wait for thread 0 */
		try {
			mythread.join();
			sum = mytask.getResult();
		} catch (InterruptedException e) {}

        double pi = sum * step;

        /* end timing and print result */
        long endTime = System.currentTimeMillis();
        System.out.printf("recursive program results with %d steps\n", numSteps);
        System.out.printf("computed pi = %22.20f\n" , pi);
        System.out.printf("difference between estimated pi and Math.PI = %22.20f\n", Math.abs(pi - Math.PI));
        System.out.printf("time to compute = %f seconds\n", (double) (endTime - startTime) / 1000);
    }
}


class Recursive implements Runnable {

	private long myFrom;
	private long myTo;
	private int myLimit;
	private double myStep;
	public  double myResult;
   
	public Recursive (long from, long to, int limit, double step) {
		this.myFrom = from;
		this.myTo = to;
		this.myLimit = limit;
		this.myStep = step;
		this.myResult = 0.0;
	}

	public double getResult() {
        return myResult;
    }

	public void run() {
		/* do recursion until limit is reached */
		//System.out.println("In thread"+Thread.currentThread().getName());
		long workload = myTo - myFrom;
		if (workload <= myLimit) {
		    //System.out.printf("Cutoff %d %d \n",myFrom, myTo);
			myResult = 0.0;
        	/* do computation */
            for (long i=myFrom; i < myTo; ++i) {
                double x = ((double)i+0.5)*myStep;
                myResult+= 4.0/(1.0+x*x);
            }
		} else {
			long mid = myFrom + workload / 2;
			//System.out.printf("L %d %d %d \n",myFrom, mid, myLimit);
			Recursive taskL = new Recursive(myFrom, mid, myLimit, myStep);
			Thread threadL = new Thread(taskL);
			threadL.start();
			//System.out.printf("R %d %d %d \n", mid, myTo, myLimit);
			Recursive taskR = new Recursive(mid, myTo, myLimit, myStep);
			taskR.run();
			try {
				threadL.join();
				myResult = taskL.myResult + taskR.myResult;
			} catch (InterruptedException e) {}
	    }
   }

}
