package edu.iastate.cs228.hw2;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException; 
import java.util.InputMismatchException;

/**
 *  
 * @author peter.collins
 *
 */

/**
 * 
 * This class implements the mergesort algorithm.   
 *
 */

public class MergeSorter extends AbstractSorter
{
	// Other private instance variables if needed
	
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts   input array of integers
	 */
	public MergeSorter(Point[] pts) 
	{
		// TODO  
		super(pts);
		algorithm = "mergesort";
	}


	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter. 
	 * 
	 */
	@Override 
	public void sort()
	{
		// TODO 
		mergeSortRec(points);
	}

	
	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of points. One 
	 * way is to make copies of the two halves of pts[], recursively call mergeSort on them, 
	 * and merge the two sorted subarrays into pts[].   
	 * 
	 * @param pts	point array 
	 */
	private void mergeSortRec(Point[] pts) {
		// base case
		if (pts.length <= 1) {
			return;
		}
		// make the arrays
		Point[] left = new Point[pts.length / 2 ];
		Point[] right = new Point[pts.length - (pts.length / 2)];
		
		// add the points
		for (int i = 0; i < left.length; i++) {
			left[i] = pts[i];
		}
		for (int j = 0; j < right.length; j++) {
			right[j] = pts[left.length + j];
		}
		
		// recurse
		mergeSortRec(left);
		mergeSortRec(right);
		// merge once they're sorted
		merge(pts, left, right);
		
	}

	private void merge(Point[] pts, Point[] l, Point[] r) {
		// indexes
		int lIndex = 0;
		int rIndex = 0;
		int index = 0;
		
		// sort
		while ((lIndex < l.length) && (rIndex < r.length)) {
			if (pointComparator.compare(l[lIndex], r[rIndex]) < 0) {
				pts[index] = l[lIndex++];
			} else {
				pts[index] = r[rIndex++];
			}
			index++;
		}
		// fill in the arrays
		while (lIndex < l.length) {
			pts[index++] = l[lIndex++];
		}
		while (rIndex < r.length) {
			pts[index++] = r[rIndex++];
		}
	}


}
