package src.ui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import api.PacmanGame;

public class RunGame
{
  // no ghost
  public static final String[] TEST0 = {
    "#######",
    "#*...*#",
    "#.###.#",
    "#.....#",
    "#.#.#.#",
    "#.#.#.#",
    "#..S..#",
    "#######",    
  };
  
  // one ghost
  public static final String[] TEST1 = {
    "#######",
    "#*...*#",
    "#.###.#",
    "#.....#",
    "#.#B#.#",
    "#.#.#.#",
    "#..S..#",
    "#######",     
  };
  
  // one ghost, and a tunnel
  public static final String[] TEST2 = {
    "#######",
    "#*...*#",
    "#.###.#",
    " ..B.. ",
    "#.#.#.#",
    "#.#.#.#",
    "#..S..#",
    "#######",    
  };


  // the classic Pacman maze
  public static final String[] MAIN1 = {
    "############################",
    "#............##............#",
    "#.####.#####.##.#####.####.#",
    "#*####.#####.##.#####.####*#",
    "#.####.#####.##.#####.####.#",
    "#..........................#",
    "#.####.##.########.##.####.#",
    "#.####.##.########.##.####.#",
    "#......##....##....##......#",
    "######.##### ## #####.######",
    "     #.##### ## #####.#     ",
    "     #.##          ##.#     ",
    "     #.## ##BPIC## ##.#     ",
    "######.## ######## ##.######",
    "      .   ##    ##   .      ",
    "######.## ######## ##.######",
    "     #.## ######## ##.#     ",
    "     #.##          ##.#     ",
    "     #.## ######## ##.#     ",
    "######.## ######## ##.######",
    "#............##............#",
    "#.####.#####.##.#####.####.#",
    "#.####.#####.##.#####.####.#",
    "#*..##................##..*#",
    "###.##.##.########.##.##.###",
    "###.##.##.########.##.##.###",
    "#......##...S##....##......#",
    "#.##########.##.##########.#",
    "#.##########.##.##########.#",
    "#..........................#",
    "############################",
  };   




  public static void main(String[] args)
  {
    final PacmanGame maze = new PacmanGame(TEST1, 50);
    Runnable r = new Runnable()
    {
      public void run()
      {
        createAndShow(maze);
      }
    };
    SwingUtilities.invokeLater(r);
  }

  protected static void createAndShow(final PacmanGame maze)
  {

    // create the frame
    JFrame frame = new JFrame("Nonbinary Pac-person");

    // create an instance of our JPanel subclass and
    // add it to the frame
    PacmanPanel panel = new PacmanPanel(maze);
    frame.getContentPane().add(panel);
    panel.setPreferredSize(new Dimension(
        maze.getNumColumns() * PacmanPanel.CELL_SIZE,
        maze.getNumRows() * PacmanPanel.CELL_SIZE));

    // give it a nonzero size
    frame.pack();

    // we want to shut down the application if the
    // "close" button is pressed on the frame
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // make the frame visible and start the UI machinery
    frame.setVisible(true);
    
    // make sure panel gets key events
    panel.grabFocus();

  }

}
