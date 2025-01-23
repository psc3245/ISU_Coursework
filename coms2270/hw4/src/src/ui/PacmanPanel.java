package src.ui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import api.Actor;
import api.CellType;
import api.Direction;
import api.Location;
import api.MazeCell;
import api.Mode;
import api.PacmanGame;

/**
 * UI for a Pacman game.  This UI does very little besides
 * a) send periodic update commands to the game, 
 * b) send turn instructions to the game when one of
 * the arrow keys is pressed, and 
 * c) draw the game state
 */
public class PacmanPanel extends JPanel 
{
  /**
   * Size in pixels of the cells for the grid.
   */
  public static final int CELL_SIZE = 30;
  
  /**
   * Size of wall squares and energizers.
   */
  public static final int BORDER_SIZE = CELL_SIZE / 2;
  
  /**
   * Size of dots.
   */
  public static final int PELLET_SIZE = CELL_SIZE / 4;

  /**
   * Slightly brighter blue for wall squares.
   */
  public static final Color WALL_COLOR = new Color(100, 100, 255); 
  
  /**
   * Pale green to draw grid, if desired.
   */
  public static final Color GRID_COLOR = new Color(0, 150, 0); 

  /**
   * The grid to be displayed by this panel.
   */
  private PacmanGame game; 

  /**
   * Timer for game updates.
   */
  private Timer timer;
  
  /**
   * Timer interval, determined by game's preferred frame rate.
   */
  private int interval;
  
  
  
  private Location prev;
  private double angle;
  private double arcIncrement;
  
  /**
   * Constructs a panel to display the given game.
   * @param game
   *   the grid to be displayed
   * @param sleepTime
   *   time between frames, in milliseconds
   */
  public PacmanPanel(PacmanGame game)
  {
    this.game = game;   
    prev = game.getPlayer().getCurrentLocation();
    interval = 1000 / game.getFrameRate();   
    timer = new Timer(interval, new TimerCallback());
    timer.start();    
    this.addKeyListener(new MyKeyListener());
  }

  @Override
  public void paintComponent(Graphics g)
  {
    // clear background
    g.clearRect(0, 0, getWidth(), getHeight());
    
    for (int row = 0; row < game.getNumRows(); ++row)
    {
      for (int col = 0; col < game.getNumColumns(); ++col)
      {
        g.setColor(Color.BLACK);
        g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);         

        
        //Color color = getColor(row, col);
        MazeCell c = game.getCell(row, col);
        if (c.isWall())
        {
          g.setColor(WALL_COLOR);
          g.drawRect(col * CELL_SIZE + BORDER_SIZE / 2, row * CELL_SIZE + BORDER_SIZE / 2, BORDER_SIZE, BORDER_SIZE);
        }
        else
        {
          if (c.getType() == CellType.DOT && c.canEat())
          {
            g.setColor(Color.WHITE);
            g.fillOval(col * CELL_SIZE + (CELL_SIZE - PELLET_SIZE) / 2, row * CELL_SIZE + (CELL_SIZE - PELLET_SIZE) / 2, PELLET_SIZE, PELLET_SIZE);
          }
          else if (c.getType() == CellType.ENERGIZER && c.canEat())
          {
            g.setColor(Color.LIGHT_GRAY);
            //g.fillOval(col * cellSize + (cellSize - PELLET_SIZE) / 2, row * cellSize + (cellSize - PELLET_SIZE) / 2, PELLET_SIZE, PELLET_SIZE);
            g.fillOval(col * CELL_SIZE + BORDER_SIZE / 2, row * CELL_SIZE + BORDER_SIZE / 2, BORDER_SIZE, BORDER_SIZE);
            
          }
        }
      }
    }
    
