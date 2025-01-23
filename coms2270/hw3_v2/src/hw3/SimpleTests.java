package hw3;

import java.util.ArrayList;

import api.GridCell;
import api.Line;
import api.Location;
import api.StringUtil;
import hw3.LinesGame;

public class SimpleTests
{
  public static void main(String[] args)
  {
 
    String[] test = {
      "GRrR",
      "ggGY",
      "Yyyy"
    };
    
    // The StringUtil class can make a grid from the string descriptor above
    GridCell[][] grid = StringUtil.createGridFromStringArray(test);

    // In order to make a game, we need to provide a list of Line objects 
    // containing the locations of the endpoints in the grid. This has to be 
    // consistent with grid above. Normally we would do this by calling
    // the method Util.createLinesFromGrid, but we can also do it by hand.   
    // In order to do this by hand, we have to look 
    // up in StringUtil.COLOR_CODES that 'G' has index 1, 'R' has index 0, and 
    // 'Y' has index 4.  Those indices are the ids for each line. (Note 
    // that these ids are unrelated to the index in the lines list.)
    // And be super careful about the row/column indices for the endpoints.
    ArrayList<Line> lines = new ArrayList<>();
    
    // endpoints for 'G' which corresponds to id 1
    lines.add(new Line(1, new Location(0, 0), new Location(1, 2)));

    // endpoints for 'R' which corresponds to id 0
    lines.add(new Line(0, new Location(0, 1), new Location(0, 3))); 

    // endpoints for 'Y' which corresponds to id 4
    lines.add(new Line(4, new Location(2, 0), new Location(1, 3))); 

    
    // We can print out the grid and the lines to check...
    System.out.println(StringUtil.originalGridToString(grid));
    System.out.println();
    System.out.println(StringUtil.allLinesToString(lines));
    System.out.println();
    
    // If they seem right, we can create a game...
    LinesGame game = new LinesGame(grid, lines);
    
    // Verify that the constructor and the two simple accessors work
    System.out.println(grid == game.getGrid());      // expected true
    System.out.println(lines == game.getAllLines()); // expected true
    
    // And we can always check the original state and current state.
    // The current state shows just the lines that have been drawn,
    // so far, which at this point is none
    System.out.println(StringUtil.originalGridToString(game.getGrid()));
    System.out.println(StringUtil.currentGridToString(game.getGrid()));
    System.out.println(StringUtil.allLinesToString(game.getAllLines()));
    
    // or, we can accomplish the above by invoking toString on the game 
    System.out.println(game);
    
    // try a couple of moves
    game.startLine(1, 3); // (1, 3) is an endpoint for id 4 (Y)  
    
    // now, the game's current line should be the one with id 4
    // and it should have location (1, 3) in its list
    // Expected:
    // id 4(Y): {(2, 0), (1, 3)} [(1, 3)]
    Line line = game.getCurrentLine();
    System.out.println(line);
   
    // This should also increment the count for the cell at (1, 3) 
    // from 0 to 1
    GridCell gc = game.getCell(1, 3);
    System.out.println(gc.getCount()); // expected 1
    
    // and when we look at the current grid, it should show
    // cell (1, 3) as occupied
    // Expected:
    //    . . . .
    //    . . . Y
    //    . . . .
    System.out.println(StringUtil.currentGridToString(game.getGrid()));

    // do a couple more moves to adjacent, matching cells
    game.addCell(2, 3);
    game.addCell(2, 2);   
    System.out.println(game);
    
    //  Expected:
    //  -----
    //  G R r R
    //  g g G Y
    //  Y y y y
    //  -----
    //  . . . .
    //  . . . Y
    //  . . y y
    //  -----
    //  id 1(G): {(0, 0), (1, 2)} []
    //  id 0(R): {(0, 1), (0, 3)} []
    //  id 4(Y): {(2, 0), (1, 3)} [(1, 3), (2, 3), (2, 2)]
    
    
  }
}
