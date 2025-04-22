
public class OperationStructure {

	private int numa,numb;
	private String operator;
	private int result;
	
	public OperationStructure(int numa, int numb, String operator) {
		this.numa = numa;
		this.numb = numb;
		this.operator = operator;
	}
	
	public String getOperator() {
		return operator;
	}

	public void setResult(int result) {
		this.result = result;
	}
	
	public int getResult() {
		return result;
	}

	public int addition() {
		return numa + numb;
	}
	
	public int substraction() {
		return numa - numb;
	}
	
	public int multiplication() {
		return numa * numb;
	}
	
	public int division() {
		return numa/numb;
	}
	
	public int exit() {
		return 0;
	}
	
	
}
