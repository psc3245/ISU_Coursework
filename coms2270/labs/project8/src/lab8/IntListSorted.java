package lab8;

/**
 * Subclass of IntList that guarantees that the elements are always
 * in ascending order.
 */
public class IntListSorted extends IntList
{
  /**
   * Constructs an empty list.
   */
  public IntListSorted()
  {
    super();
  }
  
  /**
   * Adds a new item to this list, inserting it so that
   * the list remains sorted.
   */
  @Override
  public void add(int newItem)
  {
    if (size() == 0 || get(size() - 1) <= newItem)
    {
      super.add(newItem);
    }
    else
    {
      int i = size();
      while (i > 0 && newItem < get(i - 1))
      {
        i -= 1;
      }
      
      // now i is 0, or newItem >= list[i - 1], so put the new
      // element at position i
      super.insert(i, newItem);
    }
  }
  
  /**
   * Inserts a new item in this list, inserting it so that
   * the list remains sorted.  (The given index is ignored.)
   */
  @Override
  public void insert(int index, int newItem)
  {
    this.add(newItem);
  }
  
  public int getMedian() {
	  
	  /*
	   * Note: When the list contains an even number of items, just return the last element 
	   * in the first half of the list, 
	   * i.e., the element right before the "center" of the list. For example, 
	   * in the case of [0, 1, 3, 10], 1 should be returned.
	   */
	  
	  // size = 9
	  // 8 / 2 = 4
	  // 
	  
	  // even number of items
	  if (size() % 2 == 0) {
		  return get(size() / 2 - 1);
	  }
	  
	  else {
		  return get(size() / 2);
	  }

  }
}







