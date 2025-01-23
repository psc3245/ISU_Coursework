package mini2;

/**
 * Some loop practice problems involving arrays.
 */
public class UpUpAndArray
{
	
	private UpUpAndArray() {
		
	}

  // The isPermutation code below is given.  It will be useful in 
  // implementing the rearrange() method.
  /**
   * Determines whether the given array is a permutation.
   * A permutation is defined to be an int array that
   * contains each number from 0 through n - 1 exactly
   * once, where n is the length of the array.  In other
   * words, each value is between 0 and n - 1, inclusive,
   * and there are no duplicates.
   * @param arr
   *   given array
   * @return
   *   true if the given array is a permutation, false otherwise
   */
	
  public static boolean isPermutation(int[] arr)
  {
    // set found[x] to true if we find x in arr
    boolean[] found = new boolean[arr.length];
    for (int i = 0; i< arr.length; ++i)
    {
      int x = arr[i];
      if (x < 0 || x >= arr.length || found[x])
      {
        // x is out of range, or is a duplicate
        return false;
      }
      found[x] = true;
    }
    return true;
  }
  
  /**
   * Constructs a new array consisting of elements of arr between
   * the given start and end indices, inclusive.  If start is 
   * less than zero it is clamped up to zero, and if end is greater
   * than arr.length - 1, it is clamped down to arr.length - 1. If
   * end is less than start, an empty array is returned.
   * The given array is not modified.
   * <p>
   * For example, if arr = [10, 20, 30, 40, 50, 60, 70], start is 2, and
   * end is 5, the method returns the array [30, 40, 50, 60].
   * @param arr
   *   given array
   * @param start
   *   starting index to include in the subarray
   * @param end
   *   ending index to include in the subarray
   * @return
   *   array containing elements of arr between start and end, inclusive
   */
  public static int[] makeSubArray(int[] arr, int start, int end)
  {
	
	// if end is greater than start, return empty array
	if (end < start) {
		return new int[0];
	}
	
	// if start is negative, start at the beginning of the array
	if (start < 0) {
		start = 0;
	}
	
	// if end is past the end of the array, end at arr.length - 1
	if (end > arr.length - 1) {
		end = arr.length - 1;
	}
	
	// create a new array to return
	int[] finish = new int[end - start + 1];
	
	// fill the array
	for (int i = start; i <= end; i++) {
		finish[i - start] = arr[i];
	}
	
    return finish;
  }
 
  /**
   * Exchanges adjacent pairs of elements in the given array.
   * If the given array is of odd length, the last element
   * is not moved.
   * @param arr
   *   given array to be modified
   */
  public static void swapPairs(int[] arr)
  {
	  int end;

	  // if arr is an odd length, we should end one before the last digit
	  if (arr.length % 2 == 1) {
		  end = arr.length - 1;
	  }
	  // otherwise we just go to the end
	  else {
		  end = arr.length;
	  }

	  // iterate through the array
	  for (int i = 0; i < end; i++) {
		  // only swap if we are on an even term
		  if (i % 2 == 1) {
			  int temp = arr[i - 1];
			  arr[i - 1] = arr[i];
			  arr[i] = temp;
		  }
	  }

  }
  
