package hw4;

import static api.Direction.DOWN;
import static api.Direction.LEFT;
import static api.Direction.RIGHT;
import static api.Direction.UP;

import java.util.ArrayList;
import java.util.Random;

import api.Descriptor;
import api.Direction;
import api.Location;
import api.MazeMap;
import api.Mode;

/**
 * Ghosts in pacman. Parent class for Blinky, Inky, Pinky and Clyde.
 * @author petercollins
 *
 */
public abstract class Ghost extends Character {
	
	/**
	 * Margin of error for comparing exact position to centerline
	 * of cell.
	 */
	public static final double ERR = .001;

	/**
	 * Maze configuration.
	 */
	private MazeMap maze;

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
	 * mode for blinky
	 */
	private Mode mode;

	/**
	 * next location blinky is traveling to
	 */
	private Location nextLoc;
	/**
	 * direction blinky is traveling in
	 */
	private Direction nextDir;

	/**
	 * Random seed for blinky
	 */
	private Random rand;

	/**
	 * the location blinky goes to when he is in scatter mode
	 */
	private Location scatterTarget;


	/**
	 * Constructs a new Blinky with the given maze, home location, base speed,
	 * and initial direction.
	 * @param maze
	 *   maze configuration
	 * @param home
	 *   initial location
	 * @param baseSpeed
	 *   base speed increment
	 * @param homeDirection
	 *   initial direction
	 */

	public Ghost(MazeMap maze, Location home, double baseSpeed, Direction homeDirection, Location scatterTarget,
			Random rand) {

		super(maze, home, baseSpeed, homeDirection);
		this.maze = maze;
		this.home = home;
		this.baseIncrement = baseSpeed;
		this.currentIncrement = baseSpeed;
		this.homeDirection = homeDirection;
		this.rand = rand;
		this.mode = Mode.INACTIVE;
		this.scatterTarget = scatterTarget;
		
		currentDirection = UP;

	}
	
	public Direction getNextDirection() {
		return nextDir;
	}


	@Override
	public double getBaseIncrement()
	{
		return baseIncrement;
	} 

	@Override
	public double getColExact()
	{
		return colExact;
	}

	@Override
	public Direction getCurrentDirection()
	{
		return currentDirection;
	}
	
	public Location getScatterTarget() {
		return scatterTarget;
	}

	@Override
	public double getCurrentIncrement()
	{
		return currentIncrement;
	}

	@Override
	public Location getCurrentLocation()
	{
		return new Location((int) rowExact, (int) colExact);
	}
	
	public MazeMap getMaze() {
		return maze;
	}

	@Override
	public Direction getHomeDirection()
	{
		return homeDirection;
	}

	@Override
	public Location getHomeLocation()
	{
		return home;
	}

	@Override
	public Mode getMode() {
		return mode;
	}

	@Override
	public double getRowExact()
	{
		return rowExact;
	}

	@Override
	public void reset() {
		Location homeLoc = getHomeLocation();
		setRowExact(homeLoc.row() + 0.5);
		setColExact(homeLoc.col() + 0.5);
		setDirection(getHomeDirection());
		currentIncrement = getBaseIncrement();
		mode = Mode.INACTIVE;
	}

	@Override
	public void setColExact(double c)
	{
		colExact = c;
	}

	@Override
	public void setDirection(Direction dir)
	{
		currentDirection = dir;
	}

	@Override
	public void setMode(Mode mode, Descriptor desc) {
		// update mode
		this.mode = mode;

		// update the currentIncrement based on the mode
		switch (mode) {
		case FRIGHTENED:
			currentIncrement = getBaseIncrement() * (2/3);
			break;
		case DEAD:
			currentIncrement = getBaseIncrement() * 2;
			break;
		default:
			currentIncrement = getBaseIncrement();
			break;
		}

		// call calculate next cell
		calculateNextCell(desc);
	}

	@Override
	public void setRowExact(double r) {
		rowExact = r;
	}

	protected abstract Location getTarget(Descriptor d);

	@Override
	public void calculateNextCell(Descriptor d) {
		if (mode == Mode.INACTIVE || mode == null) {
			return;
		}
		
		if (distanceToCenter() < 0 - ERR) {
			return;
		}

	
		
		// make an array list of all the possible locations (adjacent cells)
		ArrayList<Location> locs = new ArrayList<>();
		locs.add(new Location((int)getCurrentLocation().row() - 1, (int) getCurrentLocation().col()));
		locs.add(new Location((int)getCurrentLocation().row(), (int) getCurrentLocation().col() - 1));
		locs.add(new Location((int)getCurrentLocation().row() + 1, (int) getCurrentLocation().col()));
		locs.add(new Location((int)getCurrentLocation().row(), (int) getCurrentLocation().col() + 1));
		
		
		// remove the walls
		for (int i = 0; i < locs.size(); i++) {
			if ( maze.isWall(locs.get(i).row(), locs.get(i).col()) ) {
				locs.set(i, null);
			}
		}
		
		// if its frightened, handle specially
		
		if (mode.equals(Mode.FRIGHTENED)) {
			// remove the nulls
			for (int i = 0; i < locs.size(); i++) {
				if (locs.get(i) == null) {
					locs.remove(i);
					i--;
				}
				
			}
			
			// get a random number 
			int randInt = rand.nextInt(locs.size());
			
			// set nextLoc to the random cell we selected
			nextLoc = locs.get(randInt);
			
			
		}
		
		
		else {
			
			// use helper method to get our target location
			Location targetCell = getTarget(d);
			
			// set impossible cells to null
			switch (currentDirection) {
			case UP:
				locs.set(2, null);
				break;
			case LEFT:
				locs.set(3, null);
				break;
			case DOWN:
				locs.set(0, null);
				break;
			case RIGHT:
				locs.set(1, null);
				break;
			}
			// if its null, it is impossible
			
			ArrayList<Location> lowestDist = new ArrayList<>();
			
			double min = Integer.MAX_VALUE;
			
			// iterate through locs
			for (int i = 0; i < locs.size(); i++) {
				// if it isn't null
				if (locs.get(i) != null) {
					// get the distance
					double dist = getDistance(targetCell.row(), getCurrentLocation().row(), 
							targetCell.col(), getCurrentLocation().col());
					
					// if distance is lower than the minimum, clear the list
					if (dist < min) {
						min = dist;
						lowestDist.clear();
						lowestDist.add(locs.get(i));
					}
					// if they are the same, leave min and don't clear
					if (dist == min) {
						lowestDist.add(locs.get(i));
					}
				}
				
			}
			
			// return the first cell (they are already in priority order from when locs was made)
			nextLoc = lowestDist.get(0);
		}
		
		// get direction based on nextLoc
		
		if (getCurrentLocation().row() < nextLoc.row()) {
			nextDir = DOWN;
		}
		else if (getCurrentLocation().row() > nextLoc.row()) {
			nextDir = UP;
		}
		
		else if (getCurrentLocation().col() < nextLoc.col()) {
			nextDir = LEFT;
		}
		else if (getCurrentLocation().col() > nextLoc.col()) {
			nextDir = RIGHT;
		}
		
	}
	
	private double getDistance(double x1, double x2, double y1, double y2) {

		return Math.sqrt( (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) );

	}



	@Override
	public abstract void update(Descriptor d);

}
