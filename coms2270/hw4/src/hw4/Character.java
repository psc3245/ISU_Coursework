package hw4;


import api.Actor;
import api.Descriptor;
import api.Direction;
import api.Location;
import api.MazeMap;
import api.Mode;

/**
 * Generic character class for pacman game. Makes PacMan and all ghosts.
 * @author petercollins
 *
 */
public abstract class Character implements Actor {

	/**
	 * Margin of error for comparing exact position to centerline
	 * of cell.
	 */
	public static final double ERR = .001;

	/**
	 * Initial location on reset().
	 */
	private Location home;

	/**
	 * Initial direction on reset().
	 */
	private Direction homeDirection;

	/**
	 * Current direction of travel.
	 */
	private Direction currentDirection;

	/**
	 * Basic speed increment, used to determine currentIncrement.
	 */
	private double baseIncrement;

	/**
	 * Current speed increment, added in direction of travel each frame.
	 */
	private double currentIncrement;

	/**
	 * Row (y) coordinate, in units of cells.  The row number for the
	 * currently occupied cell is always the int portion of this value.
	 */
	private double rowExact;

	/**
	 * Column (x) coordinate, in units of cells.  The column number for the
	 * currently occupied cell is always the int portion of this value.
	 */
	private double colExact;

	/**
	 * Flag indicating that the player is in "turning" mode, that is, 
	 * moving on a diagonal in a new direction of travel and simultaneously in the 
	 * previous direction of travel.
	 */

	/**
	 * Constructor for generic Character class. Intializes all the variables, thats it.
	 * @param maze maze map of the game
	 * @param home home location of character
	 * @param baseSpeed speed at which character travels
	 * @param homeDirection direction of home location
	 */
	public Character(MazeMap maze, Location home, double baseSpeed, Direction homeDirection) {
		this.home = home;
		this.baseIncrement = baseSpeed;
		this.currentIncrement = baseSpeed;
		this.homeDirection = homeDirection;
	}

	/**
	 * @return base speed of character
	 */
	@Override
	public double getBaseIncrement()
	{
		return baseIncrement;
	} 

	/**
	 * @return exact column of characters
	 */
	@Override
	public double getColExact()
	{
		return colExact;
	}

	/**
	 * @return returns current direction character is traveling
	 */
	@Override
	public Direction getCurrentDirection()
	{
		return currentDirection;
	}

	/**
	 * @return current increment of character
	 */
	@Override
	public double getCurrentIncrement()
	{
		return currentIncrement;
	}

	/**
	 * @return location of character in the maze
	 */
	@Override
	public Location getCurrentLocation()
	{
		return new Location((int) rowExact, (int) colExact);
	}

	/**
	 * @return the direction home is
	 */
	@Override
	public Direction getHomeDirection()
	{
		return homeDirection;
	}

	/**
	 * @return home location for character
	 */
	@Override
	public Location getHomeLocation()
	{
		return home;
	}

	/**
	 * @return exact row of character
	 */
	@Override
	public double getRowExact()
	{
		return rowExact;
	}

	/**
	 * Resets the character to it's home location and speed
	 */
	@Override
	public void reset() {
		Location homeLoc = getHomeLocation();
		setRowExact(homeLoc.row() + 0.5);
		setColExact(homeLoc.col() + 0.5);
		setDirection(getHomeDirection());
		currentIncrement = getBaseIncrement();
	}
	
	/**
	 * sets exact column to give value
	 * @param c exact column
	 */
	@Override
	public void setColExact(double c)
	{
		colExact = c;
	}

	/**
	 * sets the current direction to the given intended direction
	 * @param intended direction
	 */
	@Override
	public void setDirection(Direction dir)
	{
		currentDirection = dir;
	}
	
	/**
	 * sets exact row to give value
	 * @param c exact row
	 */
	@Override
	public void setRowExact(double r) {
		rowExact = r;
	}

	
	/**
	 * sets the mode of the character. does nothing in pacman.
	 */
	public abstract void setMode(Mode mode, Descriptor desc);

	/**
	 * updates all characters based on the given description of the maze
	 */
	public abstract void update(Descriptor d);

	/**
	 * figures out the next best cell for a character to go to. not applicable for pacman.
	 * @param d
	 */
	public abstract void calculateNextCell(Descriptor d);

	/**
	 * Determines the difference between current position and center of 
	 * current cell, in the direction of travel.
	 */
	public double distanceToCenter()
	{
		double colPos = getColExact();
		double rowPos = getRowExact();
		switch (getCurrentDirection())
		{
		case LEFT:
			return colPos - ((int) colPos) - 0.5;
		case RIGHT:
			return 0.5 - (colPos - ((int) colPos));
		case UP:
			return rowPos - ((int) rowPos) - 0.5;
		case DOWN:
			return 0.5 - (rowPos - ((int) rowPos));
		}    
		return 0;
	}






}
