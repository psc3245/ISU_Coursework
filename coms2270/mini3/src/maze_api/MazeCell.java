package maze_api;

/**
 * Container for a cell status that can be used as part of a 2d grid for a maze.
 */
public class MazeCell 
{
  /**
   * Status of this cell.
   */
  private CellStatus status;
  
  /**
   * Indicates whether this cell is considered a wall.  
   */
  private boolean wall;
  
  /**
   * Indicates whether this cell is the goal.
   */
  private boolean goal;
  
  /**
   * Observer to be notified of changes in this cell's status.
   */
  private MazeObserver observer;
  
  /**
   * Constructs a maze cell at the given location with default
   * status NOT_STARTED and no observer.
   */
  public MazeCell()
  {
    status = CellStatus.NOT_STARTED;
    observer = null;
  }

  public boolean isWall()
  {
    return wall;
  }
  
  public boolean isGoal()
  {
    return goal;
  }
  
  public void setIsWall()
  {
    wall = true;
  }
  
  public void setIsGoal()
  {
    goal = true;
  }
  
  /**
   * Sets the status of this cell.
   * @param s
   *   new status for the cell
   */
  public void setStatus(CellStatus s)
  {
    status = s;
    if (observer != null)
    {
      observer.statusChanged(this);
    }
  }

  /**
   * Returns the status of this cell.
   * @return
   *   status of this cell
   */
  public CellStatus getStatus()
  {
    return status;
  }

  /**
   * Set an observer for this cell.  When the 
   * cell's status changes, the observer's statusChanged 
   * method is called.
   * @param givenObserver
   *   an instance of GridObserver
   */
  public void setObserver(MazeObserver givenObserver)
  {
    observer = givenObserver;
  }
  
}
