package lab8;

/**
 * A growable collection of ints based on a partially filled array.  
 * An IntList has a backing array containing the actual values, along 
 * with a variable 'size' indicating how many values are currently stored.
 * The initial capacity of the backing array is 10, and it doubles in size
 * whenever the capacity needs to be increased to add another element. 
 */
public class IntList {

   /**
    * The list of integers. It may contain more elements than are actually
    * filled.
    */
   private int[] list;
   
   /**
    * The actual number of elements.
    */
   private int size;
   
   /**
    * Create a new, empty list of integers.  We create spots for 10 numbers,
    * and we'll grow the array if these all fill up.
    */
   public IntList() {
      list = new int[10];
      size = 0;
   }
   
   /**
    * Get the number of items in the list.
    * @return number of items in list.
    */
   public int size() {
      return size;
   }
   
   /**
    * Get the item at position i
    *
    * @param i
    * Index of item to retrieve.
    *
    * @return
    * Item at position i
    *
    * @throws ArrayIndexOutOfBoundsException
    * If position invalid.
    */
   public int get(int i) {
      if (i >= size) {
         throw new ArrayIndexOutOfBoundsException();
      }
      return list[i];
   }
   
   /**
    * Add an int to the end of the list. The list is grown automatically if
    * its current capacity has been reached.
    *
    * @param newItem
    * Int to add.
    */
   public void add(int newItem) {
      if (size == list.length) {
         grow();
      }
      list[size] = newItem;
      ++size;
   }
   
   /**
    * Inserts an int in the the list at the specified index. Existing elements are
    * shifted to the right. The list is grown automatically if its current 
    * capacity has been reached.
    *
    * @param index
    *   index for the new item
    * @param newItem
    *   item to be inserted into the list
    * Int to add.
    * 
    * @throws ArrayIndexOutOfBoundsException
    * if given index is greater than the current size
    */
   public void insert(int index, int newItem) {
     if (index > size) {
       throw new ArrayIndexOutOfBoundsException();
     }
     if (size == list.length) {
       grow();
     }
     
     // shift elements to the right
     for (int i = size; i > index; i -= 1) {
       list[i] = list[i - 1];
     }
     list[index] = newItem;
     ++size;
   }
   
   /**
    * Double the capacity of the array. Automatically creates a new
    * array twice the size of the current and copies all existing
    * list data over.
    */
   private void grow() {
      int[] tmpList = new int[list.length * 2];
      for (int i = 0; i < size; ++i) {
         tmpList[i] = list[i];
      }
      list = tmpList;
   }
   
   /**
    * Find the maximum int in the list.
    *
    * @return
    * Maximum value in list.
    */
   public int getMaximum() {
      int max = list[0];
      for (int i = 1; i < size; ++i) {
         if (list[i] > max) {
            max = list[i];
         }
      }
      return max;
   }
   
   /**
    * Find the minimum int in the list.
    *
    * @return
    * Minimum value in list.
    */
   public int getMinimum() {
      int min = list[0];
      for (int i = 1; i < size; ++i) {
         if (list[i] < min) {
            min = list[i];
         }
      }
      return min;
   }
   
   /**
    * Get a string representation of the list of the form
    * [val0, val1, val2, ...].
    *
    * @return
    * Comma-separated string of elements.
    */
   public String toString() {
      String s = "[";
      if (size > 0) {
         s += list[0];
         for (int i = 1; i < size; ++i) {
            s += ", " + list[i];
         }
      }
      s += "]";
      return s;
   }
}