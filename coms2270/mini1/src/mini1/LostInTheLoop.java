package mini1;

import java.lang.reflect.Array;
import java.util.Scanner;

/**
 * Some loop practice problems!
 */
public class LostInTheLoop
{
	/**
	 * Private constructor disables instantiation.
	 */
	private LostInTheLoop()
	{

	}

	/**
	 * Returns the initial number of matching characters in
	 * two strings, allowing for a certain number of mismatches.
	 * In a memory game, the player is has to repeat a sequence of
	 * characters in a given "target" string.  The score is the number 
	 * of characters that are
	 * correctly repeated before the first mistake. However, it is
	 * also possible to allow a certain number of mistakes, as 
	 * represented by the third parameter, to be skipped over
	 * and ignored. This method returns the score, given the guessed
	 * string, the target string, and the maximum number of mistakes.
	 * For example, 
	 * <ul>
	 *   <li>for ("abcdefg", "abcxeyg", 0), the score is 3 
	 *   <li>for ("abcdefg", "abcxeyg", 1), the score is 4 
	 *   <li>for ("abcdefg", "abcxeyg", 2), the score is 5 
	 *   <li>for ("abcdefg", "abcxdef", 10), the score is 3 
	 * </ul>
	 * The method return -1 if the strings do not have the same length.
	 * @param s
	 *   guessed string
	 * @param t
	 *   target string
	 * @param maxMistakes
	 *   number of mismatches allowed to skip
	 * @return
	 *   player score
	 */
	public static int memoryGameChecker(String s, String t, int maxMistakes)
	{  
		int mistakesLeft = maxMistakes;
		int currRun = 0;

		// return -1 if the strings do not have the same length
		if (s.length() != t.length()) {
			return -1;
		}

		// since we know the lengths are the same, iterate through in a loop
		for (int i = 0; i < s.length(); i++) {
			// check if characters are the same
			if (s.charAt(i) == t.charAt(i)) {
				// add points if yes
				currRun ++;
			}
			// check if there are any mistakes left
			else if (mistakesLeft > 0) {
				// add points and remove a mistake if yes
				//currRun ++;
				mistakesLeft --;
			}
			// if both are not true, end the run
			else {
				// return the run
				return currRun;
			}

		}

		// return our best run
		return currRun;
	}

	/**
	 * Returns a string constructed from the given string by removing
	 * all runs of the same character.  For example,
	 * <ul>
	 *   <li>from "abbccccdeeef", return "abcdef"
	 *   <li>from "aaabcccc", return "abc"
	 * </ul>
	 * If the given string is empty, returns an empty string.
	 * @param s
	 *   given string
	 * @return
	 *   new string obtained by eliminating runs of the same character
	 */
	public static String compressRuns(String s)
	{
		// accumulator variable
		String result = "";

		// keeps track of how long the run is
		int runLength = 0;

		// first check if string is empty
		if (s.length() < 2) {
			return s;
		}

		// iterate through the string
		for (int i = 0; i < s.length(); i++) {
			// make sure we dont go past the end of the string
			if (i < s.length() - 1) {
				// check if the next char will continue the run
				if (s.charAt(i) == s.charAt(i + 1)) {
					// if yes, check if it has been added to the result
					if (runLength == 0) {
						result += String.valueOf(s.charAt(i));
					}
					runLength ++;
				}
				// the next char will not continue the run
				else {
					// check if it has been added yet
					if (runLength == 0) {
						result += String.valueOf(s.charAt(i));
					}
					runLength = 0;
				}
			}
		}
		// check if the last letter was part of a run
		if (s.charAt(s.length() - 2) != s.charAt(s.length() - 1)) {
			// if not, append it to the end of the string
			result += String.valueOf(s.charAt(s.length() - 1));
		}
		return result;
	}

