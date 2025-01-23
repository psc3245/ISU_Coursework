package hw3;

import java.util.ArrayList;

import api.GridCell;
import api.CellType;
import api.Line;
import api.Location;
import api.StringUtil;

/**
 * Game state for a Lines game.
 */
public class LinesGame
{
	private Line currLine;

	private ArrayList<Line> lines;

	private GridCell[][] grid;

	private int moveCount;

	private GridCell currCell;


	/**
	 * Constructs a LinesGame from the given grid and Line list.
	 * This constructor does not do any error-checking to ensure
	 * that the grid and the Line array are consistent. Initially
	 * the current line is null.
	 * @param givenGrid
	 *   a 2d array of GridCell
	 * @param givenLines
	 *   list of Line objects
	 */
	public LinesGame(GridCell[][] givenGrid, ArrayList<Line> givenLines)
	{

		lines = givenLines;

		grid = givenGrid;

		currLine = null;

		moveCount = 0;

		currCell = null;

	}

	/**
	 * Constructs a LinesGame from the given descriptor. Initially the
	 * current line is null.
	 * @param descriptor
	 *   array of strings representing initial state
	 */
	public LinesGame(String[] descriptor)
	{
		// set current line to null
		currLine = null;

		// create grid
		int rows = descriptor.length;
		int columns = descriptor[0].length();

		grid = new GridCell[rows][columns];

		// iterate through descriptor
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {

				CellType t = CellType.OPEN;
				// endpoint
				if (Character.isUpperCase(descriptor[i].charAt(j))) {
					t = CellType.ENDPOINT;
				}
				else {
					t = CellType.OPEN;
				}

				// get the ID
				int id = StringUtil.getIdForCharacter( descriptor[i].charAt(j));

				// add the created cell to grid
				grid[i][j] = new GridCell(t, id);
			}
		}

		// use grid and function in util to create lines

		lines = Util.createLinesFromGrid(grid);

