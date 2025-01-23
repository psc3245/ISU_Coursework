package hw4;

import api.Descriptor;
import api.Direction;
import api.Location;
import api.PacmanGame;
import hw4.Blinky;
import static api.Direction.RIGHT;
import static api.Direction.UP;
import static api.Mode.*;
/**
 * Some ideas for initially testing the update() method.
 */
// Same idea as SimpleTest.java, but instead of assuming a bogus implementation
// of calculateNextCell, this assumes you have calculateNextCell and 
// setMode working correctly
public class SimpleTest1b
{
  
  public static final String[] SIMPLE1a = {
    "#######",
    "#.....#",
    "#....S#",
    "#.....#",
    "#.....#",
    "#..B..#",
    "#.....#",
    "#######",     
  };
  
  
  public static void main(String[] args)
  {
    // using a frame rate of 30, the speed increment will be about .133
    PacmanGame game = new PacmanGame(SIMPLE1a, 30);
    
    // Blinky is always at index 0 in the enemies array
    Blinky b = (Blinky) game.getEnemies()[0];
        
    
    
    // verify initial state is INACTIVE
    System.out.println("Check initial state");
    System.out.println(b.getMode());  // INACTIVE
    System.out.println(b.getRowExact() + ", " + b.getColExact());  // 5.5, 3.5
    
    // calculateNextCell does nothing when in INACTIVE mode
    b.calculateNextCell(makeDescriptor(game));
    System.out.println(b.getNextCell());  // still null
    
    // update does nothing when in INACTIVE mode
    b.update(makeDescriptor(game));
    System.out.println(b.getRowExact() + ", " + b.getColExact());  // still 5.5, 3.5
    System.out.println("----------");
    System.out.println();
    
    
    System.out.println("Setting SCATTER mode");
    // this should invoke calculateNextCell after setting mode
    b.setMode(SCATTER, null);
    System.out.println(b.getMode());  // SCATTER
    System.out.println(b.getNextCell()); // expected (4, 3)
    System.out.println();
    
    System.out.println("Do 18 calls to update():");
    for (int i = 0; i < 18; ++i)
    {
      b.update(makeDescriptor(game));
      System.out.println(b.getRowExact() + ", " + b.getColExact());
      System.out.println(b.getCurrentDirection());
      System.out.println(b.getNextCell());
    }    
    // Expected: should be at 3.1, 3.5 with next cell (2, 3) and direction UP
    System.out.println("----------");
    System.out.println();
    
    System.out.println("Setting CHASE mode");   
    b.setMode(CHASE, makeDescriptor(game));
    System.out.println(b.getRowExact() + ", " + b.getColExact());
    System.out.println(b.getCurrentDirection());
    System.out.println(b.getNextCell());
    System.out.println();
    // Expected: still at 3.1, 3.5 with next cell (2, 3) and direction UP
    // Our target in CHASE mode is now Pacman, at (2, 5), so going right to (3, 4) would
    // take us closer.  But since we are already past the center of cell (3, 3), 
    // the implicit call to calculateNextCell from setMode does nothing (i.e, since
    // we are already past the center of our current cell, we can't change direction here)
    System.out.println("----------");
    System.out.println();
    System.out.println("Do two calls to update():");
    b.update(makeDescriptor(game));
    b.update(makeDescriptor(game));
       System.out.println(b.getRowExact() + ", " + b.getColExact());
    System.out.println(b.getCurrentDirection());
    System.out.println(b.getNextCell());
    // Expected: now we are at 2.83, 3.5
    // Crossing into our next cell (2, 3) triggers a call to calculateNextCell.  
    // Now the closest neighboring cell to target is to the right at (2, 4).
    // Current direction is still UP
    System.out.println("----------");
    System.out.println();
    System.out.println("Do two calls to update():");
    b.update(makeDescriptor(game));
    b.update(makeDescriptor(game));
    System.out.println(b.getRowExact() + ", " + b.getColExact());
    System.out.println(b.getCurrentDirection());
    System.out.println(b.getNextCell());
    // Expected: now we are at 2.56, 3.5
    System.out.println("----------");
    System.out.println();
   
    System.out.println("Do one call to update():");
    b.update(makeDescriptor(game));
    System.out.println(b.getRowExact() + ", " + b.getColExact());
    System.out.println(b.getCurrentDirection());
    System.out.println(b.getNextCell());    
    // Since we have to change direction to go to next cell (2, 4), we
    // adjust the increment so as not to go past the center of the current cell,
    // so we end up at 2.5, 3.5 and the current direction is now RIGHT.
    System.out.println("----------");
    System.out.println();
   
    System.out.println("Do four more calls to update():");
    for (int i = 0; i < 4; ++i)
    {
      b.update(makeDescriptor(game));
      System.out.println(b.getRowExact() + ", " + b.getColExact());
      System.out.println(b.getCurrentDirection());
      System.out.println(b.getNextCell());
    }
    // Expected: should end up at 2.5, 4.03 with next cell (2, 5)
  }
  
  
  public static Descriptor makeDescriptor(PacmanGame game)
  {
    Location enemyLoc = game.getEnemies()[0].getCurrentLocation();
    Location playerLoc = game.getPlayer().getCurrentLocation();
    Direction playerDir = game.getPlayer().getCurrentDirection();
    return new Descriptor(playerLoc, playerDir, enemyLoc);
  }
}