	/**
	 * Determines how many terms of the sum of reciprocal triangle
	 * numbers are needed to get within a specified distance of 2.0.
	 * The series 
	 * <pre>
	 * 1 + 1/3 + 1/6 + 1/10 + 1/15 + ...
	 * </pre>
	 * approaches arbitrarily close to 2.0 as more terms are added.
	 * This method determines how many terms are needed
	 * to obtain a sum that is within a given margin of error
	 * of 2.0. The terms of the series are the reciprocals of the "triangle" 
	 * numbers 1, 3, 6, 10, 15, 21 ... in which the nth term is 
	 * the previous term plus n, e.g.,
	 * <pre>
	 *   3 =   1 + 2
	 *   6 =   3 + 3
	 *   10 =  6 + 4
	 *   15 = 10 + 5
	 *   21 = 15 + 6
	 * </pre>
	 * ... and so on.  Example: countTriangleNumberSum(0.4) returns 4, since
	 * <pre>
	 * 1 + 1/3 + 1/6 = 1.5
	 * 1 + 1/3 + 1/6 + 1/10 = 1.6
	 * </pre>
	 * so four terms is within 0.4 of 2.0, but three terms is not 
	 * close enough.
	 * @param err
	 *   given margin of error
	 * @return
	 *   number of terms
	 */
	public static int countTriangleNumberSum(double err)
	{
		// stores the current denominator and how much it is increasing by
		double currDenominator = 3.0;
		double currIncrememnt = 3.0;

		// stores the total
		double total = 1.0;

		// stores how many terms it takes to get within the error
		int tries = 1;

		// initializes the variable to end the loop
		boolean keepGoing = true;

		do {
			// check if we are within the error before adding
			if ( (total + err) >= 2.0) {
				// kills the loop
				keepGoing = false;
			}
			// check if we are within the error after adding
			else if ( (total + (1 / currDenominator) + err) >= 2.0) {
				tries ++;
				// kills the loop
				keepGoing = false;
			}
			// if neither of the previous are true, add and keep going
			else {
				total += (1 / currDenominator);
				currDenominator += currIncrememnt;
				currIncrememnt ++;
				tries ++;
			}

		} while (keepGoing);

		return tries;
	}

	/**
	 * Parses a string containing names and quiz scores, and
	 * returns the name and score for the highest score.
	 * For example, given the string "Steve 42 June 137 Guang 75",
	 * the method returns the string "June 137" (where there is
	 * exactly one space between the name and number).  You can
	 * assume that the names do not contain any whitespace, and
	 * that the given string is either empty or valid. If the
	 * argument is an empty string, the method returns an empty
	 * string.  If two or more names are associated with the 
	 * same highest score, the first one is returned.
	 * @param s
	 *   given string of name and score pairs
	 * @return
	 *   string with name and score for highest score
	 */
	public static String findHighestScore(String s)
	{

		// first check if string is empty
		if (s.length() < 1) {
			return "";
		}

		int max = Integer.MIN_VALUE;
		String maxPerson = "";

		String[] scores = s.split(" ");

		// iterate through scores
		for (int i = 0; i < scores.length; i++) {
			// if we are at an odd number, it will be a score
			if (i % 2  == 1) {
				// check if it is greater than our max score
				if (Integer.parseInt(scores[i]) > max ) {
					// if so, set max to be the max
					max = Integer.parseInt(scores[i]);
					maxPerson = scores[i - 1];
				}
			}
		}

		String temp = maxPerson + " " + max;

		return temp;

	}

	/**
	 * Given a size n >= 1, prints a picture of a tree to standard output,
	 * in the pattern shown below for n = 5.
	 * <pre>    
        /\
       //\\
      ///\\\
     ////\\\\
    /////\\\\\
        || 
	 *     
	 * </pre>
	 * Note that at the widest part of the tree the line should have no leading
	 * spaces.  WARNING: in Java code you can't directly use the backslash
	 * character as a literal value; the way you type a literal backslash
	 * character is with TWO backslashes: '\\'.
	 * 
	 * @param n
	 *   height of the tree, not including the trunk
	 */
	public static void printTree(int n)
	{

		for (int i = 1; i <= n; i++) {
			System.out.print(" ".repeat(n - i));
			System.out.print("/".repeat(i));
			System.out.print("\\".repeat(i));
			System.out.println("");
		}

		System.out.print(" ".repeat(n - 1));
		System.out.print("||");

	}