		// initalize remaining member variables
		moveCount = 0;
		currCell = null;


	}

	/**
	 * Returns the number of columns for this game.
	 * @return
	 *  width for this game
	 */ 
	public int getWidth()
	{
		return grid[0].length;
	}

	/**
	 * Returns the number of rows for this game.
	 * @return
	 *   height for this game
	 */ 
	public int getHeight()
	{
		return grid.length;
	}

	/**
	 * Returns the current cell for this game, possibly null.
	 * The current cell is just the last location, if any, 
	 * in the current line, if there is one. Returns null
	 * if the current line is null or if the current line
	 * has an empty list of locations.
	 * @return
	 *   current cell for this game, or null
	 *   
	 */
	public Location getCurrentLocation()
	{
		return getLoc(currCell);
	}

	/**
	 * Returns the id for the current line, or -1
	 * if the current line is null.
	 * @return
	 *   id for the current line
	 */
	public int getCurrentId()
	{
		if (currLine == null) {
			return -1;
		}

		return currLine.getId();
	}

	/**
	 * Return this game's current line (which may be null).
	 * @return
	 *   current line for this game
	 */
	public Line getCurrentLine()
	{
		return currLine;
	}

	/**
	 * Returns a reference to this game's grid.  Clients should
	 * not modify the array.
	 * @return
	 *   the game grid
	 */
	public GridCell[][] getGrid()
	{
		return grid;
	}

	/**
	 * Returns the grid cell at the given position.
	 * @param row
	 *   given row
	 * @param col
	 *   given column
	 * @return
	 *   grid cell at (row, col)
	 */
	public GridCell getCell(int row, int col)
	{
		return grid[row][col];
	}

	/**
	 * Returns all Lines for this game.  Clients should not modify
	 * the returned list or the Line objects.
	 * @return
	 *   list of lines for this game
	 */ 
	public ArrayList<Line> getAllLines()
	{
		return lines;
	}

	/**
	 * Returns the total number of moves.  A "move" means that a 
	 * new Location was successfully added to the current line
	 * in addCell.
	 * @return
	 *   total number of moves so far in this game
	 */
	public int getMoveCount()
	{
		return moveCount;
	}

	/**
	 * Returns true if all lines are connected and all
	 * cells are at their maximum count.
	 * @return
	 *   true if all lines are complete and all cells are at max
	 */ 
	public boolean isComplete()
	{
		for (Line l : lines) {
			if (!(l.isConnected())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Attempts to set the current line based on the given
	 * row and column.  When using a GUI, this method is typically 
	 * invoked when the mouse is pressed. If the current line is 
	 * already non-null, this method does nothing.
	 * There are two possibilities:
	 * <ul>
	 *   <li>Any endpoint can be selected.  Selecting an 
	 *   endpoint clears the line associated with that endpoint's id,
	 *   and all cells that were previously included in the line are decremented.
	 *   The line then becomes the current line, and the endpoint is incremented
	 *   and placed on the line's list of locations as its only element.
	 *   <li>A non-endpoint cell can be selected if it is not a crossing
	 *   and if it is the last cell in some line.  That line then becomes
	 *   the current line.
	 * </ul>
	 * If neither of the above conditions is met, or if the
	 * current line is non-null, this method does nothing.
	 * 
	 * @param row
	 *   given row
	 * @param col
	 *   given column
	 */
	public void startLine(int row, int col)
	{
		
		if (currLine != null) {
			return;
		}

		// get the cell  
		GridCell cell = grid[row][col];

		// check if the point is an endpoint
		if (cell.isEndpoint()) {
			
			currCell = cell;
			
			// iterate through lines to get line l
			for(int i = 0; i < lines.size(); i++) {

				// get the endpoints of the line
				Location loc0 = lines.get(i).getEndpoint(0);
				Location loc1 = lines.get(i).getEndpoint(1);

				// get our location
				Location ourLoc = new Location(row, col);

				// if our location matches either endpoint, set it to be the current line
				if (loc0.equals(ourLoc) || loc1.equals(ourLoc)) {
					
					// checklist
					
					// get the cells in the line
					ArrayList<Location> locs = lines.get(i).getCells();
					// loop through the cells and decrement
					for (Location l : locs) {
						int r = l.row();
						int c = l.col();
						grid[r][c].decrement();
					}
					
					// then incrememnt the end point
					grid[row][col].increment();
					
					// clear the old cells
					lines.get(i).clear();
					
					// set currLine to the line
					currLine = lines.get(i);
					
					// add the cell to currLine and the line in lines
					addCell(row, col);
					lines.get(i).add(new Location(row, col));
				}
			}
			
		}
		// otherwise
		else {
			// kill the function if line will cross
			if (Util.checkForPotentialCrossing(lines, getLoc(currCell), new Location(row, col))) {
				return;
			}
			
			// check if it is the last point on the line
			if ( currLine.getLast() == new Location(row, col) ) {
				// if true, cell to the line
				addCell(row, col);
			}
			
			
		}

	}

	/**
	 * Sets the current line to null. When using a GUI, this method is 
	 * typically invoked when the mouse is released.
	 */
	public void endLine()
	{
		currLine = null;
	}

	/**
	 * Attempts to add a new cell to the current line.  
	 * When using a GUI, this method is typically invoked when the mouse is 
	 * dragged.  In order to add a cell, the following conditions must be satisfied.
	 * Here the "current cell" is the last cell in the current line, and "new cell"
	 * is the cell at the given row and column:
	 * :
	 * <ol>
	 *   <li>The current line is non-null
	 *   <li>The current line is not connected
	 *   <li>The given row and column are adjacent to the location of the current cell
	 *       (horizontally, vertically, or diagonally) and not the same as the current cell
	 *   <li>The count for the new cell is less than its max count
	 *   <li>If the new cell is a MIDDLE or ENDPOINT, then its id matches
	 *   the id for the current line
	 *   <li>Adding the new cell will not cause the line to re-trace any
	 *   existing line (according to the result of Util.checkForLineSegment)
	 *   <li>Adding the new cell to the line would not cross any existing line
	 *   (according to the result of Util.checkForPotentialCrossing)
	 * </ol>
	 * If the above conditions are met, a new Location at (row, col) is added
	 * to the current line and the cell count is incremented.  Otherwise, the 
	 * method does nothing.  If a new location
	 * is added to the current line, the move counter is increased by 1.
	 * @param row
	 *   given row for the new cell
	 * @param col
	 *   given column for the new cell
	 */
	public void addCell(int row, int col)
	{

		/*
		 * conditions:
		 * currline is not null
		 * currline is not connected
		 * row and column are adjacent to the current cell
		 * count is less than max
		 * if new cell is middle or end, id must match
		 * line segment does not already exist
		 * new cell would not cross
		 */

		GridCell newCell = grid[row][col];

		// if currline is null, return
		if (currLine == null) {
			return;
		}
		
		// if curr line is not connected, return
		if (currLine.isConnected()) {
			return;
		}
		
		// if the count is exceeded, return
		if (newCell.getCount() >= newCell.getMaxCount()) {
			return;
		}

		// if they are endpoints or middle, the ids must match
		if (currCell.isEndpoint() || currCell.isMiddle()) {
			// check if ids match
			if (currCell.getId() != newCell.getId()) {
				return;
			}
		}
		
		// if not adjacent, return
		if (Math.abs(getLoc(currCell).row() - row) > 1 || Math.abs(getLoc(currCell).col() - col) > 1) {
			return;
		}

		// check if line segment already exists
		if ( Util.checkForLineSegment(lines, getLoc(currCell), new Location(row, col))) {
			return;
		}

		// check if line would cross
		if (Util.checkForPotentialCrossing(lines, getLoc(currCell), new Location(row, col))) {
			return;
		}

		// add the cell to currline
		currLine.add(new Location(row, col));

		// update currCell
		currCell = grid[row][col];

		// increment currCell
		currCell.increment();

		// add a move
		moveCount ++;


	}

	private Location getLoc(GridCell g) {

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] == g) {
					return new Location(i, j);
				}
			}
		}
		return null;
	}


	/**
	 * Returns a string representation of this game.
	 */
	public String toString()
	{
		String result = "";
		result += "-----\n";
		result += StringUtil.originalGridToString(getGrid());
		result += "-----\n";
		result += StringUtil.currentGridToString(getGrid(), getAllLines());
		result += "-----\n";
		result += StringUtil.allLinesToString(getAllLines());
		Line ln = getCurrentLine();
		if (ln != null)
		{
			result += "Current line: " + ln.getId() + "\n";
		}
		else
		{
			result += "Current line: null\n";
		}
		return result;
	}

}
