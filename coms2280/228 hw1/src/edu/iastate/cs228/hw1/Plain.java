package edu.iastate.cs228.hw1;

/**
 *  
 * @author peter.collins
 *
 */

import java.io.File; 
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner; 
import java.util.Random; 

/**
 * 
 * The plain is represented as a square grid of size width x width. 
 *
 */
public class Plain 
{
	private int width; // grid size: width X width 

	public Living[][] grid; 

	/**
	 *  Default constructor reads from a file 
	 */
	public Plain(String inputFileName) throws FileNotFoundException
	{		
		// 
		// Assumption: The input file is in correct format. 
		// 
		// You may create the grid plain in the following steps: 
		// 
		// 1) Reads the first line to determine the width of the grid.
		// 
		// 2) Creates a grid object. 
		// 
		// 3) Fills in the grid according to the input file. 
		// 
		// Be sure to close the input file when you are done. 

		// get the file
		File f = new File(inputFileName);
		Scanner firstScanner = new Scanner(f);

		// width / counter variable
		int w = 0;

		String line = firstScanner.nextLine();

		Scanner sc = new Scanner(line);


		while (sc.hasNext()) {
			// count
			w += 1;
			sc.next();
		}

		// setup grid
		this.width = w;
		grid = new Living[w][w];

		// close scanners
		sc.close();
		firstScanner.close();

		Scanner scanner = new Scanner(f);

		int r = 0;
		int c = 0;

		while (scanner.hasNextLine()) {

			while (scanner.hasNext()) {
				// get the character for the living
				String s = scanner.next();
				
				if (s.length() > 1) {
					// get age
					int age = Character.getNumericValue(s.charAt(1));

					// set up the animal
					if (s.charAt(0) == 'B') {
						grid[r][c] = new Badger(this, r, c, age);
					}
					else if (s.charAt(0) == 'F') {
						grid[r][c] = new Fox(this, r, c, age);
					}
					else if (s.charAt(0) == 'R') {
						grid[r][c] = new Rabbit(this, r, c, age);
					}
				}
				else if (s.length() > 0) {
					if(s.charAt(0) == 'G') {
						grid[r][c] = new Grass(this, r, c);
					}
					else if(s.charAt(0) == 'E') {
						grid[r][c] = new Empty(this, r, c);
					}
				}
				c++;

			}
			r++;

		}
		// close last scanner
		scanner.close();

	}

	/**
	 * Constructor that builds a w x w grid without initializing it. 
	 * @param width  the grid 
	 */
	public Plain(int w)
	{
		width = w;
		grid = new Living[width][width];
	}


	public int getWidth()
	{
		return width;  // to be modified 
	}

	/**
	 * Initialize the plain by randomly assigning to every square of the grid  
	 * one of BADGER, FOX, RABBIT, GRASS, or EMPTY.  
	 * 
	 * Every animal starts at age 0.
	 */
	public void randomInit()
	{

		Random generator = new Random(); 

		for (int i = 0; i < width; i++) {

			for (int j = 0; j < width; j++) {

				int n = generator.nextInt(5);
				if (n == 0) {
					grid[i][j] = new Badger(this, i, j, 0);
				}
				else if (n == 1) {
					grid[i][j] = new Fox(this, i, j, 0);
				}
				else if (n == 2) {
					grid[i][j] = new Rabbit(this, i, j, 0);
				}
				else if (n == 3) {
					grid[i][j] = new Grass(this, i, j);
				}
				else {
					grid[i][j] = new Empty(this, i, j);
				}
			}
		}

	}


	/**
	 * Output the plain grid. For each square, output the first letter of the living form
	 * occupying the square. If the living form is an animal, then output the age of the animal 
	 * followed by a blank space; otherwise, output two blanks.  
	 */
	public String toString()
	{
		// initialize the string

		String result = "";

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				if (grid[i][j].who() == State.BADGER) {
					result += "B" + ((Badger) grid[i][j]).myAge() + " ";
				}
				if (grid[i][j].who() == State.RABBIT) {
					result += "R" + ((Rabbit) grid[i][j]).myAge() + " ";
				}
				if (grid[i][j].who() == State.FOX) {
					result += "F" + ((Fox) grid[i][j]).myAge() + " ";
				}
				if (grid[i][j].who() == State.GRASS) {
					result += "G" + "  ";
				}
				if (grid[i][j].who() == State.EMPTY) {
					result += "E" + "  ";
				}
			}
			result += "\n";
		}
		return result;
	}


	/**
	 * Write the plain grid to an output file.  Also useful for saving a randomly 
	 * generated plain for debugging purpose. 
	 * @throws FileNotFoundException
	 */
	public void write(String outputFileName) throws FileNotFoundException
	{
		// 
		// 1. Open the file. 
		// 
		// 2. Write to the file. The five life forms are represented by characters 
		//    B, E, F, G, R. Leave one blank space in between. Examples are given in
		//    the project description. 
		// 
		// 3. Close the file. 

		File f = new File(outputFileName);

		PrintWriter p = new PrintWriter(f);

		p.write(this.toString());

		p.close();

	}			
}
