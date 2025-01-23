package api;

import java.util.ArrayList;

/**
 * Utility class with methods for constructing a LinesGame grid from a string
 * descriptor, and for representing the game state in string form.
 */
public class StringUtil
{
  /**
   * Single-letter identifiers to represent different ids in
   * a game descriptor.  These letters are chosen to be mnemonic
   * for the colors listed in the parallel array GamePanel.COLORS.
   */
  public static final char[] COLOR_CODES = {
    'R',
    'G',
    'B',
    'C',
    'Y',
    'M',
    'O',
    'P',
    'S',
    'V',
    'F'
  };
  
  /**
   * Get the integer id value associated with the given character, that is, its
   * index in the COLOR_CODES array. Returns -1 if the character does not
   * appear in the COLOR_CODES array when converted to upper case.
   * @param ch
   *   given character
   * @return
   *   index of the character (converted to upper case) in the COLOR_CODES
   *   array, or -1 if it does not occur
   */
  public static int getIdForCharacter(char ch)
  {
    ch = ("" + ch).toUpperCase().charAt(0);
    for (int i = 0; i < COLOR_CODES.length; ++i)
    {
      if (ch == COLOR_CODES[i])
      {
        return i;
      }
    }
    return -1;  // not found
  }

  /**
   * Given a 2d array of GridCell, returns a string representation of the 
   * original state.  That is, all MIDDLE and ENDPOINT cells are shown
   * with the character form of their ids, all OPEN cells are shown as a '-', 
   * and all CROSSING cells are shown as their max counts.
   * @param grid
   *   2d array of GridCell
   * @return
   *   String representation of the grid in its original state
   */
  public static String originalGridToString(GridCell[][] grid)
  {
    return gridToString(grid, true, null);
  }
  
  /**
   * Given a 2d array of GridCell, returns a string representation of the 
   * current state.  That is, all MIDDLE and ENDPOINT cells are shown
   * with the character form of their ids only if they have count 1, 
   * and otherwise are shown as a '.' character.  The OPEN cells with 
   * count 1 are shown as an 'x' character and those with count 0
   * are shown as a '.' character. All CROSSING cells are shown 
   * as their current count.
   * 
   * @param grid
   *   2d array of GridCell
   * @return
   *   String representation of the grid in its current state
   */
  public static String currentGridToString(GridCell[][] grid)
  {
    return gridToString(grid, false, null);    
  }
  
  /**
   * Given a 2d array of GridCell, returns a string representation of the 
   * current state, possibly including ids for lines crossing open cells.
   * That is, all MIDDLE and ENDPOINT cells are shown
   * with the character form of their ids only if they have count 1, 
   * and otherwise are shown as a '.' character.   If the 
   * <code>lines</code> parameter is non-null and is consistent with
   * the grid, the OPEN cells are with count 1 are shown with the id
   * of the line that crosses them; otherwise, they are shown as an 'x' 
   * character. All CROSSING cells are shown as their current count.
   * 
   * @param grid
   *   2d array of GridCell
   * @param lines
   *   list of lines that is consistent with the grid, possibly null
   * @return
   *   String representation of the grid in its current state
   */
  public static String currentGridToString(GridCell[][] grid, ArrayList<Line> lines)
  {
    return gridToString(grid, false, lines);    
  }
  
  /**
   * Helper method to implement the XXXtoString methods.
   * @param grid
   *   2d grid of GridCell
   * @param original
   *   true if the original state of the grid should be represented, false
   *   if the current state should be represented
   * @param lines
   *   the Lines array associated with the grid, possibly null
   * @return
   *   String representation of the grid
   */
  private static String gridToString(GridCell[][] grid, boolean original, ArrayList<Line> lines)
  {
    int rows = grid.length;
    int cols = grid[0].length;
    String result = "";
    for (int row = 0; row < rows; ++row)
    {
      String rowString = "";
      boolean firstChar = true;
      for (int col = 0; col < cols; ++col)
      {
        if (!firstChar)
        {
          rowString += ' ';
        }
        else
        {
          firstChar = false;
        }
        GridCell gc = grid[row][col];
        int id = gc.getId();
        if (gc.isCrossing())
        {
          rowString += "" + (original ? gc.getMaxCount() : gc.getCount());
        }
        else if (gc.isOpen())
        {
          if (original)
          {
            rowString += '-';
          }
          else
          {
            int actualId = id;
            if (lines != null)
            {
              actualId = findIdForOpenCell(row, col, lines);
            }            
            if (actualId < 0)
            {
              rowString += (gc.getCount() == 0 ? '.' : 'x');
            }
            else
            {
              rowString += COLOR_CODES[actualId];          
            }
          }
        }
        else
        {
          // not a crossing or open cell, so should have a valid id
          char c = COLOR_CODES[id];
          if (original || gc.getCount() > 0)
          {
            if (gc.isEndpoint())
            {
              rowString += c;
            }
            else
            {
              rowString += ("" + c).toLowerCase();
            }
          }
          else
          {
            rowString += '.';
          }
        }
      }
      result += rowString + "\n";
          
      //System.out.println(rowString);
    }
    return result;
  }
  
