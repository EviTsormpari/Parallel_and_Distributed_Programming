import java.io.Serializable;

public class Request implements Serializable {

	   private String number1,number2;
	   private String text;
	   
	   Request(String num, String text, String num2) {
	     this.number1 = num;
	     this.text = text;
	     this.number2 = num2;
	   }

	   public String getNumber1() {
	      return number1;
	   }
	   
	   public String getNumber2() {
		      return number2;
		   }

	   public String getText() {
	      return text;
	   }

	}
