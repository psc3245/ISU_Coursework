package api;

import static api.CellType.DOT;
import static api.CellType.EMPTY;
import static api.CellType.ENERGIZER;
import static api.CellType.WALL;
import static api.Direction.DOWN;
import static api.Direction.LEFT;
import static api.Direction.RIGHT;
import static api.Direction.UP;
import static api.Mode.CHASE;
import static api.Mode.DEAD;
import static api.Mode.FRIGHTENED;
import static api.Mode.INACTIVE;
import static api.Mode.SCATTER;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import hw4.Blinky;
import hw4.Pacman;


/**
 * State and logic for a Pacman game.
 */
public class PacmanGame
{

  // these should be moved into a "level" object of some kind...
  private static final Mode[] GLOBAL_MODES = {SCATTER, CHASE, SCATTER, CHASE, SCATTER, CHASE, SCATTER, CHASE};
  private static final int[] MODE_TIMES = {7, 20, 7, 20, 5, 20, 5, -1}; // seconds
  private static final double MAX_CELLS_PER_SECOND = 10.0; // cells per second
  private static final double ENEMY_SPEED_FACTOR = 0.4; // fraction of above
  private static final double PLAYER_SPEED_FACTOR = 0.4;
  private static final int INACTIVE_TIME = 4; // seconds
  private static final int FRIGHTENED_TIME = 6; // seconds

  /**
   * Number of points for each dot.
   */
  private static final int DOT_POINTS = 10;
  
  /**
   * Number of points for each energizer.
   */
  private static final int ENERGIZER_POINTS = 50;
  
  /**
   * Number of points for catching the first ghost after eating
   * an energizer.
   */
  private static final int BASE_GHOST_POINTS = 200; 
  
  /**
   * The cells for this game.
   */
  private MazeCell[][] cells;

  /**
   * Player controlled by UI.
   */
  private Pacman player;
  
  /**
   * Array of enemies.
   */
  private Actor[] enemies;
  
  /**
   * Rendering hints for the enemies.
   */
  private Color[] colorHints;

  /**
   * Countdowns for number of frames each ghost remains in INACTIVE mode, on
   * startup and on dying.
   */
  private int[] inactiveTimer;

  /**
   * Index into the array of "global" modes.
   */
  private int globalModeIndex;
  
  /**
   * Current number of pellets eaten.
   */
  private int pelletCount;
  
  /**
   * Current score.
   */
  private int score;
  
  /**
   * Countdown for number of frames to remain in the current "global" mode.
   */
  private int modeCountdown;
  
  /**
   * Countdown for number of frames to keep enemies in FRIGHTENED mode.
   * When active, this prevents the modeCountdown from decrementing.
   */
  private int frightenedCountdown;

  /**
   * Indicates whether the player is currently dead.
   */
  private boolean playerDead = false;
  
  /**
   * Number of lives for the player.
   */
  private int lives = 3;
  
  /**
   * Total number of pellets the game started with.
   */
  private int totalPellets;
  
  /**
   * Frames per second.
   */
  private int frameRate;
  
  /**
   * Current number of points awarded for catching a frightened ghost.  This number
   * doubles each time a ghost is caught, and resets to the default whenever an
   * energizer is eaten.
   */
  private int currentGhostPoints;

