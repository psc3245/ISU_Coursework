package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import api.Line;
import api.GridCell;
import api.Location;
import api.StringUtil;
import hw3.LinesGame;
import hw3.Util;

/**
 * Main panel for the user interface a Lines game.
 * @author smkautz
 */
public class GamePanel extends JPanel
{ 
  /**
   * Line colors. 
   */
  public static final Color[] COLORS = {
    Color.RED,
    Color.GREEN,
    Color.BLUE,
    Color.CYAN,
    Color.YELLOW,
    Color.MAGENTA,
    Color.ORANGE,
    Color.PINK,
    new Color(122, 35, 48), // SCARLET
    new Color(153, 51, 255), // VIOLET
    new Color(0, 153, 51)    // FOREST
  };

  /**
   * Cell size in pixels.
   */
  public static final int SIZE = 40; 
  
  /**
   * Dot size in pixels, must be less than or equal to SIZE.
   */
  public static final int DOT_SIZE_BIG = 30; 

  /**
   * Dot size in pixels, must be less than or equal to SIZE.
   */
  public static final int DOT_SIZE_LITTLE = 20; 
  
  /**
   * Line width in pixels.
   */
  public static final int LINE_SIZE = 6;
  
  /**
   * Font size for displaying score.
   */
  public static final int SCORE_FONT = 24; 

  /**
   * Background color.
   */
  public static final Color BACKGROUND_COLOR = Color.BLACK;

  /**
   * Color for cell outlines.
   */
  public static final Color GRID_COLOR = Color.DARK_GRAY;
  
  /**
   * Suppress compiler warning.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Score panel associated with the game.
   */
  private ScorePanel scorePanel;

  /**
   * The IGame instance for which this is the UI.
   */
  private LinesGame game;

  /**
   * Only attempt to add a cell to the current line if 
   * the mouse entered a different cell than the previous one.
   */
  private Location lastEnteredCell;
  
  /**
   * Constructs a GamePanel with the given game associated ScorePanel.
   * @param game 
   *   the Game instance for which this is the UI
   * @param scorePanel
   *   panel for displaying scores associated with the game
   */
  public GamePanel(LinesGame game, ScorePanel scorePanel)
  {
    this.game = game;
    this.scorePanel = scorePanel;
    addMouseListener(new MyMouseListener());
    addMouseMotionListener(new MyMouseMotionListener());
    lastEnteredCell = new Location(-1, -1);
  }

  /**
   * Start over with a new game.
   * @param game
   */
  public void reset(LinesGame game)
  {
    this.game = game;
    scorePanel.reset();
  }
  
  // The paintComponent method is invoked by the Swing framework whenever
  // the panel needs to be rendered on the screen.  In this application,
  // repainting is normally triggered by the calls to the repaint() 
  // method in the timer callback and the mouse handlers

  @Override
  public void paintComponent(Graphics g)
  {
    // clear background
    g.setColor(GamePanel.BACKGROUND_COLOR);
    g.fillRect(0, 0, game.getWidth() * SIZE, game.getHeight() * SIZE);
    
    // first draw all the cell outlines once
    ((Graphics2D) g).setStroke(new BasicStroke(1));
    for (int row = 0; row < game.getHeight(); ++row)
    {
      for (int col = 0; col < game.getWidth(); ++col)
      {
        g.setColor(GamePanel.GRID_COLOR);

        int x = GamePanel.SIZE * col;
        int y = GamePanel.SIZE * row;
        ((Graphics2D) g).setStroke(new BasicStroke(1));

        g.drawRect(x, y, GamePanel.SIZE - 1, GamePanel.SIZE - 1);
        
        GridCell gc = game.getCell(row, col);
        int id = gc.getId();
        if (gc.isOpen() && gc.getCount() == gc.getMaxCount())
        {
          id = StringUtil.findIdForOpenCell(row, col, game.getAllLines());          
        }
        Color c;
        if (id < 0 || id >= GamePanel.COLORS.length)
        {
          c = Color.LIGHT_GRAY;
        }
        else
        {
          c = getColorForIndex(id);
        }
         
        if (!gc.isCrossing())
        {
          // if color is known, paint the cell background
          if (gc.getCount() == gc.getMaxCount())
          {
            paintOneCell(g, row, col, c);
          }
          if (!gc.isOpen())
          {
            paintOneCircle(g, row, col, c, gc.isEndpoint(), gc.getCount() == gc.getMaxCount());
          }
        }
      }
    }     
      
    // paint lines
    ArrayList<Line> lines = game.getAllLines();   
    for (Line f : lines)
    {
      int id = f.getId();
      Color c = getColorForIndex(id);
      ArrayList<Location> cells = f.getCells();

      if (cells.size() >= 2)
      {
        for (int i = 0; i < cells.size() - 1; ++i)
        {
          Location src = cells.get(i);
          Location dst = cells.get(i + 1);
          makeLine(g, src.col(), src.row(), dst.col(), dst.row(), c);
        }       
      }  
    }
    
    // finally paint all the crossings
    for (int row = 0; row < game.getHeight(); ++row)
    {
      for (int col = 0; col < game.getWidth(); ++col)
      {
        GridCell gc = game.getCell(row, col);
        if (gc.isCrossing())
        {
          paintOneCrossing(g, row, col, gc.getMaxCount() - gc.getCount());
        }
        
      }
    }     

        
  }
  
