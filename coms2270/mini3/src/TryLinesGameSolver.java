
import java.util.ArrayList;

import api.Solution;
import hw3.LinesGame;
import mini3.LinesGameSolver;

public class TryLinesGameSolver
{
  
  // 2 solutions
  public static final String[] testgrid0 = {
    "Gg",
    "gG",
  };
  
  // 1 solution
  public static final String[] testgrid = {
    "GRrR",
    "ggGB",
    "Bbbb"
  };
  
  // 2 solutions
  public static final String[] testgrid2 = {
    "GgR",
    "g2g",
    "RrG",
  };
  
  // 1 solution
  public static final String[] testgrid3 = {
    "Rrr",
    "G2r",
    "G2g",
    "Rgg"
  };
  
  // 6 solutions
  public static final String[] testgrid4 = {
    "bBO",
    "b2G",
    "22o",
    "GBO"
  };
  
  // 12 solutions
  public static final String[] testgrid5a = {
    "R-G",
    "---",
    "-RG"     
  };
  
  // 376 solutions
  public static final String[] testgrid5 = {
    "------",
    "-OR-G-",
    "BG-OR-",
    "------",
    "B-----"     
  };
  
  // 88 solutions
  public static final String[] testgrid8a = {
    "0o0O",
    "o22o",
    "ooOo"
  };
  
  // 522960 solutions - could take an hour or so,
  // if you don't run out of memory!
  public static final String[] testgrid8 = {
    "bbBb",
    "2b3b",
    "b3bB",
    "bo2O",
    "o33o",
    "ooOo"
  };
  
  public static void main(String[] args)
  {
    // create a game
    LinesGame g = new LinesGame(testgrid4);
    
    // find solutions
    ArrayList<Solution> solutions = LinesGameSolver.doSolve(g);
    
    // we could print out the solved games...
    for (Solution s : solutions)
    {
      System.out.println(s);
    }
    
    //... or see how many solutions were found
    System.out.println(solutions.size() + " solutions");
    
    // we can view solutions in the UI as well, uncomment to try this
    //ui.GameMain.display(solutions.get(0).game());
  }
 
}