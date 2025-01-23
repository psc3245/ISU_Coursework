package hw4;
import api.PacmanGame;
import hw4.Blinky;
import static api.Mode.*;
/**
 * More ideas for testing the calculateNextCell() method.
 */
public class SimpleTest3
{
  // one ghost
  public static final String[] SIMPLE3 = {
    "######",
    "#....#",
    "#.B..#",
    "#....#",
    "#S...#",
    "######",     
  };
  
  public static void main(String[] args)
  {
  // using a frame rate of 10, the speed increment will be 0.4
  PacmanGame game = new PacmanGame(SIMPLE3, 10);
  
  // Blinky is always at index 0 in the enemies array
  Blinky b = (Blinky) game.getEnemies()[0];
  
  // Blinky is at (2, 2) and his scatter target is
  // at -3, 3, so moving up to (1, 2) will minimize
  // the straight-line distance to the target
  b.setMode(SCATTER, SimpleTest.makeDescriptor(game));  
  System.out.println(b.getNextCell()); // expected (1, 2)
  System.out.println();
  
  // update should move up 0.4 units
  b.update(SimpleTest.makeDescriptor(game));
  System.out.println(b.getCurrentDirection());  // UP
  System.out.println(b.getRowExact() + ", " + b.getColExact()); // now 2.1, 2.5
  System.out.println();
  
  // now, if we change the mode to CHASE, since we are already past the midpoint of
  // the current cell in direction UP, the implicit call to calculateNextCell 
  // should not do anything
  b.setMode(CHASE, SimpleTest.makeDescriptor(game));  
  System.out.println(b.getNextCell()); // still (1, 2)
  System.out.println(b.getCurrentDirection()); // still UP
  System.out.println(b.getRowExact() + ", " + b.getColExact()); // still 2.1, 2.5
  System.out.println();
  // but when we update again, we cross the boundary into (1, 2), which
  // triggers a call to calculateNextCell.  Since the mode is now 
  // CHASE, the target is now at (4, 1).  We can't go down, which would be a
  // reversal of direction, so the neighboring cell that is closest to the
  // target is to the left at (1, 1)
  b.update(SimpleTest.makeDescriptor(game));
  System.out.println(b.getNextCell()); // expected (1, 1)
  System.out.println(b.getCurrentDirection());  // still UP
  System.out.println(b.getRowExact() + ", " + b.getColExact()); // 1.7, 2.5
  System.out.println();
  // next update should continue up to center of cell at 1.5 and then change direction
  b.update(SimpleTest.makeDescriptor(game));
  System.out.println(b.getNextCell()); // expected 1, 1
  System.out.println(b.getCurrentDirection()); // expected LEFT
  System.out.println(b.getRowExact() + ", " + b.getColExact()); // 1.5, 2.5
  System.out.println();
  
  }
}