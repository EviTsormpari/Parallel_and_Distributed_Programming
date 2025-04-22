import java.io.Serializable;

public class Reply implements Serializable {

   private String text;

   public Reply() {
	text = null;
   }

   public void setText(String n) {
     text = n;
   }

   public String getText() {
     return text;
   }

}
