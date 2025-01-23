package maze_api;

/**
 * Two-dimensional maze consisting of cells arranged in a grid.
 */
public class TwoDMaze
{
  /**
   * The cells for this maze.
   */
  private MazeCell[][] cells;
  
  /**
   * Starting row.
   */
   private int startRow;
   
   /**
    * Starting column.
    */
   private int startCol;

  /**
   * Constructs a maze based on a 2D grid.  The given strings
   * represent rows of the maze, where '#' represents a wall,
   * a blank represents a possible path, 'S' represents the 
   * starting cell, and '$' represents the goal.
   * @param rows
   *   array of strings, one per row of the maze
   */
  public TwoDMaze(String[] rows)
  {
    int width = rows[0].length();
    int height = rows.length;
    cells = new MazeCell[height][width];
    for (int row = 0; row < height; ++row)
    {
      String s = rows[row];
      for (int col = 0; col < width; ++col)
      {
        MazeCell current = new MazeCell();
        char c = s.charAt(col);
        if (c == '#')
        {
          current.setIsWall();
        }
        else if (c == '$')
        {
          current.setIsGoal();
        }
        else if (c == 'S')
        {
          current.setStatus(CellStatus.NOT_STARTED);
          startRow = row;
          startCol = col;
        }
        else 
        {
          current.setStatus(CellStatus.NOT_STARTED);
        }  
        cells[row][col] = current;
      }
    }
  }

  /**
   * Returns the cell at the given position.
   * @param row
   * @param col
   * @return
   *   cell at the given position
   */
  public MazeCell getCell(int row, int col)
  {
    return cells[row][col];
  }
  
  /**
   * Returns the number of rows in the grid for this maze.
   * @return
   *   number of rows in the grid
   */
  public int getNumRows()
  {
    return cells.length;
  }
  
  /**
   * Returns the number of columns in the grid for this maze.
   * @return
   *   number of columns in the grid
   */
  public int getNumColumns()
  {
    return cells[0].length;
  }
  
  /**
   * Returns row of the starting cell for this maze.
   * @return
   *   starting row
   */
  public int getStartRow()
  {
    return startRow;
  }
  
  /**
   * Returns column of the starting cell for this maze.
   * @return
   *   starting column
   */
  public int getStartColumn()
  {
    return startCol;
  }
  
  /**
   * Set an observer for all cells in this maze.  When a 
   * cell's status changes, the observer's statusChanged 
   * method is called.
   * @param givenObserver
   *   an instance of MazeObserver
   */
  public void setObserver(MazeObserver givenObserver)
  {
    for (int row = 0; row < cells.length; row += 1)
    {
      for (int col = 0; col < cells[0].length; col += 1)
      {
        cells[row][col].setObserver(givenObserver);
      }
    }
  }
}
