package hw3;

import java.util.ArrayList;

import api.GridCell;
import api.Line;
import api.Location;
import api.StringUtil;

/**
 * Game state for a Lines game.
 */
public class LinesGame
{
  /**
   *  The current line, possibly null.
   */
  private Line currentLine;
  
  /**
   * All lines in this game.  NOTE: indexing of list is not necessarily
   * in order of id.
   */
  private ArrayList<Line> lines;
  
  /**
   * Grid of cells for this game.
   */
  private GridCell[][] grid;
  
  /**
   * Number of successful addCell operations.
   */
  private int moveCount;
  
  /**
   * Move history.
   */
  private ArrayList<MoveRecord> undoList = new ArrayList<>();

  
  /**
   * Constructs a LinesGame from the given grid and Line list.
   * This constructor does not do any error-checking to ensure
   * that the grid and the Line array are consistent. Initially
   * the current line is null.
   * @param givenGrid
   *   a 2d array of GridCell
   * @param givenLines
   *   list of Line objects
   */
  public LinesGame(GridCell[][] givenGrid, ArrayList<Line> givenLines)
  {
    lines = givenLines;
    grid = givenGrid;
  }
  
  /**
   * Constructs a LinesGame from the given descriptor. Initially the
   * current line is null.
   * @param descriptor
   *   array of strings representing initial state
   */
  public LinesGame(String[] descriptor)
  {
    grid = StringUtil.createGridFromStringArray(descriptor);
    lines = Util.createLinesFromGrid(grid);
    currentLine = null; 
  }
  
  /**
   * Returns the number of columns for this game.
   * @return
   *  width for this game
   */ 
  public int getWidth()
  {
    return grid[0].length;
  }
  
  /**
   * Returns the number of rows for this game.
   * @return
   *   height for this game
   */ 
  public int getHeight()
  {
    return grid.length;
  }
  
  /**
   * Returns the current cell for this game, possibly null.
   * The current cell is just the last location, if any, 
   * in the current line, if there is one. Returns null
   * if the current line is null or if the current line
   * has an empty list of locations.
   * @return
   *   current cell for this game, or null
   *   
   */
  public Location getCurrentLocation()
  {
    if (currentLine == null)
    {
      return null;
    }
    return currentLine.getLast();
  }
  
  /**
   * Returns the id for the current line, or -1
   * if the current line is null.
   * @return
   *   id for the current line
   */
  public int getCurrentId()
  {
    if (currentLine == null)
    {
      return -1;
    }
    return currentLine.getId();
  }
  
  /**
   * Return this game's current line (which may be null).
   * @return
   *   current line for this game
   */
  public Line getCurrentLine()
  {
    return currentLine;
  }
  
  /**
   * Returns a reference to this game's grid.  Clients should
   * not modify the array.
   * @return
   *   the game grid
   */
  public GridCell[][] getGrid()
  {
    return grid;
  }
  
  /**
   * Returns the grid cell at the given position.
   * @param row
   *   given row
   * @param col
   *   given column
   * @return
   *   grid cell at (row, col)
   */
  public GridCell getCell(int row, int col)
  {
    return grid[row][col];
  }
  
  /**
   * Returns all Lines for this game.  Clients should not modify
   * the returned list or the Line objects.
   * @return
   *   list of lines for this game
   */ 
  public ArrayList<Line> getAllLines()
  {
    return lines;
  }
  
  /**
   * Returns the total number of moves.  A "move" means that a 
   * new Location was successfully added to the current line
   * in addCell.
   * @return
   *   total number of moves so far in this game
   */
  public int getMoveCount()
  {
    return moveCount;
  }
  
  /**
   * Returns true if all lines are connected and all
   * cells are at their maximum count.
   * @return
   *   true if all lines are complete and all cells are at max
   */ 
  public boolean isComplete()
  {
    // all lines connected?
    for (Line f : lines)
    {
      if (!f.isConnected())
      {
        return false;
      }
    }   
    
    // all cells at max?
    for (int row = 0; row < getHeight(); ++row)
    {
      for (int col = 0; col < getWidth(); ++col)
      {
        GridCell gc = grid[row][col];
        if (!gc.maxedOut())
        {
          return false;
        }
      }
    }
    
    return true;
    
  }
  
  /**
   * Attempts to set the current line based on the given
   * row and column.  When using a GUI, this method is typically 
   * invoked when the mouse is pressed. If the current line is 
   * already non-null, this method does nothing.
   * There are two possibilities:
   * <ul>
   *   <li>Any endpoint can be selected.  Selecting an 
   *   endpoint clears the line associated with that endpoint's id,
   *   and all cells that were previously included in the line are decremented.
   *   The line then becomes the current line, and the endpoint is incremented
   *   and placed on the line's list of locations as its only element.
   *   <li>A non-endpoint cell can be selected if it is not a crossing
   *   and if it is the last cell in some line.  That line then becomes
   *   the current line.
   * </ul>
   * If neither of the above conditions is met, or if the
   * current line is non-null, this method does nothing.
   * 
   * @param row
   *   given row
   * @param col
   *   given column
   */
  public void startLine(int row, int col)
  {
    if (currentLine != null)
    {
      return;
    }
    
    GridCell gc = grid[row][col];
    if (gc.isEndpoint())
    {
      // reduce count at all grid cells along the line, and clear the line
      Line f = getLineForId(gc.getId());
      ArrayList<Location> locs = f.getCells();
      for (Location loc : locs)
      {
        GridCell c = grid[loc.row()][loc.col()];
        c.decrement();
      }
      
      // clear the line
      f.clear();
      
      // add this cell to the line, and make it the current line
      f.add(new Location(row, col));
      gc.increment();     
      currentLine = f;
      
      // add to move history (added for mini3)
      undoList.add(new MoveRecord(currentLine, new Location(row, col)));
    } 

    // not an endpoint,
    // if it is an occupied cell other than a crossing, we can allow
    // it to be a starting cell only if it is the last cell in some line
    else if (!gc.isCrossing())
    {
      Location here = new Location(row, col);
      for (Line f : lines)
      {
        Location loc = f.getLast();
        if (loc != null && loc.equals(here))
        {
          currentLine = f;
          break;
        }
      }
    }
      
    // otherwise do nothing
  }
  
