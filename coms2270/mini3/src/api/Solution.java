package api;
import java.util.ArrayList;

import api.GridCell;
import api.Line;
import api.Location;
import api.StringUtil;
import hw3.LinesGame;

/**
 * The Solution class is just a container for a LinesGame
 * whose constructor makes a deep copy of a given game.
 * @author smkautz
 */
public class Solution
{
  /**
   * The encapsulated game.
   */
  private LinesGame game;
  
  /**
   * Constructs a Solution containing a deep copy of the
   * given game.
   * @param givenGame
   *   a LinesGame instance
   */
  public Solution(LinesGame givenGame)
  {
    GridCell[][] grid = new GridCell[givenGame.getHeight()][givenGame.getWidth()];
    for (int row = 0; row < givenGame.getHeight(); ++row)
    {
      for (int col = 0; col < givenGame.getWidth(); ++col)
      {
        grid[row][col] = new GridCell(givenGame.getCell(row, col));
      }
    }
    ArrayList<Line> lines = new ArrayList<>();
    for (Line line : givenGame.getAllLines())
    {
      Line copy = new Line(line.getId(), line.getEndpoint(0), line.getEndpoint(1));
      for (Location loc : line.getCells())
      {
        copy.add(loc);  // no need to clone since Location is immutable
      }
      lines.add(copy);
    }
    
    game = new LinesGame(grid, lines);
  }
  
  /**
   * Returns a reference to the contained game.
   * @return
   *   the LinesGame instance
   */
  public LinesGame game()
  {
    return game;
  }

  /**
   * Returns a string representation of this solution.
   */
  @Override
  public String toString()
  {
    return game.toString();
  }
}
