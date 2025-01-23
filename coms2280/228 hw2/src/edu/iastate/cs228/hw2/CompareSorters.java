package edu.iastate.cs228.hw2;

import java.io.File;

/**
 *  
 * @author peter.collins
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Scanner; 
import java.util.Random; 


public class CompareSorters 
{
	/**
	 * Repeatedly take integer sequences either randomly generated or read from files. 
	 * Use them as coordinates to construct points.  Scan these points with respect to their 
	 * median coordinate point four times, each time using a different sorting algorithm.  
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException
	{		
		// 
		// Conducts multiple rounds of comparison of four sorting algorithms.  Within each round, 
		// set up scanning as follows: 
		// 
		//    a) If asked to scan random points, calls generateRandomPoints() to initialize an array 
		//       of random points. 
		// 
		//    b) Reassigns to the array scanners[] (declared below) the references to four new 
		//       PointScanner objects, which are created using four different values  
		//       of the Algorithm type:  SelectionSort, InsertionSort, MergeSort and QuickSort. 
		// 
		// 	
		PointScanner[] scanners = new PointScanner[4]; 
		Algorithm[] algos = {Algorithm.InsertionSort, Algorithm.SelectionSort, Algorithm.QuickSort, Algorithm.MergeSort};
		
		System.out.println("Performances of Four Sorting Algorithms in Point Scanning");
		System.out.println();
		
		boolean keepGoing = true;
		
		Scanner s = new Scanner(System.in);
		
		int trials = 1;
		
		System.out.println("Enter 1 for random points, 2 for file and 3 to end");
		
		while (keepGoing) {
			System.out.println();
			System.out.print("Trial " + trials + ": ");
			int input = s.nextInt();
			
			if (input == 1) {
				System.out.print("Enter number of points: ");
				int num = s.nextInt();
				System.out.println();
				Point[] pts = generateRandomPoints(num, new Random());
				System.out.println("algorithm    | size | time (ns)");
				System.out.println("-------------------------------");
				for (int i = 0; i < 4; i++) {
					scanners[i] = new PointScanner(pts, algos[i]);
					scanners[i].scan();
					System.out.println(scanners[i].stats());
				}
				trials ++;
			}
			else if (input == 2) {
				System.out.print("File Name:");
				String file = s.next();
				System.out.println();
				System.out.println();
				System.out.println("algorithm    | size | time (ns)");
				System.out.println("-------------------------------");
				for (int i = 0; i < 4; i++) {
					scanners[i] = new PointScanner(file, algos[i]);
					scanners[i].scan();
					System.out.println(scanners[i].stats());
				}
				
				trials ++;
			} 
			else {
				System.out.println("");
				break;
			}
			
		}
		
		// For each input of points, do the following. 
		// 
		//     a) Initialize the array scanners[].  
		//
		//     b) Iterate through the array scanners[], and have every scanner call the scan() 
		//        method in the PointScanner class.  
		//
		//     c) After all four scans are done for the input, print out the statistics table from
		//		  section 2.
		//
		// A sample scenario is given in Section 2 of the project description. 
		
	}
	
	
	/**
	 * This method generates a given number of random points.
	 * The coordinates of these points are pseudo-random numbers within the range 
	 * [-50,50] � [-50,50]. Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing. 
	 * 
	 * @param numPts  	number of points
	 * @param rand      Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	public static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException
	{ 
		if (numPts < 1) {
			throw new IllegalArgumentException();
		}
		Point[] arr = new Point[numPts];
		
		for (int i = 0; i < numPts; i++ ) {
			int x = rand.nextInt(101) - 50;
			int y = rand.nextInt(101) - 50;
			arr[i] = new Point(x, y);
		}
		
		return arr; 
	}
	
}
