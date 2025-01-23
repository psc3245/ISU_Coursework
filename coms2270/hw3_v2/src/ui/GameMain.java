package ui;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import api.GridCell;
import api.Line;
import api.Location;
import api.StringUtil;
import hw3.LinesGame;



/**
 * Main class for a GUI for a Lines game sets up a 
 * GamePanel instance in a frame.  
 * 
 * EDIT THE create() METHOD BELOW TO CONFIGURE THE GAME.
 * 
 * @author smkautz
 */
public class GameMain
{
  // sample descriptors
  public static final String[] testgrid = {
    "GRrR",
    "ggGB",
    "Bbbb"
  };

  public static final String[] testgrid2 = {
    "GrR",
    "g2g",
    "RrG",
  };

  public static final String[] testgrid3 = {
    "Rrr",
    "G2r",
    "G2g",
    "Rgg"
  };

  public static final String[] testgrid4 = {
    "bBO",
    "b2G",
    "22o",
    "GBO"
  };

  public static final String[] testgrid5 = {
    "------",
    "-OR-G-",
    "BG-OR-",
    "------",
    "B-----"     
  };

  public static final String[] testgrid6 = {
    "M--------O",
    "----------",
    "----------",
    "----------",
    "C--Y--C--Y",
    "----------",
    "----------",
    "----------",
    "M--------O"
  };

  public static final String[] testgrid7 = {
    "R---------Y",
    "OB--------G",
    "----------M",
    "S--------G-",
    "--VP-O-----",
    "-P---------",
    "----CS-B---",
    "--M----FR--",
    "----Y------",
    "---------F-",
    "VC---------"
  };
 
  
  public static final String[] testgrid8 = {
    "bbBb",
    "2b3b",
    "b3bB",
    "bo2O",
    "o33o",
    "ooOo"
  };
  
  
  
  /**
   * Helper method for instantiating the components.  This
   * method should be executed in the context of the Swing
   * event thread only.
   */
  private static void create()
  {
    LinesGame game;

    // EDIT HERE TO CHANGE THE GAME BEING CREATED
    // 1) These will correctly construct a game with the two-argument constructor,
    // so will work even if you don't have Util.createLinesFromGrid done
    game = makeSimpleGame1();
    //game = makeSimpleGame2();
   
    // 2) These require that your one-argument constructor is implemented
    // Edit to try different grids above, or make your own :)
    //game = new LinesGame(testgrid3);


    // create the three UI panels
    ScorePanel scorePanel = new ScorePanel();
    GamePanel panel = new GamePanel(game, scorePanel);
    ChooseButtonPanel choosePanel = new ChooseButtonPanel(panel, scorePanel);

    // arrange the panels vertically
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.add(choosePanel);
    mainPanel.add(scorePanel);
    mainPanel.add(panel);

    // put main panel in a window
    JFrame frame = new JFrame("Com S 227 Lines Game");
    frame.getContentPane().add(mainPanel);

    // give panels a nonzero size
    Dimension d = new Dimension(game.getWidth() * GamePanel.SIZE, game.getHeight() * GamePanel.SIZE);   
    panel.setPreferredSize(d);

    d = new Dimension(game.getWidth() * GamePanel.SIZE, 3 * GamePanel.SIZE);   
    scorePanel.setPreferredSize(d);
    // better to let the choose panel just expand to fit the button...
    //      d = new Dimension(game.getCols() * GameMain.SIZE, GameMain.SIZE);   
    //      choosePanel.setPreferredSize(d);

    frame.pack();

    // we want to shut down the application if the 
    // "close" button is pressed on the frame
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // rock and roll...
    frame.setVisible(true);
  }
  
  /**
   * Entry point.  Main thread passed control immediately
   * to the Swing event thread.
   * @param args not used
   */
  public static void main(String[] args)
  {
    Runnable r = new Runnable()
    {
      public void run()
      {
        create();
      }
    };
    SwingUtilities.invokeLater(r);
  }
  
  private static LinesGame makeSimpleGame1()
  {
    String[] test = {
      "GRrR",
      "ggGY",
      "Yyyy"
    };
    
    // The StringUtil class can make a grid from the string descriptor above
    GridCell[][] grid = StringUtil.createGridFromStringArray(test);

    // In order to make a game, we need to provide an array of Line objects 
    // containing the locations of the endpoints in the grid. This has to be 
    // consistent with grid above. Normally we should do this by calling
    // the method Util.createLinesFromGrid, but we can also do it by hand.   
    // In order to do this by hand, we have to look 
    // up in StringUtil.COLOR_CODES that 'G' has index 1, 'R' has index 0, and 
    // 'Y' has index 4.  Those indices are the ids for each line.  Note 
    // that these ids are unrelated to the array index in the lines array.
    // And be super careful about the row/column indices for the endpoints.
    ArrayList<Line> lines = new ArrayList<>();
    
    // endpoints for 'G' which corresponds to id 1
    lines.add(new Line(1, new Location(0, 0), new Location(1, 2)));

    // endpoints for 'R' which corresponds to id 0
    lines.add(new Line(0, new Location(0, 1), new Location(0, 3))); 

    // endpoints for 'Y' which corresponds to id 4
    lines.add(new Line(4, new Location(2, 0), new Location(1, 3))); 
  
    // construct the game
    LinesGame game = new LinesGame(grid, lines);
    
    return game;
  }
  
  private static LinesGame makeSimpleGame2()
  {
    String[] test = {
      "Rrr",
      "G2r",
      "G2g",
      "Rgg"
    };

    GridCell[][] grid = StringUtil.createGridFromStringArray(test);

    ArrayList<Line> lines = new ArrayList<>();
   
    // endpoints for 'G' which corresponds to id 1
    lines.add(new Line(1, new Location(1, 0), new Location(2, 0))); 
    
    // endpoints for 'R' which corresponds to id 0
    lines.add(new Line(0, new Location(0, 0),new Location(3, 0))); 
    
    LinesGame game = new LinesGame(grid, lines);
    
    return game;
  }
  
}
