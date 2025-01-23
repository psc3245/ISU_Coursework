package edu.iastate.cs228.hw1;

/**
 *  
 * @author peter.collins
 *
 */

/** 
 * Empty squares are competed by various forms of life.
 */
public class Empty extends Living 
{

	Plain currPlain;
	int row;
	int col;

	public Empty (Plain p, int r, int c) 
	{
		super.plain = p;
		currPlain = p;
		row = r;
		col = c;

	}

	public State who()
	{
		return State.EMPTY; 
	}

	/**
	 * An empty square will be occupied by a neighboring Badger, Fox, Rabbit, or Grass, or remain empty. 
	 * @param pNew     plain of the next life cycle.
	 * @return Living  life form in the next cycle.   
	 */
	public Living next(Plain pNew)
	{
		
		int[] population = new int[NUM_LIFE_FORMS];
		census(population);
		
		if (population[Living.RABBIT] > 1) {
			pNew.grid[row][col] = new Rabbit(pNew, row, col, 0);
		}
		else if (population[Living.FOX] > 1) {
			pNew.grid[row][col] = new Fox(pNew, row, col, 0);
		}
		else if (population[Living.BADGER] > 1) {
			pNew.grid[row][col] = new Badger(pNew, row, col, 0);
		}
		else if (population[Living.GRASS] > 1) {
			pNew.grid[row][col] = new Grass(pNew, row, col);
		}
		else {
			// do nothing, remain empty
			plain = pNew;
			pNew.grid[row][col] = new Empty(pNew, row, col);
		}
		return pNew.grid[row][col];
		
		
		// See Living.java for an outline of the function. 
		// See the project description for corresponding survival rules. 
	}

	// To be implemented in the classes Badger, Empty, Fox, Grass, and Rabbit. 
	// 
	// For each class (life form), carry out the following: 
	// 
	// 1. Obtains counts of life forms in the 3x3 neighborhood of the class object. 
	//
	// 2. Applies the survival rules for the life form to determine the life form  
	//    (on the same square) in the next cycle.  These rules are given in the  
	//    project description. 
	// 
	// 3. Generate this new life form at the same location in the plain pNew. 
}