  /**
   * Returns a string representation of the given list of lines, using the 
   * conventions of the Line toString method.
   * @param lines
   *   list of Line objects
   * @return
   *   a multiline string representing the given lines
   */
  public static String allLinesToString(ArrayList<Line> lines)
  {
    String result = "";
    for (Line line : lines)
    {
      result += line.toString() + "\n";
    }
    return result;     
  }
  
  /**
   * Given a row and column and list of lines, determines the id of the line
   * that that includes that (row, col).  If the location (row, col) occurs in 
   * multiple lines (i.e. it is a crossing in the game) then the result
   * is indeterminate.
   * @param row
   *   given row
   * @param col
   *   given col
   * @param lines
   *   list of Line objects
   * @return
   *   id of the line containing the given location
   */
  public static int findIdForOpenCell(int row, int col, ArrayList<Line> lines)
  {
    Location temp = new Location(row, col);
    for (Line f : lines)
    {
      if (f.getCells().contains(temp))
      {
        return f.getId();
      }
    }
    return -1;
  }
  
  /**
   * Creates a 2d array of GridCell from an array of strings.  The length 
   * of the string array is the number of rows in the 2d array, and the 
   * number of rows is the length of the strings.  (If the
   * given strings are not all the same length, the method returns null.)
   * <p>
   * The encoding is as follows:
   * <ul>
   *   <li>A capital letter is an endpoint cell.  Allowable characters are the
   *   eleven characters appearing in the array StringUtil.COLOR_CODES. The
   *   id for the cell is the index of the character in that array.
   *   <li>A lowercase letter is a middle cell.  Allowable characters are the
   *   lowercase versions of the eleven characters appearing in the array 
   *   StringUtil.COLOR_CODES. The id for the cell is the index of the character,
   *   converted to uppercase, in that array.
   *   <li>A dash is an open cell.
   *   <li>A digit 0 through 9 is a crossing cell, and the numeric value
   *   of the digit is its max count.
   * </ul>
   * If any other character appears in one of the strings, the method returns
   * null.
   * @param descriptor
   *   array of strings representing the initial state of a LinesGame, as described above
   * @return
   *   a 2d array of GridCell objects appropriate for initializing a LinesGame
   */
  public static GridCell[][] createGridFromStringArray(String[] descriptor)
  {
    // width and height that we expect
    int width = descriptor[0].length();
    int height = descriptor.length;
    GridCell[][] grid = new GridCell[height][width];

    for (int row = 0; row < descriptor.length; ++row)
    {
      if (descriptor[row].length() != width)
      {
        // strings are not the same length, descriptor is invalid
        return null;
      }
      for (int col = 0; col < descriptor[row].length(); ++col)
      {
        char ch = descriptor[row].charAt(col);
        if (Character.isAlphabetic(ch))
        {
          int id = StringUtil.getIdForCharacter(ch);
          if (id < 0)
          {
            // invalid character in descriptor
            //System.out.println("Invalid letter in descriptor: " + ch);
            return null;
          }
          if (ch >= 'a' && ch <= 'z')
          {
            grid[row][col] = new GridCell(CellType.MIDDLE, id);
          }
          else if (ch >= 'A' && ch <= 'Z')
          {
            // endpoint has same id
            grid[row][col] = new GridCell(CellType.ENDPOINT, id);
          }
        }
        else if (Character.isDigit(ch))  // works for 0 - 9 only...
        {
          // endpoint
          int maxCount = Integer.parseInt("" + ch);
          grid[row][col] = new GridCell(maxCount);
        }
        else if (ch == '-')
        {
          // open cell
          grid[row][col] = new GridCell();
        }
        else
        {
          //System.out.println("Invalid character in descriptor: " + ch);
          
          return null;
        }
      }
    }
    
    return grid;
  }



}
