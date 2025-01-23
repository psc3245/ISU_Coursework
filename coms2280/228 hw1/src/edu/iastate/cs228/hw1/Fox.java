package edu.iastate.cs228.hw1;

/**
 *  
 * @author peter.collins
 *
 */

/**
 * A fox eats rabbits and competes against a badger. 
 */
public class Fox extends Animal 
{
	Plain currPlain;
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
	public Fox (Plain p, int r, int c, int a) 
	{
		super.plain = p;
		currPlain = p;
		row = r;
		col = c;
		super.age = a;
	}

	/**
	 * A fox occupies the square. 	 
	 */
	public State who()
	{
		return State.FOX; 
	}

	/**
	 * A fox dies of old age or hunger, or from attack by numerically superior badgers. 
	 * @param pNew     plain of the next cycle
	 * @return Living  life form occupying the square in the next cycle. 
	 */
	public Living next(Plain pNew)
	{
		// 

		int[] population = new int[NUM_LIFE_FORMS];
		census(population);


		// if fox is age 6, kill it
		if (age == Living.FOX_MAX_AGE) {

			pNew.grid[row][col] = new Empty(pNew, row, col);
		}

		else if (population[Living.BADGER] < population[Living.FOX]) {
			pNew.grid[row][col] = new Badger(pNew, row, col, 0);
		}
		else if (population[Living.FOX] + population[Living.BADGER] > population[Living.RABBIT]) {
			pNew.grid[row][col] = new Empty(pNew, row, col);
		}
		else {
			// do nothing, increment age
			pNew.grid[row][col] = new Fox(pNew, row, col, age + 1);
		}
		
		return pNew.grid[row][col];


		// See Living.java for an outline of the function. 
		// See the project description for the survival rules for a fox. 
	}
}
