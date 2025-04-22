import java.util.Random;

/*
 * This program estimates the value of π (pi) using the Monte Carlo method in a multithreaded environment.
 * The core idea is based on the ratio of the area of a unit circle to the area of a square that encloses it,
 * which equals π/4. The program spawns multiple threads, each generating random (x, y) points within the square [-1,1] x [-1,1],
 * and counts how many of these points fall inside the unit circle (radius = 1).
 * Using the ratio of points inside the circle to the total number of points generated, the value of π is estimated as:
 *      π ≈ 4 * (points inside circle / total points)
 * Finally, the estimated value of π and the total execution time are printed.
 */


public class MonteCarlo {

    public static void main(String[] args) {

        int s = 1000000;

        Random rand = new Random();

        int sumCirclePoint = 0;
        int sumSquarePoint = 0;

        int numThreads = Runtime.getRuntime().availableProcessors();

        /* start timing */
        long startTime = System.currentTimeMillis();

        // create threads
		NumIntThread threads[] = new NumIntThread[numThreads];

        // thread execution   
		for (int i = 0; i < numThreads; i++) 
		{
			threads[i] = new NumIntThread(i, numThreads, rand, s);
			threads[i].start();
		}

        // wait for threads to terminate and collect result    
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
				sumCirclePoint += threads[i].getCirclePoint();
                sumSquarePoint += threads[i].getSquarePoint();
			} catch (InterruptedException e) {}
		}

        double pi = 4 * ((double) sumCirclePoint / sumSquarePoint);

        /* end timing and print result */
        long endTime = System.currentTimeMillis();
        System.out.printf("time to compute = %f seconds\n" , (double) (endTime - startTime) / 1000);
        System.out.printf("computed pi = %22.20f\n" , pi);
    }

}

class NumIntThread extends Thread
    {
        private int myStart;
        private int myStop;
        // Shared random object for generating points
        private Random random;
        //Counters for points inside the square and circle
        private int squarePoint ;
        private int circlePoint ;

        public NumIntThread(int myId, int numThreads, Random rand,  int s) {

            this.random = rand;
            this.circlePoint = 0;
            this.squarePoint = 0;

            myStart = myId * (s/ numThreads);
            myStop = myStart + (s / numThreads);
            if (myId == (numThreads - 1)) myStop = s;
        }

        public int getCirclePoint() {
            return circlePoint;
        }

        public int getSquarePoint() {
            return squarePoint;
        }

        public void run() {

            for (int i = myStart; i < myStop; i++) {
                // Generate random (x, y) coordinates in the range [-1, 1]
                double x = random.nextDouble() * 2.0 - 1.0;
                double y = random.nextDouble() * 2.0 - 1.0;;
    
                 // Count if the point is inside the square
                if (isInsideSquare(x, y))
                    squarePoint++;
    
                // Count if the point is inside the unit circle
                if (Math.pow(x, 2) + Math.pow(y, 2) <= 1)
                    circlePoint++;
            }
        }
    
        // Checks whether the point (x, y) lies inside the square [-1,1] x [-1,1], 
        // which represents a square of side length 2 centered at the origin
        public boolean isInsideSquare(double x, double y) {
            double maxX = 1;
            double maxY = 1;
            double minX = -1;
            double minY = -1;
    
            return (x >= minX && x <= maxX && y >= minY && y <= maxY);
        }
    
    }