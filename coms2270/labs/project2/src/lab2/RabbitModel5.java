package lab2;
import java.util.random.*;

/**
 * A RabbitModel is used to simulate the growth
 * of a population of rabbits. 
 */
public class RabbitModel5
{
  // TODO - add instance variables as needed
	
	private int numRabbits;
	private int yearsPassed;
  
  /**
   * Constructs a new RabbitModel.
   */
  public RabbitModel5()
  {
    // TODO
	  numRabbits = 2;
	  yearsPassed = 0;
  }  
 
  /**
   * Returns the current number of rabbits.
   * @return
   *   current rabbit population
   */
  public int getPopulation()
  {
    // TODO - returns a dummy value so code will compile
    return numRabbits;
  }
  
  /**
   * Updates the population to simulate the
   * passing of one year.
   */
  public void simulateYear()
  {
    // TODO
	  double rand = Math.random();
	  rand *= 9;
	  int randInt = (int)rand;
	  numRabbits += randInt;
	  
  }
  
  /**
   * Sets or resets the state of the model to the 
   * initial conditions.
   */
  public void reset()
  {
    // TODO
	  numRabbits = 2;
  }
}
