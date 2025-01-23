package hw2;

import static api.ThrowType.*;

/**
 * A few basic tests of the DartGame methods as shown in the
 * assignment pdf.
 */
public class SimpleTests
{
  public static void main(String[] args)
  {
    // basic initial state
    DartGame g = new DartGame(1, 100, 3);
    System.out.println(g.getScore(0));        // expected 100
    System.out.println(g.getScore(1));        // expected 100
    System.out.println(g.getCurrentPlayer()); // expected 1
    System.out.println(g.getDartCount());     // expected 3  
    System.out.println(g);
    System.out.println();
    // updating score and reducing dart count
    g = new DartGame(1, 100, 3);
    g.throwDart(DOUBLE, 10);
    System.out.println(g);
    g.throwDart(DOUBLE, 10);
    System.out.println(g);
    g.throwDart(DOUBLE, 10);
    System.out.println(g);
    g.throwDart(DOUBLE, 10);
    System.out.println(g);
    System.out.println();
    // Expected:
    //    Player 0: 100  Player 1: 80  Current: Player 1  Darts: 2
    //    Player 0: 100  Player 1: 60  Current: Player 1  Darts: 1
    //    Player 0: 100  Player 1: 40  Current: Player 0  Darts: 3
    //    Player 0: 80  Player 1: 40  Current: Player 0  Darts: 2
    
    
    // checking the static method calcPoints
    System.out.println(DartGame.calcPoints(MISS, 42));   // expected 0 (second arg ignored)
    System.out.println(DartGame.calcPoints(SINGLE, 7)); // expected 7
    System.out.println(DartGame.calcPoints(DOUBLE, 10)); // expected 20
    System.out.println(DartGame.calcPoints(TRIPLE, 8)); // expected 24
    System.out.println(DartGame.calcPoints(OUTER_BULLSEYE, 42)); // expected 25
    System.out.println(DartGame.calcPoints(INNER_BULLSEYE, 42)); // expected 50
    System.out.println();
    
    // player 1 goes bust
    g = new DartGame(1, 50, 3);
    g.throwDart(DOUBLE, 15);
    System.out.println(g);
    g.throwDart(DOUBLE, 12);
    System.out.println(g);
    System.out.println();
    // Expected:
    //   Player 0: 50  Player 1: 20  Current: Player 1  Darts: 2
    //   Player 0: 50  Player 1: 50  Current: Player 0  Darts: 3
    
    // player 1 goes bust, again
    g = new DartGame(1, 50, 3);
    g.throwDart(DOUBLE, 15);
    System.out.println(g);
    g.throwDart(SINGLE, 5);
    g.throwDart(SINGLE, 5);
    System.out.println(g);  // player 0's turn, player 1 score now 10
    g.throwDart(DOUBLE, 1);
    g.throwDart(DOUBLE, 1);
    g.throwDart(DOUBLE, 1); // player 0 having a bad day
    System.out.println(g);     
    g.throwDart(SINGLE, 5);
    g.throwDart(SINGLE, 5); // player 1 gone bust
    System.out.println(g);  // player 0's turn, player 1 score back to 10
    System.out.println();
    // Expected:
    //    Player 0: 50  Player 1: 20  Current: Player 1  Darts: 2
    //    Player 0: 50  Player 1: 10  Current: Player 0  Darts: 3
    //    Player 0: 44  Player 1: 10  Current: Player 1  Darts: 3
    //    Player 0: 44  Player 1: 10  Current: Player 0  Darts: 3
   
    // doubling in
    g = new DartGame(1, 100, 3);
    g.throwDart(SINGLE, 10);
    System.out.println(g);   // score still 100
    g.throwDart(DOUBLE, 7);
    System.out.println(g);   // ok, score now 86
    // Expected:   
    //    Player 0: 100  Player 1: 100  Current: Player 1  Darts: 2
    //    Player 0: 100  Player 1: 86  Current: Player 1  Darts: 1
    
    
  }
}