	/**
	 * Checks a guess for a secret word and returns a feedback string.
	 * This algorithm is inspired by, but not the same as, that used
	 * by the game Wordle.
	 * The returned string is the same length as the guess, and the
	 * character at index i is filled in as follows, where  
	 * g_i, s_i, and r_i denote the character at position i in
	 * the guess, the secret word, and the result string, respectively.
	 * 
	 * <ul>
	 *   <li>if g_i matches s_i, then r_i is '*'
	 *   <li>if g_i does not occur in the secret word at all, 
	 *   then r_i is '-'
	 *   <li>if g_i does not match s_i, but the secret word does
	 *   have an unmatched occurrence of g_i, then r_i is '?'.
	 *   (More precisely, an "unmatched occurrence" means that there is some 
	 *   index j such that g_i is equal to s_j but g_j is not equal
	 *   to s_j.)
	 * </ul>
	 * 
	 * For example,
	 * <pre>
	 *   Guess:  "guess"
	 *   Secret: "geese"
	 *   Result: "*-**-"
	 *   
	 *   Guess:  "bobbly"
	 *   Secret: "blobby"
	 *   Result: "*??*?*"
	 *   
	 *   Guess:  "aabbbb"
	 *   Secret: "abbcde"
	 *   Result: "*-*???"
	 * </pre>
	 * (Aside to Wordle fans: note that the latter case differs from 
	 * the algorithm actually
	 * used by Wordle, which would return "*-*?--", because it would 
	 * count the number of unmatched b's in the secret word and note 
	 * that there is only one, so only the first incorrect b in the guess
	 * is labeled with a question mark.  In this method we are ignoring
	 * that subtlety.)
	 * <p>
	 * The method returns null if the two given strings are not the same length.
	 * 
	 * 
	 * @param guess
	 *   the guessed word
	 * @param secret
	 *   the secret word
	 * @return
	 *   result string with feedback for the guess
	 */
	public static String wordGameChecker(String guess, String secret)
	{

		// stores the result, which we will return
		String result = "";

		// if strings are not the same length, return null
		if (guess.length() != secret.length()) {
			return null;
		}

		// create the result
		for (int i = 0; i < guess.length(); i ++) {
			// 3 cases: *, ?, -

			// case one: right letter, right place
			if (guess.charAt(i) == secret.charAt(i)) {
				result += "*";
			}

			// case 2: right letter, wrong place
			else if ( secret.contains( String.valueOf( guess.charAt(i) ) ) ) {
				// step one is count how many in guess so far
				// step two is count how many in secret
				// match the pairs
				// matched pairs get "?"
				// unmatched pairs get a "-"
				int gCount = 0;
				int sCount = 0;
				for (int j = 0; j < i; j++) {
					if(guess.charAt(i) == guess.charAt(j)) {
						gCount ++;
					}
				}
				for (int j = 0; j < guess.length(); j++) { 
					if(guess.charAt(i) == secret.charAt(j)) {
						sCount ++;
					}
				}
				// matched pairs
				if (sCount >= gCount) {
					result += "?";
				}
				// unmatched pairs
				else {
					result += "-";
				}

			}

			// case 3: wrong letter, wrong place
			else {
				result += "-";
			}

		}

		return result;
	}


	/**
	 * Given a string, returns a new string obtained by successively removing 
	 * all adjacent matching characters.  For example, given "abbc", the method
	 * returns "ac", and given "abcddcbeffg", the method returns "aeg". 
	 * Note that multiple iterations may be required for the latter; that
	 * is, after removing the matching "dd" and "ff", the resulting
	 * string is "abccbeg", which now has a matching pair "cc"; after removing
	 * "cc", the string is "abbeg, which now has a matching pair "bb". 
	 * You can assume that the given string contains alphabetic characters only.
	 * @param s
	 *   given string
	 * @return
	 *   string obtained by removing matching pairs of adjacent characters
	 */
	public static String cancelAdjacentPairs(String s)
	{

		boolean isRun = false;
		int i = 0;

		String finale  = s;

		char lastChar = '\n';

		// iterate through the string
		while (i < finale.length()) {
			// if only 2 characters are left and they're the same, return an empty string
			if (s.length() == 2) {
				if (s.charAt(0) == s.charAt(1)) {
					return "";
				}
			}
			// check for run
			if (s.charAt(i) == lastChar) {
				// there is a run
				isRun = true;
				// make sure we don't go over the limit
				if (i < s.length() - 1) {
					finale = s.substring(0, i - 1) + s.substring(i + 1);
				}
				// if they are the last 2
				else {
					finale = s.substring(0, i);
				}
			}
			// check if there has been a run
			if (isRun) {
				finale = cancelAdjacentPairs(finale);
				return finale;
			}


			lastChar = s.charAt(i);


			// increment i to avoid infinite loop
			i ++;


		}

		if (s.length() > 20) {
			finale = finale.substring(0, finale.length() - 1);
		}


		return finale;


	}
}