  /**
   * Constructs a maze based on a 2D grid.  The given strings
   * represent rows of the maze, where '#' represents a wall,
   * a blank represents a possible path, 'S' represents the 
   * starting cell, and '$' represents the goal.
   * @param rows
   *   array of strings, one per row of the maze
   */
  public PacmanGame(String[] rows, int frameRate)
  {
    this.frameRate = frameRate;
    double enemyBaseSpeed = MAX_CELLS_PER_SECOND * ENEMY_SPEED_FACTOR / frameRate;
    double playerBaseSpeed = MAX_CELLS_PER_SECOND * PLAYER_SPEED_FACTOR / frameRate;
    ArrayList<Actor> enemyList = new ArrayList<>();
    ArrayList<Color> colorList= new ArrayList<>();
    Random rand = new Random(42);
    MazeMap maze = new MazeMap(this);
    
    int width = rows[0].length();
    int height = rows.length;
    cells = new MazeCell[height][width];
    for (int row = 0; row < height; ++row)
    {
      String s = rows[row];
      for (int col = 0; col < width; ++col)
      {
        MazeCell current; // = new MazeCell();
        char c = s.charAt(col);
        if (c == '#')
        {
          current = new MazeCell(WALL);
        }
        else if (c == '.')
        {
          current = new MazeCell(DOT);
          totalPellets += 1;
        }
        else if (c == '*')
        {
          current = new MazeCell(ENERGIZER);
        }
        else
        {
          current = new MazeCell(EMPTY);

          // check for player initial position
          if (c == 'S')
          {
            //public Pacman(TwoDMaze maze, Location home, double baseSpeed, Direction initialDirection)
            Direction playerDir = findInitialDirection(row, col);
            Location playerHome = new Location(row, col);
            player = new Pacman(maze, playerHome, playerBaseSpeed, playerDir);

          }
          else
          {
            // check for ghost initial positions

            
// TODO: UNCOMMENT THESE LINES AS YOU GET THE GHOSTS IMPLEMENTED...
            
            if (c == 'B')
            {
              Location home = new Location(row, col);
              Direction dir = findInitialDirection(row, col);
              Location scatterTarget = new Location(-3, width - 3);
              Actor a = new Blinky(maze, home, enemyBaseSpeed, dir, scatterTarget, rand);
              enemyList.add(0, a);
              colorList.add(0, Color.RED);
            }
//            else if (c == 'P')
//            {
//              Location home = new Location(row, col);
//              Direction dir = findInitialDirection(row, col);
//              Location scatterTarget = new Location(-3, 2);
//              Actor a = new Pinky(maze, home, enemyBaseSpeed, dir, scatterTarget, rand);
//              enemyList.add(a);
//              colorList.add(Color.PINK);
//            }
//            else if (c == 'I')
//            {
//              Location home = new Location(row, col);
//              Direction dir = findInitialDirection(row, col);
//              Location scatterTarget = new Location(height + 1, width - 1);
//              Actor a = new Inky(maze, home, enemyBaseSpeed, dir, scatterTarget, rand);
//              enemyList.add(a);
//              colorList.add(Color.CYAN);
//            }
//            else if (c == 'C')
//            {
//              Location home = new Location(row, col);
//              Direction dir = findInitialDirection(row, col);
//              Location scatterTarget = new Location(height + 1, 0);
//              Actor a = new Clyde(maze, home, enemyBaseSpeed, dir, scatterTarget, rand);
//              enemyList.add(a);
//              colorList.add(Color.ORANGE);
//            }
          }
        }
        cells[row][col] = current;
      }
    }
    enemies = enemyList.toArray(new Actor[] {}); 
    colorHints = colorList.toArray(new Color[] {});
    resetAll();
  }
  
  /**
   * Returns true if the level is over, i.e., either the player has no lives left,
   * or all the pellets have been eaten.
   * @return
   *   true if the level is over
   */
  public boolean levelOver()
  {
    return lives == 0 || pelletCount == totalPellets;
  }
  
  /**
   * Returns the current score.
   * @return
   *   current score
   */
  public int getScore()
  {
    return score;
  }
  
  /**
   * Returns the number of frames per second assumed in this game instance.
   * @return
   *   frame rate
   */
  public int getFrameRate()
  {
    return frameRate;
  }
  
  /**
   * Suggested colors for the enemies.  This array must be the same length
   * as the enemies array.
   * @return
   *   color hints for enemies
   */
  public Color[] getColorHints()
  {
    return colorHints;
  }
  
  /**
   * Returns the cell at the given position.
   * @param row
   * @param col
   * @return
   *   cell at the given position
   */
  public MazeCell getCell(int row, int col)
  {
    return cells[row][col];
  }

