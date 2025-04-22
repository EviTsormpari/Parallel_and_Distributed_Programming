import java.net.*;
import java.io.*;

public class ServerProtocol {
	
	private String number1, number2;
	private String text;
	private int tmp;
	private Reply res = new Reply();

	public Reply processRequest(Request theInput) {
		System.out.println("Received message from client: " + theInput);
		
		number1 = theInput.getNumber1();
		number2 = theInput.getNumber2();
		text = theInput.getText();
		
		switch(number1) {
			case "1" : 
				res.setText(text.toLowerCase());
				break;
			case "2" :
				res.setText(text.toUpperCase());
				break;
			case "3" :
				res.setText(ceaserCipher(text, number2));
				break;
			case "4" :
				res.setText(ceaserDecoder(text, number2));
				break;
			case "CLOSE":
				res.setText("CLOSE");
				break;
		}

		System.out.println("Send message to client:" + res.getText());
		return res;
	}
	
	private String ceaserCipher(String message, String offset) {
		
		int numOffset = Integer.parseInt(offset);
		StringBuilder result = new StringBuilder();
		for (char character : message.toCharArray()) {
			if (character != ' ') {
				int originalAlphabetPosition = character - 'a';
				int newAlphabetPosition = (originalAlphabetPosition + numOffset) % 26;
				char newCharacter = (char) ('a' + newAlphabetPosition);
				result.append(newCharacter);
			} else {
				result.append(character);
			}
		}
		return result.toString();
	}
	
	private String ceaserDecoder(String message, String offset) {
		
		int numOffset = Integer.parseInt(offset);
		StringBuilder result = new StringBuilder();
		for (char character : message.toCharArray()) {
			if (character != ' ') {
				int originalAlphabetPosition = character - 'a';
				int newAlphabetPosition = (originalAlphabetPosition - numOffset + 26) % 26;
				char newCharacter = (char) ('a' + newAlphabetPosition);
				result.append(newCharacter);
			} else {
				result.append(character);
			}
		}
		return result.toString();
	}
}