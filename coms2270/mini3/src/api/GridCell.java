package api;

import static api.CellType.CROSSING;
import static api.CellType.ENDPOINT;
import static api.CellType.MIDDLE;
import static api.CellType.OPEN;

/**
 * Cell type for a 2d grid representing a Lines game.  Each cell has a type, an id, a count, and
 * a maximum count.  The type is one of the CellType values OPEN, MIDDLE, ENDPOINT, or CROSSING. 
 * For types OPEN, MIDDLE, or ENDPOINT, the max count is always 1, and for CROSSING, the count
 * is specified by the constructor.  For OPEN or CROSSING, the id is always -1, while for 
 * MIDDLE or ENDPOINT, it is specified in the constructor.
 */
public class GridCell
{
  /**
   * Type of this cell.
   */
  private CellType type;

  /**
   * Id for this cell.
   */
  private int id;

  /**
   * Crossing count for this cell.
   */
  private int count = 0;
  
  /**
   * Maximum number of crossings.
   */
  private int maxCount;
  
  /**
   * Copy constructor.
   * @param other
   */
  public GridCell(GridCell other)
  {
    this.type = other.type;
    this.id = other.id;
    this.count = other.count;
    this.maxCount = other.maxCount;
  }
  
  /**
   * Constructs an OPEN cell. The max count is 1 and the id is -1.
   */
  public GridCell()
  {
    type = OPEN;
    maxCount = 1;
    id = -1;
  }
  
  /**
   * Constructs an ENDPOINT or MIDDLE cell. The max count is 1 , and 
   * initially the count is zero.
   * @param type
   *   ENDPOINT or MIDDLE
   * @param id
   *   a nonnegative integer
   */
  public GridCell(CellType type, int id)
  {
    this.type = type;
    maxCount = 1;
    if (type == MIDDLE || type == ENDPOINT)
    {
      this.id = id;
    }
    else
    {
      this.id = -1;
    }
  }
  
  /**
   * Constructs a CROSSING cell with the given max count.  Initially the 
   * count is zero. The id is -1.
   * @param maxCount
   *   the max count for this cell
   */
  public GridCell(int maxCount)
  {
    type = CROSSING;
    id = -1;
    this.maxCount = maxCount;
  }
  
  /**
   * Returns true if this cell is of type ENDPOINT, false otherwise.
   * @return
   *   true if the cell is an endpoint
   */
  public boolean isEndpoint()
  {
    return type == ENDPOINT;
  }
  
  /**
   * Returns true if this cell is of type CROSSING, false otherwise.
   * @return
   *   true if the cell is a crossing
   */
  public boolean isCrossing()
  {
    return type == CROSSING;
  }
  
  /**
   * Returns true if this cell is of type OPEN, false otherwise.
   * @return
   *   true if the cell is open
   */
  public boolean isOpen()
  {
    return type == OPEN;
  } 
  
  /**
   * Returns true if this cell is of type MIDDLE, false otherwise.
   * @return
   *   true if the cell is a middle cell
   */
  public boolean isMiddle()
  {
    return type == MIDDLE;
  }  
  
  /**
   * Returns the id for this cell.
   * @return
   *   id for this cell
   */
  public int getId()
  {
    return id;
  }
  
  /**
   * Returns true if the given id matches this cell's id.  Always returns
   * true if this is an open cell or crossing.
   * 
   * @param givenId
   *   the id to check
   * @return
   *   true if this cell is open or a crossing or if the id matches
   */
  public boolean idMatches(int givenId)
  {
    return type == OPEN || type == CROSSING || id == givenId;
  }
  
  /**
   * Returns the current count for this cell.
   * @return
   *   count for this cell
   */
  public int getCount()
  {
    return count;
  }
  
  /**
   * Returns the max count for this cell.
   * @return
   *   max count for this cell
   */
  public int getMaxCount()
  {
    return maxCount;
  }
  
  /**
   * Returns true if this cell's count has reached its maximum.
   * @return
   *   true if this cell's count has reached its maximum
   */
  public boolean maxedOut()
  {
    return count == maxCount;
  }
  
  /**
   * Increments the count for this cell.
   * @throws IllegalStateException
   *   if the count is already equal to the max
   */
  public void increment()
  {
    if (count == maxCount)
    {
      throw new IllegalStateException("Attempt to increment cell count above max. ");
    }
    count += 1;
  }
  
  /**
   * Decrements the count for this cell.
   * @throws IllegalStateException
   *   if the count is already zero
   */
  public void decrement()
  {
    if (count == 0)
    {
      throw new IllegalStateException("Attempt to reduce cell count below zero. ");
    }
    count -= 1;
  }

  
  /**
   * Determines whether this cell is equal to the given object
   * @param obj
   *   an object
   * @return 
   *   true if the given object is a GridCell that is the same as this one
   */
  public boolean equals(Object obj)
  {
    if (obj == null || obj.getClass() != this.getClass())
    {
      return false;
    }
    GridCell other = (GridCell) obj;
    return this.type == other.type && 
        this.count == other.count && 
        this.id == other.id && 
        this.maxCount == other.maxCount;
  }
  
  /**
   * Returns a String representation of this object in the form,
   * <p>
   * TYPE (id) count/maxCount
   * @return
   *   String representation of this object
   */
  public String toString()
  {
    return type + " (" + id + ") " + count + "/" + maxCount;
  }
  
}