  /**
   * Returns the number of rows in the grid for this maze.
   * @return
   *   number of rows in the grid
   */
  public int getNumRows()
  {
    return cells.length;
  }

  /**
   * Returns the number of columns in the grid for this maze.
   * @return
   *   number of columns in the grid
   */
  public int getNumColumns()
  {
    return cells[0].length;
  }

  /**
   * Returns the player (Pacman) in this game.
   * @return
   *   reference to the player
   */
  public Pacman getPlayer()
  {
    return player;
  }

  /**
   * Returns the array of enemies in this game.
   * @return
   *   array of enemies
   */
  public Actor[] getEnemies()
  {
    return enemies;
  }

  /**
   * Returns the current global mode for this game.  This will be the current mode
   * for all enemies except when they are frightened, inactive, or dead.
   * @return
   *   current global mode
   */
  public Mode getGlobalMode()
  {
    return GLOBAL_MODES[globalModeIndex];
  }

  /**
   * True if the player is currently dead.
   * @return
   *   true if player is dead, false otherwise
   */
  public boolean isPlayerDead()
  {
    return playerDead;
  }

  /**
   * Returns the number of lives remaining for the player.
   * @return
   *   number of lives remaining
   */
  public int getLives()
  {
    return lives;
  }
  
  /**
   * Attempt to turn the player in response to user control.
   * @param newDir
   *   desired new direction
   */
  public void turnPlayer(Direction newDir)
  {
    if (!playerDead && !levelOver())
    {
      player.tryTurn(newDir);
    }
  }
  
  /**
   * Returns the current value of the counter for the
   * period of time the ghosts remain frightened
   * @return
   *   current value of frightened countdown
   */
  public int getFrightenedCount()
  {
    return frightenedCountdown;
  }
  
  /**
   * Resets the player and enemies back to their home position and 
   * direction, revives the player, restarts the global mode
   * index and countdown, and restarts the inactive timers for
   * the enemies.
   */
  public void resetAll()
  {
    // reset everyone to home position and revive the player
    playerDead = false;
    currentGhostPoints = BASE_GHOST_POINTS;
    player.reset();
    for (int i = 0; i < enemies.length; ++i)
    {
      Actor g = enemies[i];
      g.reset();
    }

    // restart counters for mode and activity
    globalModeIndex = 0;    
    modeCountdown = MODE_TIMES[0] * frameRate;
    inactiveTimer = new int[enemies.length];    
    for (int i = 0; i < enemies.length; ++i)
    {
      if (i == 0)
      {
        inactiveTimer[i] = 1;  // should not be less than or equal to zero
      }
      else
      {
        inactiveTimer[i] = i * INACTIVE_TIME * frameRate;  // 4 seconds each?
      }
    }
  }
  
