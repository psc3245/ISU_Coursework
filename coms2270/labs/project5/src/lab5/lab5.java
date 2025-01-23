package lab5;

import java.util.Scanner;

public class lab5 {
	
	
	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);

		System.out.print("Type your name: ");
		String name = in.nextLine();
		
		String s = getInitials(name);
		
		// print the initials
		System.out.println("Your initials are: " + s);
		
		System.out.print("Type a word: ");
		String word = in.nextLine();
		
		if (hasVowel(word) > -1) {
			System.out.println("The first vowel is character " + (hasVowel(word) + 1));
		}
		else {
			System.out.println("There are no vowels");
		}
		
		
	}
	
	public static int hasVowel(String s) {
		// initialize ch
		char ch;
		
		// iterate through the given string
		for (int i = 0; i < s.length() - 1; i++) {
			// set ch to the current character
			ch = s.charAt(i);
			// check if character is a vowel
			if ("aeiouAEIOU".indexOf(ch) >= 0) {
				// return its position
				return i;
			}
		}
		return -1;
		
	}
	
	public static String getInitials(String name) {
		String initials = "";
		
		// if the first character is not a space, add it to initials
		if (!String.valueOf(name.charAt(0)).equals(" ")) {
			initials += String.valueOf(name.charAt(0));
		}

		// iterate through the name
		for (int i = 0; i < name.length(); i++) {
			// if there is a space, next char would be added to initials
			if ( String.valueOf(name.charAt(i)).equals(" ") ) {
				// if it isn't the last character, add it to the initials
				if ( i+1 < name.length() && !String.valueOf(name.charAt(i+1)).equals(" ")) {
					initials += String.valueOf(name.charAt(i + 1));
				}
			}
		}

		return initials;
	}

}
