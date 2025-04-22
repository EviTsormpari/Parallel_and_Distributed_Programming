
public class CounterThread extends Thread{

	private DoubleCounter counter;
	private int end;
	
	public CounterThread(DoubleCounter counter, int end) {
		this.counter = counter;
		this.end = end;
	}
	
	public void run() {
		for(int i = 0; i< end; i++) {
			counter.increase_n1();
			counter.increase_n2();
		}
	}

	
}
