package lab2;

import java.util.Scanner;

public class StringTest {
	public static void main(String[] args) {
		String message;
		Scanner s = new Scanner(System.in);
		message = s.nextLine();
		
		System.out.println(message);
		
		int theLength = message.length();
		System.out.println(theLength);
		
		char theChar = message.charAt(6);
		System.out.println(theChar);

		theChar = message.charAt(7);
		System.out.println(theChar);
		
		String partialMessage = message.substring(0, 5);
		System.out.println(partialMessage);
		
		int y = 5;
		
		System.out.println(y*y<50);
		
	}
}