  /**
   * Exchanges two columns of a 2D array.  For example, if
   * arr is:
   * <pre>
   * 1 2 3 4 5
   * 6 7 8 9 0
   * </pre>
   * then after the call swapColumns(arr, 1, 4), arr contains:
   * <pre>
   * 1 5 3 4 2
   * 6 0 8 9 7
   * </pre>
   * You can assume that the given array is nonempty and 
   * rectangular (i.e. all rows are the same length).
   * If i or j is out of range, the method does nothing.
   * @param arr
   *   given 2D array
   * @param i
   *   index of column to swap
   * @param j
   *   index of column to swap
   */
  public static void swapColumns(int[][] arr, int i, int j)
  {

	  // if i or j are out of bounds, kill the function
	  if (i > arr[0].length || j > arr[0].length) {
		  return;
	  }
	  if (i < 0 || j < 0) {
		  return;
	  }

	  // if i and j are the same, there is no swap
	  if (i == j) {
		  return;
	  }

	  // create temporary arrays to store the values
	  int tempInt = 0;

	  // iterate through the array
	  for (int k = 0; k < arr.length; k++) {
		  // iterate through the inside array
		  for (int l = 0; l < arr[0].length; l++) {
			  if (l == i) {
				  tempInt = arr[k][i];
			  }
			  if (l == j) {
				  arr[k][i] = arr[k][j];
				  arr[k][j] = tempInt;
			  }
		  }
	  }
	  

  }


  
  /**
   * Rearranges the given array according to the given permutation,
   * where perm[i] is the new index of element arr[i].
   * For example, if arr is [10, 20, 30, 40] and perm is
   * [2, 0, 3, 1], then after calling this method, arr contains
   * [20, 40, 10, 30].  If perm is not the same length as arr,
   * or if perm is not a permutation, the method does nothing.
   * @param arr
   *   given array
   * @param perm
   *   given array to be used as a permutation, if possible
   */
  public static void rearrange(int[] arr, int[] perm)
  {   
	  
	// if they are not the same length, kill function
	if (arr.length != perm.length) {
		return;
	}
	
	// check if perm is a permutation
	if (!isPermutation(perm)) {
		return;
	}
	
	// create a temporary array
	int[] temp = new int[arr.length];
	
	// iterate through arr
	for (int i = 0; i < arr.length; i++) {
		// set the temp at the value of perm to arr[i]
		temp[perm[i]] = arr[i];
	}
	
	for (int i = 0; i < arr.length; i++) {
		arr[i] = temp[i];
	}
	
	
  }
  
  /**
   * Finds the longest common suffix among the strings
   * in an array.  For example, if arr is
   * ["gladly", "badly", "boodly", "sadly"], the method
   * returns the string "dly".  If arr is ["foo", "food"],
   * the method returns an empty string.
   * @param arr
   *   array of strings
   * @return
   *   longest common suffix
   */
  public static String longestCommonSuffix(String[] arr)
  {
	  
	// accumulator variable
	String result = "";
	
	// if arr[0] is empty, we will get an error so we need to kill the function
	if (arr[0].equals("") ) {
		return "";
	}

	// set last char to the last character of arr[0]
	char lastChar = arr[0].charAt(arr[0].length() - 1);

	// while loop to continuously check for run
	while (arr[0].length() > 0) {
		
		// check if all last chars are equal
		for (int i = 0; i < arr.length; i++) {
			// if not, reverse result and return
			if (arr[i].charAt(arr[i].length() - 1) != lastChar) {
				String temp = "";
				
				for (int h = result.length() - 1; h >= 0; h--) {
					temp += String.valueOf(result.charAt(h));
				}
				return temp;
			}
		}
		
		// if yes, add last char to result

		result += lastChar;
		
		// makes sure we don't substring if the length is 0
		if (arr[0].length() == 1) {
			String temp = "";
			
			for (int h = result.length() - 1; h >= 0; h--) {
				temp += String.valueOf(result.charAt(h));
			}
			return temp;
		}

		// substring all to remove last char
		for (int i = 0; i < arr.length; i++) {
			arr[i] = arr[i].substring(0, arr[i].length() - 1);
		}

		// set a new last char
		lastChar = arr[0].charAt(arr[0].length() - 1);
		
		
	}
	
	// reverse result
	String temp = "";
	
	for (int i = result.length() - 1; i >= 0; i--) {
		temp += String.valueOf(result.charAt(i));
	}

	// return final product
	return temp;

  }
  
