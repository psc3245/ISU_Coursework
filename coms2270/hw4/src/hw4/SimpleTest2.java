package hw4;

import api.PacmanGame;
import hw4.Blinky;
import static api.Mode.*;
/**
 * Some ideas for initially testing the calculateNextCell() method.
 */
public class SimpleTest2
{
  // one ghost
  public static final String[] SIMPLE2 = {
    "############",
    "#..........#",
    "#.B........#",
    "#..........#",
    "#S.........#",
    "############",     
  };
  
  public static void main(String[] args)
  {
  // using a frame rate of 10, the speed increment will be 0.4
  PacmanGame game = new PacmanGame(SIMPLE2, 10);
  
  // Blinky is always at index 0 in the enemies array
  Blinky b = (Blinky) game.getEnemies()[0];
  
  // Blinky is at (2, 2) and his scatter target is
  // at -3, 9, so moving right to (2, 3) will minimize
  // the straight-line distance to the target
  b.setMode(SCATTER, SimpleTest.makeDescriptor(game));  
  System.out.println(b.getNextCell()); // expected (2, 3)
  
  // Since already at the center of current cell, 
  // should immediately change direction to RIGHT
  // at first update without actually moving up
  System.out.println(b.getCurrentDirection());  // UP
  b.update(SimpleTest.makeDescriptor(game));
  System.out.println(b.getCurrentDirection());  // RIGHT
  System.out.println(b.getRowExact() + ", " + b.getColExact()); // still 2.5, 2.5
  System.out.println();
  
  // a similar test using CHASE mode
  game = new PacmanGame(SIMPLE2, 10);
  b = (Blinky) game.getEnemies()[0];
  // This time, next cell should minimize distance
  // to the location of Pacman, provided in the descriptor,
  // This would appear to be the cell (3, 2) directly below,
  // but that direction is off limits because it's the opposite
  // of the current direction UP, and ghosts are not allowed
  // to reverse direction except in FRIGHTENED mode.
  // So among the possible next cells (1, 2), (2, 1), and (2, 3),
  // going left to (2, 1) minimizes the distance to target.
  b.setMode(CHASE, SimpleTest.makeDescriptor(game));  
  System.out.println(b.getNextCell()); // expected (2, 1)
  
  
  
  }
}