    // draw grid ?
//    g.setColor(GRID_COLOR);
//    for (int row = 0; row < maze.getNumRows(); ++row)
//    {
//      for (int col = 0; col < maze.getNumColumns(); ++col)
//      {
//        g.drawRect(col * cellSize, row * cellSize, cellSize, cellSize);
//      }
//    }

    
    drawPacman(g);
    drawGhosts(g);

   }
  
  private void drawPacman(Graphics g)
  {
    // upper left corner
    Actor pacman = game.getPlayer();
    Direction dir = pacman.getCurrentDirection();
    
    int dirDegrees = 0;
    switch (dir)
    {
      case LEFT:
        dirDegrees = 180;
        break;
      case RIGHT:
        dirDegrees = 0;
        break;
      case UP:
        dirDegrees = 90;
        break;
      case DOWN:
        dirDegrees = 270;
    }
    
    int currAngle = (int) Math.round(angle);
    int start = dirDegrees + currAngle;
    int sweep = 360 - currAngle * 2;
    
    double pmRow = pacman.getRowExact() - 0.5;
    int rowPixel = (int) Math.round(pmRow * CELL_SIZE);
    double pmCol = pacman.getColExact() - 0.5;
    int colPixel = (int) Math.round(pmCol * CELL_SIZE);
    g.setColor(Color.YELLOW);
    //g.fillOval(colPixel, rowPixel, CELL_SIZE, CELL_SIZE);
    g.fillArc(colPixel, rowPixel, CELL_SIZE, CELL_SIZE, start, sweep);
  }

  
  private void drawGhosts(Graphics g)
  {
    Actor[] enemies = game.getEnemies();
    Color[] colorHints = game.getColorHints();
    for (int i = 0; i < enemies.length; ++i)
    {
      Actor ghost = enemies[i];
      if (ghost.getMode() == Mode.FRIGHTENED)
      {
        g.setColor(Color.BLUE);
        
        // flash every QUARTER second = 8 flashes in last 4 seconds
        int count = game.getFrightenedCount();
        int quarterSecondCount = 250 / interval;
        if (quarterSecondCount * 16 >= count)
        {
          int flag = count / quarterSecondCount;
          if (flag % 2 == 1)
          {
            g.setColor(Color.WHITE);
          }
        }
      }
      else if (ghost.getMode() == Mode.DEAD)
      {
        g.setColor(Color.DARK_GRAY);
      }
      else
      {
        g.setColor(colorHints[i]);
      }

      double pmRow = ghost.getRowExact() - 0.5;
      int rowPixel = (int) Math.round(pmRow * CELL_SIZE);
      double pmCol = ghost.getColExact() - 0.5;
      int colPixel = (int) Math.round(pmCol * CELL_SIZE);
      g.fillOval(colPixel, rowPixel, CELL_SIZE, CELL_SIZE);
      g.fillRect(colPixel, rowPixel + CELL_SIZE / 2, CELL_SIZE, CELL_SIZE / 2);

      
      int eyeSize = CELL_SIZE / 4;
      int eyeSep = eyeSize / 2 + 1;
      int left = CELL_SIZE / 2 - eyeSep - eyeSize / 2;
      int right = CELL_SIZE / 2 + eyeSep - eyeSize / 2;
      int vertOffset = CELL_SIZE / 10;
      g.setColor(Color.WHITE);
      
      g.fillOval(colPixel + left, rowPixel + vertOffset, eyeSize, eyeSize + 1);
      g.fillOval(colPixel + right, rowPixel + vertOffset, eyeSize, eyeSize + 1);
      
      
      int eyeballX = eyeSize / 2;
      int eyeballY = eyeSize / 2;
      int shift = eyeSize / 4;
      Direction dir = ghost.getCurrentDirection();
      
      switch (dir)
      {
        case LEFT:
          eyeballX -= shift;
          break;
        case RIGHT:
          eyeballX += shift;
          break;
        case UP:
          eyeballY -= shift;
          break;
        case DOWN:
          eyeballY += shift + 1;
      }
      
      int xPos = left + eyeballX - eyeSize / 4;
      int yPos = vertOffset + eyeballY - eyeSize / 4;
      
      g.setColor(Color.BLACK);
      
      g.fillOval(colPixel + xPos, rowPixel + yPos, eyeSize / 2, eyeSize / 2);
      xPos = right + eyeballX - eyeSize / 4;
      g.fillOval(colPixel + xPos, rowPixel + yPos, eyeSize / 2, eyeSize / 2);

      
    }
  }
  
  
  
  private class MyKeyListener implements KeyListener
  {
    @Override
    public void keyPressed(KeyEvent event)
    {
      //System.out.println("key " + event);
      int key = event.getKeyCode();
      Direction dir = null;
      switch( key ) { 
        case KeyEvent.VK_UP:
          dir = Direction.UP;
          break;
        case KeyEvent.VK_DOWN:
          dir = Direction.DOWN;
          break;
        case KeyEvent.VK_LEFT:
          dir = Direction.LEFT;
          break;
        case KeyEvent.VK_RIGHT :
          dir = Direction.RIGHT;
          break;
        default:
          return;
      }
      
      game.turnPlayer(dir);
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
      // do nothing
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
      // do nothing
    }
  }
  
  
  private class TimerCallback implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      game.updateAll();  
      
      // calculation for eating animation...
      Actor pacman = game.getPlayer();
      Location curr = pacman.getCurrentLocation();
      if (!curr.equals(prev))
      {
        // transitioned to new cell, start closing pacman's mouth
        prev = curr;
        double currSpeed = pacman.getCurrentIncrement();
        int numUpdates = (int) (1.0 / currSpeed / 2);
        arcIncrement = - 50 / numUpdates;
        angle = 50 + arcIncrement;
      }
      else if (angle <= 0)
      {
        arcIncrement = - arcIncrement;
        angle = arcIncrement;
      }
      else if (angle < 50)
      {
        angle += arcIncrement;
      }
      
      
      repaint();
    }   
  }

}
