package api;
import static api.CellType.*;

/**
 * Container for a cell type that can be used as part of a 2d grid for a Pacman game.
 */
public class MazeCell 
{
  /**
   * Current cell status.
   */
  private CellType type;
  
  /**
   * For DOT and ENERGIZER types, indicates whether
   * it has been eaten.
   */
  private boolean eaten;
  
  /**
   * Constructs a maze cell with type empty.
   */
  public MazeCell()
  {
    type = EMPTY;
    eaten = false;
  }

  /**
   * Constructs a maze cell with given type
   */
  public MazeCell(CellType givenType)
  {
    type = givenType;
    eaten = false;
  }
  
  /**
   * Returns true if this cell is a wall.
   * @return
   *   true if this cell is a wall
   */
  public boolean isWall()
  {
    return type == WALL;
  }

  /**
   * Returns true if this cell can be eaten.
   * @return
   *   true if cell can be eaten
   */
  public boolean canEat()
  {
    return !eaten && (type == DOT || type == ENERGIZER);
  }
  
  /**
   * Returns the type of this cell.
   * @return
   *   type of this cell
   */
  public CellType getType()
  {
    return type;
  }
  
  /**
   * Sets this cell to eaten if it is of type DOT or ENERGIZER.
   */
  public void eat()
  {
    if (type == DOT || type == ENERGIZER)
    {
      eaten = true;
    }
  }
  
  /**
   * Restores this cell's state to un-eaten.
   */
  public void reset()
  {
    eaten = false;
  }

  
}
