package edu.iastate.cs228.hw1;

/**
 *  
 * @author peter.collins
 *
 */

/**
 * 
 * Living refers to the life form occupying a square in a plain grid. It is a 
 * superclass of Empty, Grass, and Animal, the latter of which is in turn a superclass
 * of Badger, Fox, and Rabbit. Living has two abstract methods awaiting implementation. 
 *
 */
public abstract class Living 
{
	protected Plain plain; // the plain in which the life form resides
	protected int row;     // location of the square on which 
	protected int column;  // the life form resides

	// constants to be used as indices. 
	protected static final int BADGER = 0; 
	protected static final int EMPTY = 1; 
	protected static final int FOX = 2; 
	protected static final int GRASS = 3; 
	protected static final int RABBIT = 4; 

	public static final int NUM_LIFE_FORMS = 5; 

	// life expectancies 
	public static final int BADGER_MAX_AGE = 4; 
	public static final int FOX_MAX_AGE = 6; 
	public static final int RABBIT_MAX_AGE = 3; 


	/**
	 * Censuses all life forms in the 3 X 3 neighborhood in a plain. 
	 * @param population  counts of all life forms
	 */
	protected void census(int population[ ])
	{		
		// 
		// Count the numbers of Badgers, Empties, Foxes, Grasses, and Rabbits  
		// in the 3x3 neighborhood centered at this Living object.  Store the 
		// counts in the array population[] at indices 0, 1, 2, 3, 4, respectively. 

		// Get the Neighborhood
		Living[][] arr = getNeighborhood(plain.grid, row, column);

		// Count the number of organisms in the neighborhood
		for (int i = 0; i < 3; i++) {
			for (Living l : arr[i]) {
				
				if (l == null) {
					// do nothing, have to check for this first
				}
				else if (l.who() == State.BADGER) {
					population[0] ++;
				}
				else if (l.who()  == State.EMPTY) {
					population[1] ++;
				}
				else if (l.who()  == State.FOX) {
					population[2] ++;
				}
				else if (l.who()  == State.GRASS) {
					population[3] ++;
				}
				else if (l.who() == State.RABBIT) {
					population[4] ++;
				}
				else {
					// null, do nothing
				}
			}
		}
	}

	/**
	 * Gets the identity of the life form on the square.
	 * @return State
	 */
	public abstract State who();
	// To be implemented in each class of Badger, Empty, Fox, Grass, and Rabbit. 
	// 
	// There are five states given in State.java. Include the prefix State in   
	// the return value, e.g., return State.Fox instead of Fox.  

	/**
	 * Determines the life form on the square in the next cycle.
	 * @param  pNew  plain of the next cycle
	 * @return Living 
	 */
	public abstract Living next(Plain pNew); 
	// To be implemented in the classes Badger, Empty, Fox, Grass, and Rabbit. 
	// 
	// For each class (life form), carry out the following: 
	// 
	// 1. Obtains counts of life forms in the 3x3 neighborhood of the class object. 

	// 2. Applies the survival rules for the life form to determine the life form  
	//    (on the same square) in the next cycle.  These rules are given in the  
	//    project description. 
	// 
	// 3. Generate this new life form at the same location in the plain pNew. 

	
	// helper method for census
	private static Living[][] getNeighborhood(Living[][] arr, int row, int col) {

		int rowMax = arr[0].length - 1;
		int colMax = arr.length - 1;

		Living[][] returnArr = new Living[3][3];

		// Best Case **Works Perfectly**
		if (row < rowMax && col < colMax) {
			if (row > 0 && col > 0) {
				for (int i = -1; i < 2; i++) {
					for (int j = -1; j < 2; j++) {
						returnArr[i+1][j+1] = arr[row + i][col + j];
					}
				}
			}
		}

		// Edge Cases Part 1 - Left and Top Side

		if (row < rowMax && col < colMax) {
			if (col < 2 && row > 1) {
				for (int i = -1; i < 2; i++) {
					for (int j = 0; j < 2; j++) {
						returnArr[i+1][j+1] = arr[row + i][col + j];
					}
				}
			}
			else if (row < 2 && col > 1) {
				for (int i = 0; i < 2; i++) {
					for (int j = -1; j < 2; j++) {
						returnArr[i+1][j+1] = arr[row + i][col + j];
					}
				}
			}
			else {
				for (int i = 0; i < 2; i++) {
					for (int j = 0; j < 2; j++) {
						returnArr[i+1][j+1] = arr[row + i][col + j];
					}
				}
			}
		}

		// Edge Cases Part 2 - Right and Bottom Side

		if (row > 1 && col > 1) {
			if (col == colMax && row < rowMax) {
				for (int i = -1; i < 2; i++) {
					for (int j = -1; j < 1; j++) {
						returnArr[i+1][j+1] = arr[row + i][col + j];
					}
				}
			}
			else if (col < colMax && row == rowMax) {
				for (int i = -1; i < 1; i++) {
					for (int j = -1; j < 2; j++) {
						returnArr[i+1][j+1] = arr[row + i][col + j];
					}
				}
			}
			else {
				for (int i = -1; i < 1; i++) {
					for (int j = -1; j < 1; j++) {
						returnArr[i+1][j+1] = arr[row + i][col + j];
					}
				}
			}
		}


		return returnArr;
	}



}