  /**
   * Inserts one array into another at the given position.  Existing
   * elements are shifted to the right.  For example, if 
   * arr is [10, 20, 30, 40, 50, 60], pos is 1, and toInsert is the 
   * array [101, 102, 103], then after calling this method,
   * arr contains [10, 101, 102, 103, 20, 30].  If pos plus the
   * length of toInsert exceeds the length of arr, the extra elements
   * of toInsert are ignored. If pos is out of range, the method
   * does nothing.
   * @param arr
   *   given array to be modified
   * @param pos
   *   starting insert position
   * @param toInsert
   *   elements to be inserted
   */
  public static void insertArray(int[] arr, int pos, int[] toInsert)
  {
	
	// if pos is out of range, kill function
	if (pos < 0 || pos >= arr.length) {
		return;
	}
	// create a new array
	int[] newArr = new int[arr.length];
	
	// iterate through the new array
	for (int i = 0; i < arr.length; i++) {
		if (i < pos) {
			newArr[i] = arr[i];
		}
		else if (i < pos + toInsert.length) {
			newArr[i] = toInsert[i - pos];
		}
		else {
			newArr[i] = arr[i - toInsert.length];
		}
	}
	
	for (int i = 0; i < arr.length; i++) { 
		arr[i] = newArr[i];
	}
	  
	  
  }
  
  
  /**
   * Finds a permutation with the property that if arr 
   * is rearranged according to that permutation, it will
   * end up in ascending order.  That is, for an array arr,
   * after executing the code
   * <pre>
   *    int[] p = findPermutationToSort(arr);
   *    rearrange(arr, p);
   * </pre>
   * the array arr is sorted.  For example, if 
   * arr is [12, 7, 2, 5, 8], the method returns 
   * the array [4, 2, 0, 1, 3].  You can assume that
   * the given array has no duplicates.
   * @param arr
   *   given array
   * @return
   *   permutation that will rearrange arr to be sorted in
   *   ascending order
   */
  public static int[] findPermutationToSort(int[] arr)
  {

	  int lowestIndex = 0;
	  int counter = 0;
	  int lowest = Integer.MAX_VALUE;
	  int highest = Integer.MIN_VALUE;

	  int[] result = new int[arr.length];

	  // find the highest value
	  for (int i = 0; i < arr.length; i++) {
		  if (arr[i] > highest) {
			  highest = arr[i];
		  }
		  // highest is now the highest value
	  }

	  while (counter < arr.length) {
		  for (int i = 0; i < arr.length; i++) {
			  if (arr[i] < lowest) {
				  lowest = arr[i];
				  lowestIndex = i;
			  }
		  }
		  // set the lowest value to 1 higher than the highest
		  arr[lowestIndex] = highest + 1;
		  // set lowest value to counter
		  result[lowestIndex] = counter;
		  // increment counter so the permutation isn't all 0's
		  counter ++;
		  // reset lowest
		  lowest = Integer.MAX_VALUE;
	  }

    return result;
  }
  

  /**
   * NOTE: THIS IS AN EXTRA CREDIT PROBLEM.
   * 
   * Finds the longest arithmetic run in an int array.
   * An <em>arithmetic run</em> is a sequence of consecutive
   * elements with the same difference between them. For example, 
   * in the array [10, 20, 30, 25, 20, 15, 8, 9], all of 
   * the following are arithmetic runs:
   * <ul>
   *   <li>[10, 20, 30] (difference 10)
   *   <li>[30, 25, 20] (difference -5)
   *   <li>[25, 20, 15] (difference -5)
   *   <li>[30, 25, 20, 15] (difference -5)
   * </ul>
   * Note that any adjacent pair of elements is an arithmetic run
   * of length 2. In this case the method would return [30, 25, 20, 15].
   * If there are multiple arithmetic runs of the same length, the 
   * method returns the one with the smallest starting index. For example,
   * for array [12, 6, 2, 5, 9], the method returns [12, 6].
   * For an array of length 0, 1, or 2, the method just returns 
   * the array itself.
   * 
   * @param arr
   *   given array
   * @return
   *   new array containing the longest arithmetic run in the given array
   */
  public static int[] findLongestArithmeticRun(int[] arr)
  {
    // TODO
	
	// if arr is length 0, 1 or 2, return original array
	if (arr.length < 3) {
		return arr;
	}

    return null;
  }


}