  /**
   * Draws line from center of first cell to center of second
   */
  private void makeLine(Graphics g, int col1, int row1, int col2, int row2, Color color)
  {
    int s = GamePanel.SIZE;
    int x1 = col1 * s + s / 2;
    int y1 = row1 * s + s / 2;
    int x2 = col2 * s + s / 2;
    int y2 = row2 * s + s / 2;
    g.setColor(color);
    ((Graphics2D) g).setStroke(new BasicStroke(GamePanel.LINE_SIZE));
    g.drawLine(x1, y1, x2, y2);
  }
  
  /**
   * Renders an endpoint or middle cell.
   */
  private void paintOneCircle(Graphics g, int row, int col, Color color, boolean big, boolean fill)
  {
    // scale everything up by the SIZE
    int x = GamePanel.SIZE * col;
    int y = GamePanel.SIZE * row;
    int offset = big ? (GamePanel.SIZE - GamePanel.DOT_SIZE_BIG) / 2 :
      (GamePanel.SIZE - GamePanel.DOT_SIZE_LITTLE) / 2;
    g.setColor(color);
    int size = big ? GamePanel.DOT_SIZE_BIG : GamePanel.DOT_SIZE_LITTLE;
    if (!fill)
    {
      g.drawOval(x + offset, y + offset, size, size);
    }
    else
    {
      g.fillOval(x + offset, y + offset, size, size);
    }
  }

  
  private void paintOneCrossing(Graphics g, int row, int col, int count)
  {
    // scale everything up by the SIZE
    int x = GamePanel.SIZE * col;
    int y = GamePanel.SIZE * row;
    

    
    if (count == 0)
    {
      int offset = (GamePanel.SIZE - GamePanel.DOT_SIZE_LITTLE) / 2;
      int size = GamePanel.DOT_SIZE_LITTLE;
      g.setColor(Color.LIGHT_GRAY);
      g.fillOval(x + offset, y + offset, size, size);
    }
    else
    {

      int offset = (GamePanel.SIZE - GamePanel.DOT_SIZE_BIG) / 2;
      g.setColor(Color.LIGHT_GRAY);

      int size = GamePanel.DOT_SIZE_BIG;
      g.fillOval(x + offset, y + offset, size, size);

      g.setColor(Color.BLACK);
      Font font = new Font(Font.SANS_SERIF, Font.PLAIN, GamePanel.SCORE_FONT);
      g.setFont(font);
      FontMetrics metrics = g.getFontMetrics(font);
      String text = "" + count;
      int width = metrics.stringWidth(text);
      int height = metrics.getHeight();
      x = x + GamePanel.SIZE / 2 - width / 2;

      // not sure why adding height / 2 is too much
      y = y + GamePanel.SIZE / 2 + height / 3;

      g.drawString(text, x, y);
    }
  }
  
  /**
   * Renders background of a single cell of the grid.
   * 
   * @param g the Swing graphics context
   * @param row y-coordinate of the cell to render
   * @param col x-coordinate of the cell to render
   * @param color the color to render
   */
  private void paintOneCell(Graphics g, int row, int col, Color color)
  {
    // scale everything up by the SIZE
    int x = GamePanel.SIZE * col;
    int y = GamePanel.SIZE * row;
    Color mutedColor = color.darker().darker();
    g.setColor(mutedColor);
    g.fillRect(x, y, GamePanel.SIZE, GamePanel.SIZE);
    g.setColor(GamePanel.GRID_COLOR);
    g.drawRect(x, y, GamePanel.SIZE - 1, GamePanel.SIZE - 1);  
  }


  
  
  
  private Color getColorForIndex(int i)
  {
    if (i >= 0 && i < COLORS.length)
    {
      return COLORS[i];
    }
    return Color.LIGHT_GRAY;
  }
  
  /**
   * Callback for mouse events.
   */
  private class MyMouseListener implements MouseListener
  {

    @Override
    public void mouseClicked(MouseEvent event)
    {
    }

    @Override
    public void mousePressed(MouseEvent event)
    {
      if (!game.isComplete())
      {     
        int row = event.getY() / GamePanel.SIZE;
        int col = event.getX() / GamePanel.SIZE;
        lastEnteredCell = new Location(row, col);
        game.startLine(row, col);
      }
      repaint();
    }

    @Override
    public void mouseReleased(MouseEvent event)
    {
      game.endLine(); 
      lastEnteredCell = new Location(-1, -1);
      repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }
  }
  
  /**
   * Callback for mouse motion events.
   */
  private class MyMouseMotionListener implements MouseMotionListener
  {

    @Override
    public void mouseDragged(MouseEvent e)
    {
      if (!game.isComplete()) 
      {
        int row = e.getY() / GamePanel.SIZE;
        int col = e.getX() / GamePanel.SIZE;
        
        // make sure we are within the middle of the cell
        int rOffset = e.getY() % GamePanel.SIZE;
        int cOffset = e.getX() % GamePanel.SIZE;
        int margin = GamePanel.SIZE / 4;
        boolean inMiddle = rOffset > margin && rOffset < GamePanel.SIZE - margin
            && cOffset > margin && cOffset < GamePanel.SIZE - margin;
        boolean inBounds = row >= 0 && col >= 0 && row < game.getHeight() && col < game.getWidth();
        if (inBounds && inMiddle)
        {
          // if we have just called addCell for this location, 
          // don't do it again
          if (lastEnteredCell.row() != row || lastEnteredCell.col() != col)
          {
            game.addCell(row, col);
            lastEnteredCell = new Location(row, col);
            scorePanel.updateScore(game.getMoveCount());
          }
        }
        
        if (game.isComplete())
        {
          //timer.stop();
          scorePanel.gameOver();
        }

      }
      repaint();

    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
    }
    
  }
  

}

