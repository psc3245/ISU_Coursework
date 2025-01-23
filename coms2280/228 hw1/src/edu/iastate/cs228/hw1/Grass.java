package edu.iastate.cs228.hw1;

/**
 *  
 * @author peter.collins
 *
 */
/**
 * Grass remains if more than rabbits in the neighborhood; otherwise, it is eaten. 
 *
 */
public class Grass extends Living 
{

	Plain currPlain;
	int row;
	int col;

	public Grass (Plain p, int r, int c) 
	{
		super.plain = p;
		currPlain = p;
		row = r;
		col = c;
	}

	public State who()
	{
		return State.GRASS; 
	}

	/**
	 * Grass can be eaten out by too many rabbits. Rabbits may also multiply fast enough to take over Grass.
	 */
	public Living next(Plain pNew)
	{

		int[] population = new int[NUM_LIFE_FORMS];
		census(population);


		if (population[Living.RABBIT] * 3 > population[Living.GRASS]) {
			// empty, do nothing
			pNew.grid[row][col] = new Empty(pNew, row, col);
		}
		else if (population[Living.RABBIT] > 3) {
			pNew.grid[row][col] = new Rabbit(pNew, row, col, 0);
		}
		else {
			// do nothing
			pNew.grid[row][col] = new Grass(pNew, row, col);
		}
		
		return pNew.grid[row][col];


		// See Living.java for an outline of the function. 
		// See the project description for the survival rules for grass. 

	}


}