  /**
   * Sets the current line to null. When using a GUI, this method is 
   * typically invoked when the mouse is released.
   */
  public void endLine()
  {
    currentLine = null;
  }
  
  /**
   * Attempts to add a new cell to the current line.  
   * When using a GUI, this method is typically invoked when the mouse is 
   * dragged.  In order to add a cell, the following conditions must be satisfied.
   * Here the "current cell" is the last cell in the current line, and "new cell"
   * is the cell at the given row and column:
   * :
   * <ol>
   *   <li>The current line is non-null
   *   <li>The current line is not connected
   *   <li>The given row and column are adjacent to the location of the current cell
   *       (horizontally, vertically, or diagonally) and not the same as the current cell
   *   <li>The count for the new cell is less than its max count
   *   <li>If the new cell is a MIDDLE or ENDPOINT, then its id matches
   *   the id for the current line
   *   <li>Adding the new cell will not cause the line to re-trace any
   *   existing line (according to the result of Util.checkForLineSegment)
   *   <li>Adding the new cell to the line would not cross any existing line
   *   (according to the result of Util.checkForPotentialCrossing)
   * </ol>
   * If the above conditions are met, a new Location at (row, col) is added
   * to the current line and the cell count is incremented.  Otherwise, the 
   * method does nothing.  If a new location
   * is added to the current line, the move counter is increased by 1.
   * @param row
   *   given row for the new cell
   * @param col
   *   given column for the new cell
   */
  public void addCell(int row, int col)
  {    
    // bounds check (added for mini3)
    if (row < 0 || col < 0 || row >= getHeight() || col >= getWidth())
    {
      return;
    }
    
    // is there a current line, and if so, is it connected?
    if (currentLine == null || currentLine.isConnected())
    {
      return;
    }
    
    
    // is it adjacent?
    Location current = getCurrentLocation();
    int c = Math.abs(col - current.col());
    int r = Math.abs(row - current.row());
    if (!((r == 1 && c == 0) || (r == 0 && c == 1) || (r == 1 && c == 1)))
    {
      return;
    }
    
    // would it cause a crossing or retrace an existing line?
    if (Util.checkForPotentialCrossing(lines, current, new Location(row, col)) ||
        Util.checkForLineSegment(lines, current, new Location(row, col)))
    {
      return;
    }  
    
    // check the count and id
    GridCell newCell = grid[row][col];
    int id = getCurrentId();
    if (!newCell.maxedOut() && newCell.idMatches(id))
    {
      newCell.increment();
      currentLine.add(new Location(row, col));
      moveCount += 1;
      
      // add to move history (added for mini3)
      undoList.add(new MoveRecord(currentLine, new Location(row, col)));
    }

    // otherwise, do nothing
  }
  
  /**
   * Undo the most recent move.  NOTE: this operation is designed to work
   * correctly when startLine is *only* called on an endpoint of an empty line.
   */
  // (added for mini3)
  public void undoMove()
  {
    if (currentLine == null)
    {
      throw new IllegalStateException("Current line is null in undoMove");
    }   
    
    MoveRecord r = undoList.remove(undoList.size() - 1);
    
    if (currentLine.getId() != r.line().getId())
    {
      throw new IllegalStateException("Current line id " + currentLine.getId() + 
          " does not match undo list id " + r.line().getId());      
    }
    
    ArrayList<Location> cells = r.line().getCells();  
    cells.remove(cells.size() - 1);
    grid[r.loc().row()][r.loc().col()].decrement();
    
    if (cells.size() == 0) 
    {
      // removed first endpoint, so revert current to previous line, if any
      if (undoList.size() > 0)
      {
        MoveRecord prev = undoList.get(undoList.size() - 1);
        currentLine = prev.line();
      }
      else
      {
        currentLine = null;
      }
    }
  }
  
  
  /**
   * Helper method returns the Line with the given id, or
   * null if there is no such line.
   * @param id
   *   a potential Line id
   * @return
   *   line matching the given id, or null if there isn't one
   */
  private Line getLineForId(int id)
  {
    for (Line line : lines)
    {
      if (line.getId() == id)
      {
        return line;
      }
    }
    return null;
  }
  
  /**
   * Returns a string representation of this game.
   */
  public String toString()
  {
    String result = "";
    result += "-----\n";
    result += StringUtil.originalGridToString(grid);
    result += "-----\n";
    result += StringUtil.currentGridToString(grid, lines);
    result += "-----\n";
    result += StringUtil.allLinesToString(lines);
    Line ln = getCurrentLine();
    if (ln != null)
    {
      result += "Current line: " + ln.getId() + "\n";
    }
    else
    {
      result += "Current line: null\n";
    }
    return result;
  }

  /**
   * Inner class for recording move history.
   * @author smkautz
   */
  private static class MoveRecord
  {
    private final Line line;
    private final Location loc;
    public MoveRecord(Line line, Location loc)
    {
      this.line = line;
      this.loc = loc;
    }
    public Line line()
    {
      return line;
    }
    public Location loc()
    {
      return loc;
    }
  }
}
