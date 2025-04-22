import java.net.*;
import java.io.*;

public class ServerProtocol {

	public String processRequest(String theInput) {
		System.out.println("Received message from client: " + theInput);
		String theOutput = null;
		
		//Regular expression to seperate the number from the letters
		String[] part = theInput.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
		switch(part[0]) {
			case "1" : 
				theOutput = part[1].toLowerCase();
				break;
			case "2" :
				theOutput = part[1].toUpperCase();
				break;
			case "3" :
				theOutput = ceaserCipher(part[1], part[2]);
				break;
			case "4" :
				theOutput = ceaserDecoder(part[1], part[2]);
				break;
			case "CLOSE":
				theOutput = "CLOSE";
				break;
		}
		
		System.out.println("Send message to client:" + theOutput);
		return theOutput;
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