
public class DoubleCounter {

	private int n1, n2, end;
	private Object n1O = new Object();
	private Object n2O = new Object();
	
	
	public DoubleCounter(int end) {
		n1 = n2 = 0;
		this.end = end;
	}
	
	public void increase_n1() {
		synchronized(n1O) {
			n1++;
		}
		
	}
	
	public void increase_n2() {
		synchronized(n2O) {
			n2++;
		}
	}
	
	public void check_array (int numThreads) {  
		int errors = 0;

		System.out.println ("Checking...");

			if (n1 != end*numThreads && n2 != end*numThreads) {
				errors++;
				System.out.println("n1 and n2 should be " + numThreads * end + " but are n1: " + n1 + " and n2: " + n2);
			}         
        System.out.println (errors+" errors.");
	}
}
