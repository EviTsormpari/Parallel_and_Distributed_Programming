public class Sum {
	private static int anum;
	private static double result;
	
	public Sum (int init) {
		anum = init;
		result = 0;
	}

	public synchronized void addTo(double toAdd) {
		result += toAdd;
	}

	public synchronized void printResult (double step) {
		double pi = result * step;
	    System.out.println("Result =" + pi);
	}
	
	public synchronized String printInit () {
	    return String.valueOf(anum);
	}
	
	public int getAnum() {
		return anum;
	}
	
        
}
