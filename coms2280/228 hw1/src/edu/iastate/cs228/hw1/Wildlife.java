package edu.iastate.cs228.hw1;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner; 

/**
 *  
 * @author peter.collins
 *
 */

/**
 * 
 * The Wildlife class performs a simulation of a grid plain with
 * squares inhabited by badgers, foxes, rabbits, grass, or none. 
 *
 */
public class Wildlife 
{
	/**
	 * Update the new plain from the old plain in one cycle. 
	 * @param pOld  old plain
	 * @param pNew  new plain 
	 */
	public static void updatePlain(Plain pOld, Plain pNew)
	{
		// 
		// For every life form (i.e., a Living object) in the grid pOld, generate  
		// a Living object in the grid pNew at the corresponding location such that 
		// the former life form changes into the latter life form. 
		// 
		// Employ the method next() of the Living class. 

		int n = pOld.getWidth();

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				pNew.grid[i][j] = pOld.grid[i][j].next(pNew);

			}
		}
	}

	/**
	 * Repeatedly generates plains either randomly or from reading files. 
	 * Over each plain, carries out an input number of cycles of evolution. 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException
	{	
		// TODO 
		// 
		// Generate wildlife simulations repeatedly like shown in the 
		// sample run in the project description. 
		// 
		// 1. Enter 1 to generate a random plain, 2 to read a plain from an input
		//    file, and 3 to end the simulation. (An input file always ends with 
		//    the suffix .txt.)
		// 
		// 2. Print out standard messages as given in the project description. 
		// 
		// 3. For convenience, you may define two plains even and odd as below. 
		//    In an even numbered cycle (starting at zero), generate the plain 
		//    odd from the plain even; in an odd numbered cycle, generate even 
		//    from odd. 

		Plain even = null;   				 // the plain after an even number of cycles 
		Plain odd = null;                   // the plain after an odd number of cycles

		// 4. Print out initial and final plains only.  No intermediate plains should
		//    appear in the standard output.  (When debugging your program, you can 
		//    print intermediate plains.)
		// 
		// 5. You may save some randomly generated plains as your own test cases. 
		// 
		// 6. It is not necessary to handle file input & output exceptions for this 
		//    project. Assume data in an input file to be correctly formated. 

		Scanner sc = new Scanner(System.in);

		int input = 0;

		int trial = 1;

		int width = 0;

		boolean isEven = true;

		System.out.println("Enter 1 to generate a random plain, 2 to read a plain from an input file, and 3 to end the simulation.");
		System.out.print("Enter command: ");

		while (true) {

			input = sc.nextInt();

			System.out.println("Trial " + trial + ": " + input);


			// random plain
			if(input == 1) {

				while (width < 1) {
					System.out.print("Enter width for Random Plain: ");
					width = sc.nextInt();
				}

				even = new Plain(width);
				even.randomInit();
				odd = new Plain(width);


			}

			else if (input == 2) {
				System.out.print("Enter filename, including .txt: ");
				String file = sc.next();

				even = new Plain(file);
				odd = new Plain(even.getWidth());
			}


			else if (input == 3) {
				sc.close();
				break;
			}


			// start the cycles

			int cycles = 0;

			while(cycles < 1) {
				System.out.print("Enter number of cycles: ");
				cycles = sc.nextInt();
			}

			System.out.println("Initial Plain: ");
			// even starts
			System.out.println(even.toString());

			for (int i = 1; i <= cycles; i++) {
				if (i % 2 == 1) {
					// even
					updatePlain(even, odd);
					isEven = false;
				}
				else if (i % 2 == 0) {
					// odd
					updatePlain(odd, even);
					isEven = true;
				}			
			}

			if (isEven) {
				System.out.println("Final plain: \n" + odd);
			} else if (!isEven) {
				System.out.println("Final plain: \n" + even);
			}

			trial ++;
		}

	}
}









