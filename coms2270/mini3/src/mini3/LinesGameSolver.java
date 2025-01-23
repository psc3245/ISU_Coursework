package mini3;
import java.util.ArrayList;

import api.Line;
import api.Location;
import api.Solution;
import hw3.LinesGame;

/**
 * Recursive solver for the LinesGame.
 * @author smkautz
 */
public class LinesGameSolver
{

  /**
   * Find all solutions for the given game.
   * @param game
   *   a lines game
   * @return
   *   arraylist of Solution objects, one for each possible
   *   solution of the given game
   */
  public static ArrayList<Solution> doSolve(LinesGame game)
  {
    // to start the solver, we have to make sure the game has a current line
    ArrayList<Line> lines = game.getAllLines();
    Line line = lines.get(0);
    Location loc = line.getEndpoint(0);
    game.startLine(loc.row(), loc.col());  
    
    // then pass the game into the recursive solver
    ArrayList<Solution> solutions = new ArrayList<>();
    findSolutions(game, solutions); 
    return solutions;
  }

  /**
   * Recursively finds all solutions of the given game and populates the arraylist with the solutions.
   * The given game must have non-null current line.
   * @param g
   *   given LinesGame
   * @param solutions
   *   list of solutions
   */
  public static void findSolutions(LinesGame g, ArrayList<Solution> solutions) //, int currIndex)
  {
    // TODO
	  
  }
  
  
  /**
   * Given a row and column pair, returns Locations representing
   * the row and column for each of the eight immediate neighbors
   * of the given position.  Neighbors are returned in counterclockwise
   * order starting with the one directly above. No bounds checking is done.
   * 
   * @param row
   *   given row
   * @param col
   *   given column
   * @return
   *   array of Locations representing the eight neighbors of the given row/column
   */
  private static Location[] getNeighbors(int row, int col)
  {
    Location[] n = new Location[8];
    n[0] = new Location(row - 1, col);
    n[1] = new Location(row - 1, col - 1);
    n[2] = new Location(row, col - 1);
    n[3] = new Location(row + 1, col - 1);
    n[4] = new Location(row + 1, col);
    n[5] = new Location(row + 1, col + 1);
    n[6] = new Location(row, col + 1);
    n[7] = new Location(row - 1, col + 1);
    return n;    
  }
  

  /**
   * Given a game and a line id, determines the index of the line 
   * with that id in the game's line array.
   * @param g
   *   a LinesGame
   * @param id
   *   an integer id for a line
   * @return
   *   index of the given line id in the given game, if it exists; otherwise
   *   returns -1
   */
  private static int getIndexForLine(LinesGame g, int id)
  {
    ArrayList<Line> lines = g.getAllLines();
    for (int i = 0; i < lines.size(); ++i)
    {
      if (lines.get(i).getId() == id)
      {
        return i;
      }
    }
    return -1;
  }
}
