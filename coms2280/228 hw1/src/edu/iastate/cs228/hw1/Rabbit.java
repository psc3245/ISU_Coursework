package edu.iastate.cs228.hw1;

/**
 *  
 * @author peter.collins
 *
 */

/*
 * A rabbit eats grass and lives no more than three years.
 */
public class Rabbit extends Animal 
{	
	Plain currPlain;
	int row;
	int col;
	int age;

	/**
	 * Creates a Rabbit object.
	 * @param p: plain  
	 * @param r: row position 
	 * @param c: column position
	 * @param a: age 
	 */
	public Rabbit (Plain p, int r, int c, int a) 
	{
		super.plain = p;
		currPlain = p;
		row = r;
		col = c;
		super.age = a;
	}

	// Rabbit occupies the square.
	public State who()
	{
		return State.RABBIT; 
	}

	/**
	 * A rabbit dies of old age or hunger. It may also be eaten by a badger or a fox.  
	 * @param pNew     plain of the next cycle 
	 * @return Living  new life form occupying the same square
	 */
	public Living next(Plain pNew)
	{

		int[] population = new int[NUM_LIFE_FORMS];
		census(population);


		// if rabbit is age 6, kill it
		if (age == Living.RABBIT_MAX_AGE) {
			pNew.grid[row][col] = new Empty(pNew, row, col);
		}

		else if (population[Living.GRASS] < 1) {
			pNew.grid[row][col] = new Badger(pNew, row, col, 0);
		}
		else if (population[Living.FOX] + population[Living.BADGER] > population[Living.RABBIT] 
				&& population[Living.FOX] > population[Living.BADGER]) {
			pNew.grid[row][col] = new Fox(pNew, row, col, 0);
		}
		else if (population[Living.FOX] + population[Living.BADGER] > population[Living.RABBIT] 
				&& population[Living.RABBIT] < population[Living.BADGER]) {
			pNew.grid[row][col] = new Badger(pNew, row, col, 0);
		}
		else {
			// do nothing, increment age
			pNew.grid[row][col] = new Rabbit(pNew, row, col, age + 1);
		}
		
		return pNew.grid[row][col];
		// 
		// See Living.java for an outline of the function. 
		// See the project description for the survival rules for a rabbit. 
	}
}
