package api;

/**
 * Immutable container for a pair of two-dimensional coordinates.
 */
public class Location
{
  /**
   * The row.
   */
  private final int row;
  
  /**
   * The columnn.
   */
  private final int col;
  
  /**
   * Constructs a Location with the given row and column.
   * @param row
   *   given row
   * @param col
   *   given column
   */
  public Location(int row, int col)
  {
    this.row = row;
    this.col = col;
  }
  
  /**
   * Returns the row value.
   * @return
   *   the row
   */
  public int row()
  {
    return row;
  }
  
  /**
   * Returns the column value.
   * @return
   *   the column
   */
  public int col()
  {
    return col;
  }

  /**
   * Determines whether this Location is equal to the given object.
   * @return
   *   true if the given object is a Location with the same row and column
   */
  public boolean equals(Object obj)
  {
    if (obj == null || obj.getClass() != this.getClass())
    {
      return false;
    }
    Location other = (Location) obj;
    return row == other.row && col == other.col;
  }
  
  /**
   * Returns a string representation of this object in the form (row, column).
   */
  public String toString()
  {
    return "(" + row + ", " + col + ")";
  }
}
