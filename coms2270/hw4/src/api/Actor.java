package api;

/**
 * Interface representing the behavior of the player and
 * enemies in a Pac-man game.
 * @author smkautz
 */
public interface Actor
{
  /**
   * Returns the base actor speed, in units of cells per frame.
   * @return
   *   base speed
   */
  double getBaseIncrement();

  /**
   * Returns the exact column (x) coordinate of the actor's center 
   * within the maze grid, in units of cells. 
   * @return
   *   exact x-coordinate
   */
  double getColExact();

  /**
   * Returns the current actor speed, in units of cells per frame.
   * In certain modes this may vary from the base speed.
   * @return
   *   current speed
   */
  double getCurrentIncrement();

  /**
   * Returns the cell currently occupied by the actor's center.
   * This value is always equal to the location:
   * <pre>
   * ((int) getRowExact(), (int) getColExact())
   * </pre>
   * @return
   *   current cell
   */
  Location getCurrentLocation();

  /**
   * Returns the actor's current direction.
   * @return
   *   current direction
   */
  Direction getCurrentDirection();

  /**
   * Returns the actor's initial direction, that is, the direction
   * it will have after an invocation of the reset() method. 
   * @return
   *   actor's initial direction
   */
  Direction getHomeDirection();

  /**
   * Returns the actor's initial location, that is, the cell
   * it will occupy after an invocation of the reset() method. 
   * @return
   *   actor's initial location
   */
  Location getHomeLocation();

  /**
   * Returns the actor's current mode.  This value is always null
   * for the player.
   * @return
   *    current mode
   */
  Mode getMode();
  
  /**
   * Returns the exact row (y) coordinate of the actor's center 
   * within the maze grid, in units of cells. 
   * @return
   *   exact y-coordinate
   */
  double getRowExact();

  /**
   * Resets this actor to its initial location, direction, and mode.
   */
  void reset();

  /**
   * Sets the column (x) coordinate of this actor's center within the maze grid,
   * in units of cells.
   * @param c
   *   column coordinate
   */
  void setColExact(double c);
  
  /**
   * Sets the current direction for this actor. 
   * @param dir
   *   new direction to set
   */
  void setDirection(Direction dir);
  
  /**
   * Updates this actor's mode and performs related initialization.
   * For the player, this is a no-op.
   * @param mode
   *   given mode
   * @param desc
   *   current game descriptor
   */
  void setMode(Mode mode, Descriptor desc);
  
  /**
   * Sets the row (y) coordinate of this actor's center within the maze grid,
   * in units of cells.
   * @param r
   *   row coordinate
   */
  void setRowExact(double r);

  /**
   * Updates the actor's position.  This method is typically invoked
   * once per frame.
   * @param desc
   *   current game descriptor  
   */
  void update(Descriptor desc);
}