  /**
   * Method invoked once per frame to update all aspects of game, player, and enemy states.
   */
  public void updateAll()
  {
    if (levelOver()) return;

    Descriptor desc = makeDescriptor();
    
    // decrement inactive timers and activate enemies as needed
    for (int i = 0; i < inactiveTimer.length; ++i)
    {
      if (inactiveTimer[i] > 0)
      {
        inactiveTimer[i] -= 1;
        if (inactiveTimer[i] <= 0)
        {
          System.out.println("* setting mode " + i + " " +  GLOBAL_MODES[globalModeIndex]);
          enemies[i].setMode(GLOBAL_MODES[globalModeIndex], desc);
        }
      }
    }

    // if we are currently in a frightened mode countdown, then
    // decrement the count
    if (frightenedCountdown > 0)
    {
      frightenedCountdown -= 1;
      if (frightenedCountdown == 0)
      {
        for (Actor g : enemies)
        {
          // anyone who is still frightened goes back to current mode,
          // but dead or inactive stay as they are
          if (g.getMode() == FRIGHTENED)
          {
            g.setMode(GLOBAL_MODES[globalModeIndex], desc);
          }
        }
      }
    }
    else
    {
      // ...otherwise, decrement the mode countdown
      
      // Note: we don't count down the last mode, stays in CHASE
      // mode indefinitely after the last change
      if (globalModeIndex < GLOBAL_MODES.length - 1)
      {
        modeCountdown -= 1;
        if (modeCountdown <= 0)
        {
          // transition to next mode
          globalModeIndex += 1;
          for (Actor g : enemies)
          {
            // don't change the ones that are inactive/frightened/dead,
            // they'll just go into the global mode when appropriate
            if (g.getMode() == SCATTER || g.getMode() == CHASE)
            {
              g.setMode(GLOBAL_MODES[globalModeIndex], desc);
            }
          }
          
          // restart the timer, except for last mode
          if (globalModeIndex < GLOBAL_MODES.length - 1)
          {
            modeCountdown = MODE_TIMES[globalModeIndex] * frameRate;
          }
        }     
      }
    }

    // update player and enemy positions
    player.update(desc);
    for (Actor a : enemies)
    {
      a.update(desc);
    }

    // collect pellets, adjust score
    Location p = player.getCurrentLocation();
    MazeCell c = getCell(p.row(), p.col());
    if (c.getType() == CellType.DOT)
    {
      if (c.canEat())
      {
        c.eat();
        pelletCount += 1;
        score += DOT_POINTS;
      }
    }
    else if (c.getType() == CellType.ENERGIZER)
    {
      if (c.canEat())
      {
        c.eat();
        frightenedCountdown = FRIGHTENED_TIME * frameRate;
        for (Actor g : enemies)
        {
          if (g.getMode() != INACTIVE)
          {
            g.setMode(FRIGHTENED, desc);
          }
        }
        score += ENERGIZER_POINTS;
        currentGhostPoints = BASE_GHOST_POINTS;
      }
    }

    // check collisions
    for (Actor a : enemies)
    {
      if (a.getCurrentLocation().equals(player.getCurrentLocation()))
      {
        if (a.getMode() == FRIGHTENED)
        {
          a.setMode(DEAD, desc);
          score += currentGhostPoints;
          currentGhostPoints *= 2;
        }
        else if (a.getMode() != DEAD)
        {
          lives -= 1;
          playerDead = true;
          
          // TODO - have this triggered by UI after a delay
          try
          {
            Thread.sleep(4000);
          }
          catch (InterruptedException cantHappen)
          {
            // ignore
          }
          if (!levelOver())
          {
            resetAll();
          }
        }
      }
    }

    // check for dead ghosts back to home, restart inactive timers as needed
    for (int i = 0; i < enemies.length; ++i)
    {
      Actor a = enemies[i];
      if (a.getMode() == DEAD)
      {
        if (a.getCurrentLocation().equals(a.getHomeLocation()))
        {
          a.setMode(INACTIVE, desc);
          a.reset();
          inactiveTimer[i] = INACTIVE_TIME * frameRate;
        }
      }
    }
  }

  /**
   * Make a descriptor for current game state.
   * @return
   */
  private Descriptor makeDescriptor()
  {
    Location enemyLoc = null;
    if (enemies.length > 0)
    {
      enemyLoc = enemies[0].getCurrentLocation();
    }
    return new Descriptor(player.getCurrentLocation(), player.getCurrentDirection(), enemyLoc);
  }

  /**
   * Choose an initial direction for the player or enemy. This just checks
   * up, left, down, right for a non-wall, in that order.
   * @param row
   * @param col
   * @return
   */
  private Direction findInitialDirection(int row, int col)
  {
    // check in order up, left, down, right for a non-wall
    if (!getCell(row - 1, col).isWall())
    {
      return UP;
    }
    else if (!getCell(row, col - 1).isWall())
    {
      return LEFT;
    }
    else if (!getCell(row + 1, col).isWall())
    {
      return DOWN;
    }
    else if (!getCell(row, col + 1).isWall())
    {
      return RIGHT;
    }
    return LEFT;  // shouldn't happen?
  }
  
}
