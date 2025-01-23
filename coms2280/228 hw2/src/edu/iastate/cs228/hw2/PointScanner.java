package edu.iastate.cs228.hw2;

import java.io.File;

/**
 * 
 * @author peter.collins
 *
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * 
 * This class sorts all the points in an array of 2D points to determine a reference point whose x and y 
 * coordinates are respectively the medians of the x and y coordinates of the original points. 
 * 
 * It records the employed sorting algorithm as well as the sorting time for comparison. 
 *
 */
public class PointScanner  
{
	private Point[] points; 

	private Point medianCoordinatePoint;  // point whose x and y coordinates are respectively the medians of 
	// the x coordinates and y coordinates of those points in the array points[].
	private Algorithm sortingAlgorithm;    

	private String outputFileName;


	protected long scanTime; 	       // execution time in nanoseconds. 

	/**
	 * This constructor accepts an array of points and one of the four sorting algorithms as input. Copy 
	 * the points into the array points[].
	 * 
	 * @param  pts  input array of points 
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public PointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException
	{
		points = new Point[pts.length];
		
		if (sortingAlgorithm == Algorithm.InsertionSort) {
			outputFileName = "Insert.txt";
		}
		else if (sortingAlgorithm == Algorithm.MergeSort) {
			outputFileName = "MergeSort.txt";
		}
		else if (sortingAlgorithm == Algorithm.QuickSort) {
			outputFileName = "QuickSort.txt";
		}
		else {
			outputFileName = "SelectionSort.txt";
		}
		
		sortingAlgorithm = algo;
		
		if (pts.length == 0 || pts == null) {
			throw new IllegalArgumentException();
		}

		for (int i = 0 ; i < pts.length; i++) {
			points[i] = new Point(pts[i].getX(), pts[i].getY());
		}
		sortingAlgorithm = algo;

	}


	/**
	 * This constructor reads points from a file. 
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException   if the input file contains an odd number of integers
	 */
	protected PointScanner(String inputFileName, Algorithm algo) throws FileNotFoundException, InputMismatchException
	{
		// TODO
		
		sortingAlgorithm = algo;
		
		File f = new File(inputFileName);
		Scanner s = new Scanner(f);

		int count = 0;

		while (s.hasNextInt()) {
			s.nextInt();
			count ++;
		}
		// ensure we have an even number of integers
		if (count % 2 != 0) {
			throw new InputMismatchException();
		}
		
		points = new Point[count];

		// reset scanner
		Scanner sc = new Scanner(f);

		// accumulator
		int i = 0;

		while (sc.hasNextInt()) {
			int x = sc.nextInt();
			int y = sc.nextInt();
			Point p = new Point(x, y);
			points[i] = p;
			i++;
		}
		s.close();
		sc.close();
	}


	/**
	 * Carry out two rounds of sorting using the algorithm designated by sortingAlgorithm as follows:  
	 *    
	 *     a) Sort points[] by the x-coordinate to get the median x-coordinate. 
	 *     b) Sort points[] again by the y-coordinate to get the median y-coordinate.
	 *     c) Construct medianCoordinatePoint using the obtained median x- and y-coordinates.     
	 *  
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter, InsertionSorter, MergeSorter,
	 * or QuickSorter to carry out sorting.       
	 * @param algo
	 * @return
	 */
	public void scan()
	{
		// TODO  
		AbstractSorter aSorter;
		if (sortingAlgorithm == Algorithm.InsertionSort) {
			aSorter = new InsertionSorter(points);
		}
		else if (sortingAlgorithm == Algorithm.MergeSort) {
			aSorter = new MergeSorter(points);
		}
		else if (sortingAlgorithm == Algorithm.QuickSort) {
			aSorter = new QuickSorter(points);
		}
		else {
			aSorter = new SelectionSorter(points);
		}
		
		// sort by x, and time it
		
		long xStart = System.nanoTime();

		aSorter.setComparator(0);

		aSorter.sort();

		int x = aSorter.getMedian().getX();
		
		long xTime = System.nanoTime() - xStart;
		
		// sort by y, and time it
		
		long yStart = System.nanoTime();

		aSorter.setComparator(1);

		aSorter.sort();

		int y = aSorter.getMedian().getY();
		
		long yTime = System.nanoTime() - yStart;
		
		// add up the times
		
		scanTime = xTime + yTime;
		
		medianCoordinatePoint = new Point(x, y);



		// create an object to be referenced by aSorter according to sortingAlgorithm. for each of the two 
		// rounds of sorting, have aSorter do the following: 
		// 
		//     a) call setComparator() with an argument 0 or 1. 
		//
		//     b) call sort(). 		
		// 
		//     c) use a new Point object to store the coordinates of the medianCoordinatePoint
		//
		//     d) set the medianCoordinatePoint reference to the object with the correct coordinates.
		//
		//     e) sum up the times spent on the two sorting rounds and set the instance variable scanTime. 

	}


	/**
	 * Outputs performance statistics in the format: 
	 * 
	 * <sorting algorithm> <size>  <time>
	 * 
	 * For instance, 
	 * 
	 * selection sort   1000	  9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the project description. 
	 */
	public String stats()
	{
		String result = sortingAlgorithm + "  " + points.length + "   " + scanTime;
		return result; 
	}


	/**
	 * Write MCP after a call to scan(),  in the format "MCP: (x, y)"   The x and y coordinates of the point are displayed on the same line with exactly one blank space 
	 * in between. 
	 */
	@Override
	public String toString()
	{

		return "MCP: " + medianCoordinatePoint.toString();
	}


	/**
	 *  
	 * This method, called after scanning, writes point data into a file by outputFileName. The format 
	 * of data in the file is the same as printed out from toString().  The file can help you verify 
	 * the full correctness of a sorting result and debug the underlying algorithm. 
	 * 
	 * @throws FileNotFoundException
	 */
	public void writeMCPToFile() throws FileNotFoundException
	{
		File file = new File(outputFileName);
		PrintWriter p = new PrintWriter(file);
		p.write(this.toString());
		p.close();
	}	




}
