package hw4;

import java.util.ArrayList;
import java.util.Random;
import static api.Direction.DOWN;
import static api.Direction.LEFT;
import static api.Direction.RIGHT;
import static api.Direction.UP;

import api.Descriptor;
import api.Direction;
import api.Location;
import api.MazeMap;
import api.Mode;

/**
 * Blinky ghost in PacMan.
 * @author petercollins
 * Design Choices paragraph:
 * I chose to use two abstract classes to simplify the ghosts and connect them to pacman. I also made a helper method called
 * get target. This is useful because the target is the only thing that changes from ghost to ghost. This is useful because
 * it limits the amount of changes I have to make from ghost to ghost.
 */
public class Inky extends Ghost {

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

	public Inky(MazeMap maze, Location home, double baseSpeed, Direction homeDirection, Location scatterTarget,
			Random rand) {

		super(maze, home, baseSpeed, homeDirection, scatterTarget, rand);
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

	/**
	 * figures out the next best cell for a character to go to. not applicable for pacman.
	 * @param d
	 */
	@Override
	public void update(Descriptor d) {


		if (mode == Mode.INACTIVE || mode == null) {
			return;
		}

		if (getCurrentDirection() == null)
		{
			return;
		}


		double increment = getCurrentIncrement();
		double curRowExact = getRowExact();
		double curColExact = getColExact();
		int rowNum = (int) curRowExact;
		int colNum = (int) curColExact;   

		// distance to center of cell we are in, in the direction of travel, may be negative    
		double diff = distanceToCenter();


		switch(getCurrentDirection())
		{     
		// tricky bit: if we are approaching a wall, adjust increment if needed,
		// so we end up in the center of the cell

		case RIGHT:
			// special case: check whether we are in the tunnel and need to wrap around
			if (curColExact - increment - 0.5 < 0)
			{
				curColExact = maze.getNumColumns() + (curColExact - increment - 0.5);
			}
			else
			{
				// if we are approaching a wall, be sure we stop moving
				// at the center of current cell.  This only applies when
				// 'diff' is positive but small enough that we can't move a full
				// increment
				if (diff > -ERR && diff < increment && maze.isWall(rowNum, colNum - 1))
				{
					increment = diff;
				}
				curColExact -= increment;         
			}
			break;   

		case LEFT:
			// special case: check whether we are in the tunnel and need to wrap around
			if (curColExact + increment + 0.5 >= maze.getNumColumns())
			{
				curColExact = curColExact + increment + 0.5 - maze.getNumColumns();
			}
			else
			{
				if (diff > -ERR && diff < increment && maze.isWall(rowNum, colNum + 1))
				{
					increment = diff;
				}
				curColExact += increment;
			}
			break;    

		case UP:
			if (diff > -ERR && diff < increment && maze.isWall(rowNum - 1, colNum))
			{
				increment = diff;
			}
			curRowExact -= increment;       
			break;

		case DOWN:
			if (diff > -ERR && diff < increment && maze.isWall(rowNum + 1, colNum))
			{
				increment = diff;
			}
			curRowExact += increment;
			break;
		}

		diff = distanceToCenter();

		if (nextDir != getCurrentDirection()) {
			// center the ghost
			switch(nextDir)
			{     

			case RIGHT:
				if (diff > -ERR && diff < increment)
				{
					curRowExact = (int)getRowExact() + 0.5;

				}   
				break;    

			case LEFT:
				if (diff > -ERR && diff < increment)
				{
					curRowExact = (int)getRowExact() + 0.5;

				}   
				break;  

			case UP:
				if (diff > -ERR && diff < increment)
				{
					curColExact = (int)getColExact() + 0.5;

				}   
				break;

			case DOWN:
				if (diff > -ERR && diff < increment)
				{
					curColExact = (int)getColExact() + 0.5;

				}   
				break;
			}

		}

		setRowExact(curRowExact);
		setColExact(curColExact);


		diff = distanceToCenter();

		if (diff + ERR > 0 && diff - ERR < 0) {
			currentDirection = nextDir;
		}


		if (getCurrentLocation().equals(nextLoc)) {
			calculateNextCell(d);
		}


	}

	public Location getNextCell() {
		return nextLoc;
	}

	/**
	 * Determines the difference between current position and center of 
	 * current cell, in the direction of travel.
	 */
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

		// ghost is not frightened
		if (!mode.equals(Mode.FRIGHTENED)) {

			switch(super.getCurrentDirection()) {

			case UP:

				locs.remove(2);

				break;

			case DOWN:

				locs.remove(0);

				break;

			case RIGHT:

				locs.remove(1);

				break;

			case LEFT:

				locs.remove(3);

				break;

			}


		}

		for(int i = 0; i < locs.size(); i++) {

			if(maze.isWall(locs.get(i).row(), locs.get(i).col())) {
				locs.remove(i);
				i --;
			}
		}

		if(mode.equals(Mode.FRIGHTENED)) {

			int temp = rand.nextInt(locs.size());

			nextLoc = locs.get(temp);

			// case up
			if(nextLoc.row() - getCurrentLocation().row() == -1) {
				temp = 3;
			}

			// case down
			if(nextLoc.row() - getCurrentLocation().row() == 1) {
				temp = 2;
			}

			// case left
			if(nextLoc.col() - getCurrentLocation().col() == -1) {
				temp = 1;
			}

			// case right
			if(nextLoc.col() - getCurrentLocation().col() == 1) {
				temp = 0;
			}

			switch(temp) {

			case 0:
				nextDir = RIGHT;
				break;
			case 1:
				nextDir = LEFT;
				break;
			case 2:
				nextDir = DOWN;
				break;
			case 3:
				nextDir = UP;
				break;
			default:
				nextDir = UP;
				break;
			}

		}

		else {

			//now we compare the locations to see which is closest to our target cell
			
			Location target = getTarget(d);

			int tRow = target.row();

			int tCol = target.col();

			double min = Integer.MAX_VALUE;
			
			ArrayList<Location> minLocs = new ArrayList<Location>();

			minLocs.add(locs.get(0));

			// add all locations that have a distance equal to to the lowest distance in the list

			// if a new location has a lower distance clear the list and and add the new loc

			for(int i = 0; i< locs.size(); i++) {

				int possRow = locs.get(i).row();

				int possCol = locs.get(i).col();

				double dist = getDistance(tRow, possRow, tCol, possCol);

				if(dist < min-ERR) {

					minLocs.clear();

					min = dist;

					minLocs.add(locs.get(i));

				}

				else if(dist + ERR > min && dist - ERR < min) {

					minLocs.add(locs.get(i));

				}

			}

			
			int[] pList = new int[minLocs.size()];

			for(int i =0; i<minLocs.size(); i++) {

				// going up

				if(minLocs.get(i).row() - getCurrentLocation().row() == -1) {

					pList[i] = 4;

				}

				// going down

				if(minLocs.get(i).row() - getCurrentLocation().row() == 1) {

					pList[i] = 2;

				}

				// going left

				if(minLocs.get(i).col() - getCurrentLocation().col() == -1 ) {

					pList[i] = 3;

				}

				// going right

				if(minLocs.get(i).col() - getCurrentLocation().col() == 1) {

					pList[i] = 1;

				}

			}

		
			int pMax = -1;

			int index= -1;


			for(int i = 0; i < pList.length; i++ ) {

				if (pList[i] > pMax) {

					pMax = pList[i];

					index = i;

				}

			}



			nextLoc = minLocs.get(index);

			int tempNum = pList[index];

			switch(tempNum) {

			case 1:

				nextDir = RIGHT;

				break;

			case 2:

				nextDir = DOWN;

				break;

			case 3:

				nextDir = LEFT;

				break;

			case 4:

				nextDir = UP;

				break;

			}

		}


	}
	
	/**
	 * helper method that returns the location blinky is trying to reach
	 */
	@Override
	protected Location getTarget(Descriptor d) {

		if (mode == Mode.CHASE) {
			int pRow = d.getPlayerLocation().row();
			// 2 in front of pac man
			int pCol = d.getPlayerLocation().col() + 2;
			
			int bRow = d.getBlinkyLocation().row();
			int bCol = d.getBlinkyLocation().col();
			
			double pManSlope = (bRow - pRow) / (bCol - pCol);
			double yIntercept = pRow - (pCol * pManSlope);
			
			double a = Math.sin(30) * getDistance(pRow, bRow, pCol, bCol) * 2;
			
			int row;
			if (bRow > pRow) {
				row = bRow - (int)a;
			}
			else {
				row = bRow + (int)a;
			}
			
			int col = (int)(pManSlope * a + yIntercept);
			
			return new Location(row, col);
		}

		else if (mode == Mode.DEAD) {
			return home;
		}

		else if  (mode == Mode.SCATTER) {
			return scatterTarget;
		}

		return null;
	}

	/**
	 * helper method that does the distance formula
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @return
	 */
	private double getDistance(double x1, double x2, double y1, double y2) {

		return Math.sqrt( (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) );

	}

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

