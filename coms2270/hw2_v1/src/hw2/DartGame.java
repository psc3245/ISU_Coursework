
package hw2;
import api.ThrowType;
import static api.ThrowType.*;

/**
 * This class models a standard game of darts, keeping track of the scores,
 * whose turn it is, and how many darts the current player has remaining.
 * The number of starting points and the number of darts used in 
 * a player's turn are configurable.
 */
public class DartGame {  
	
	private int player0Score;
	private int player1Score;
	private int startPoints;
	private int preTurnPoints;
	
	private int currPlayer;
	
	// dartCount is how many darts each player gets
	// dartsLeft is the remaining amount of darts for the player who's turn it is
	private int dartCount;
	private int dartsLeft;
	
	
	
	/**
	 * 
	 * @param startingPlayer
	 */
	public DartGame(int startingPlayer) {
		
		currPlayer = startingPlayer;
		
		player0Score = 301;
		player1Score = 301;
		startPoints = 301;
		
		preTurnPoints = 301;
		
		dartCount = 3;
		dartsLeft = dartCount;
		
		
	}
	
	/** 
	 * 
	 * @param startingPlayer
	 * @param startingPoints
	 * @param numDarts
	 */
	public DartGame(int startingPlayer, int startingPoints, int numDarts) {
		
		currPlayer = startingPlayer;
		
		player0Score = startingPoints;
		player1Score = startingPoints;
		startPoints = startingPoints;
		
		dartCount = numDarts;
		dartsLeft = dartCount;
	}
	
	/** 
	 * 
	 * @param type
	 * @param pts
	 * @return
	 */
	static int calcPoints(ThrowType type, int pts) {
		int points = 0;
		  
		  if (type == ThrowType.MISS) {
			  points = 0;
		  }
		  else if (type == ThrowType.SINGLE) {
			  points = pts;
		  }
		  
		  else if (type == ThrowType.DOUBLE) {
			  points = pts * 2;
		  }
		  
		  else if (type == ThrowType.TRIPLE) {
			  points = pts * 3;
		  }
		  
		  else if (type == ThrowType.OUTER_BULLSEYE) {
			  points = 25;
		  }
		  
		  else if (type == ThrowType.INNER_BULLSEYE) {
			  points = 50;
		  }
		
		return points;
	}
	
	/** 
	 * 
	 * @return
	 */
	public int getCurrentPlayer() {
		return currPlayer;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getDartCount() {
		return dartsLeft;
	}
	
	/**
	 * 
	 * @param which the player who's score we want
	 * @return the score of the player
	 */
	public int getScore(int which) {
		
		if (which == 0) {
			return player0Score;
		}
		return player1Score;
	}
	
	/**
	 * Function that will return true if the game has ended
	 * @return if the game is over
	 */
	public boolean isOver() {
		
		boolean temp0 = (player0Score == 0);
		boolean temp1 = (player1Score == 0);
		
		return !(temp0 || temp1);
	}
	
	/**
	 * Function that represents a dart being thrown. Inputs the shot type and value and uses method calcPoints to 
	 * calculate the correct amount of points and remove them from the player's score
	 * @param type the type of shot, miss, single, etc
	 * @param pts amount of points the shot is worth
	 */
	public void throwDart(ThrowType type, int pts) {
		int points = calcPoints(type, pts);
		
		// Only proceed is game is not over
		if (isOver()) {
			
			// have to double in
			if ( (getScore(currPlayer) == startPoints) && (type != ThrowType.DOUBLE)) {
				// remove a dart and kill the function
				dartsLeft --;
				return;
			}
			
			// in order to throw:
			// score over 1
			// have darts
			if ( (getScore(currPlayer) > 2) && (dartsLeft > 0) ) {
				
				// check for bust
				if (getScore(currPlayer) - points == 1) {
					// if true, the player has busted. remove a dart and kill function without changing points for the turn
					points = preTurnPoints;
					dartsLeft --;
					return;
				}
			
				
				// now find out whos turn it is
				if (currPlayer == 0) {
					// make sure player does not bust
					if (player0Score - points >= 1) {
						player0Score -= points;
					}
					// else bust
				}
				else {
					
					if (player1Score - points >= 1) {
						player1Score -= points;
					}
				}
				
			}
			
		}
		
		if (dartsLeft > 1) {
			dartsLeft --;
		}
		else if (dartsLeft == 1) {
			changeTurns();
		}
		
	}
	
	/**
	 * Function that returns the player who won
	 * @return player that won, either 0 or 1
	 */
	public int whoWon() {
		return 0;
	}
	
	/**
	 * Helper function that resets the darts to the dart count and switches the current player
	 * It also resets the current points
	 */
	public void changeTurns() {
		dartsLeft = dartCount;
		
		if (currPlayer == 0) {
			currPlayer = 1;
			preTurnPoints = player1Score;
		}
		else {
			currPlayer = 0;
			preTurnPoints = player0Score;
		}
	}
	
  
  /**
   * Returns a string representation of the current game state.
   */
  public String toString()
  {
    String result = "Player 0: " + getScore(0) +
                    "  Player 1: " + getScore(1) +
                    "  Current: Player " + getCurrentPlayer() +
                    "  Darts: " + getDartCount();
    return result;
  }
  
}