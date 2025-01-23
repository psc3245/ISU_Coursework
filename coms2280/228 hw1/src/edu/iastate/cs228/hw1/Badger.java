package edu.iastate.cs228.hw1;

/**
 *  
 * @author peter.collins
 *
 */


/**
 * A badger eats a rabbit and competes against a fox. 
 */
public class Badger extends Animal
{

	Plain plain;
	int row;
	int col;
	int age;
	/**
	 * Constructor 
	 * @param p: plain
	 * @param r: row position 
	 * @param c: column position
	 * @param a: age 
	 */
	public Badger (Plain p, int r, int c, int a) 
	{
		super.plain = p;
		row = r;
		col = c;
		super.age = a;
	}

	/**
	 * A badger occupies the square. 	 
	 */
	public State who()
	{
		return State.BADGER; 
	}
	

	/**
	 * A badger dies of old age or hunger, or from isolation and attack by a group of foxes. 
	 * @param pNew     plain of the next cycle
	 * @return Living  life form occupying the square in the next cycle. 
	 */
	public Living next(Plain pNew)
	{
		int[] population = new int[NUM_LIFE_FORMS];
		census(population);
		
		// if badger is age 4, kill it
		if (age == Living.BADGER_MAX_AGE) {
			
			pNew.grid[row][col] = new Empty(pNew, row, col);
		}

		else if (population[Living.BADGER] < 2 && population[Living.FOX] > 1) {
			pNew.grid[row][col] = new Fox(pNew, row, col, 0);
		}
		else if (population[Living.FOX] + population[Living.BADGER] > population[Living.RABBIT]) {
			pNew.grid[row][col] = new Empty(pNew, row, col);
		}
		else {
			// do nothing, increment age
			pNew.grid[row][col] = new Badger(pNew, row, col, age + 1);
		}
		
		return pNew.grid[row][col];


		// 
		// See Living.java for an outline of the function. 
		// See the project description for the survival rules for a badger. 
	}
}
