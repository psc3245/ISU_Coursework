package api;

/**
 * Provides a limited read-only view of the cell layout in a Pacman game.
 * @author smkautz
 */
public class MazeMap
{
  /**
   * The underlying game instance
   */
  private PacmanGame game;
  
  /**
   * Constructs a MazeMap from the given game.
   * @param game
   *   given game instance
   */
  public MazeMap(PacmanGame game)
  {
    this.game = game;
  }
  
  /**
   * Returns true if the cell at given row and column is a wall.
   * No bounds checking is done.
   * @param row
   *   given row
   * @param col
   *   given column
   * @return
   *   true if the given location is a wall cell
   */
  public boolean isWall(int row, int col)
  {
    return game.getCell(row, col).isWall();
  }
  
  /**
   * Returns the number of rows in the game.
   * @return
   *   number of rows in the game
   */
  public int getNumRows()
  {
    return game.getNumRows();
  }
  
  /**
   * Returns the number of columns in the game.
   * @return
   *   number of columns in the game
   */  
  public int getNumColumns()
  {
    return game.getNumColumns();
  }
}
