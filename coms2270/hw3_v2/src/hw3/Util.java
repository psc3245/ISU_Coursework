package hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import api.GridCell;
import api.Line;
import api.Location;
import api.StringUtil;

/**
 * Utility class with methods to help initializing a Lines game from 
 * a string descriptor, and for creating a collection of games from
 * a file containing descriptors.
 */

public class Util
{


	/**
	 * Given a 2d array of GridCell, constructs an array of Line
	 * objects based on the information in the grid.  Specifically,
	 * for each pair of endpoints with matching ids, a corresponding
	 * Line object is constructed with that id and with the given endpoints.
	 * The order of the endpoints (endpoint 0 vs endpoint 1) is unspecified.
	 * If there are more than two endpoints with the same id, or if there
	 * is only one endpoint with the given id, this
	 * method returns null. No other error-checking is performed (e.g. there
	 * may be middle cells with no matching endpoint, or the game
	 * may be unsolvable for other reasons).
	 * <p>
	 * Note that in general the id for a Line will <em>not</em> be the
	 * same as its index in the returned array.
	 * @param grid
	 *   a 2d array of GridCell
	 * @return
	 *   array of Line objects based on the grid information
	 */
	public static ArrayList<Line> createLinesFromGrid(GridCell[][] grid)
	{

		// create the arraylist of lines
		ArrayList<Line> lines = new ArrayList<Line>();

		// create a list to store our lines and locations
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ArrayList<Location> locs = new ArrayList<Location>();

		// iterate through the nested lists
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {

				// create the cell we are in
				GridCell cell = grid[i][j];

				// get the cell's id and location
				int id = cell.getId();
				Location loc = new Location(i, j);

				// check for endpoint
				if (cell.isEndpoint()) {
					// if endpoint, check if we have seen the match yet
					if (ids.contains(id)) {
						int index = ids.indexOf(id);
						Line l = new Line(id, loc, locs.get(index));
						lines.add(l);
					}
					ids.add(id);
					locs.add(loc);
				}

			}
		}

		// if there is an uneven amount of endpoints, return null
		// (one line has 3 endpoints or there is an unmatched enpoint)
		if (ids.size() % 2 != 0) {
			return null;
		}

		// otherwise return lines
		return lines;


	}

	/**
	 * Reads the given file and constructs a list of LinesGame objects, one for
	 * each descriptor in the file.  Descriptors in the file are separated by one or more
	 * blank lines, where a "blank line" consists of some amount of whitespace and a 
	 * newline character. The file may have extra whitespace at the beginning, 
	 * and it must always end with one or more blank lines. Invalid descriptors
	 * are ignored, so the method may return an empty list.  (A descriptor is "invalid"
	 * if either createGridFromStringArray returns null, or createLinesFromGrid
	 * returns null.)
	 * @param filename
	 *   name of the file to read
	 * @return
	 *   list of LinesGame objects created from the valid descriptors in the file
	 * @throws FileNotFoundException
	 *   if a file with the given name can't be opened
	 */ 
	public static ArrayList<LinesGame> readFile(String filename) throws FileNotFoundException
	{
		// TODO
		
		// create the list we will return
		ArrayList<LinesGame> games = new ArrayList<LinesGame>();

		// get the file from the filename
		File f = new File(filename);

		// make a scanner
		Scanner s = new Scanner(f);
		
		// make an array list of lines
		ArrayList<String> lines = new ArrayList<String>();


		while (s.hasNext()) {
			
			// get the current line
			String line = s.nextLine();
			
			// counter
			int counter = 0;
			
			// every time we hit a new line, increment counter
			if (line.equals("\n")) {
				counter ++;
			}
			
			// once counter hits 2, we have ran into a new game
			if (counter == 2) {
				// reset counter to 1 because we are currently at a newline character
				counter = 1;
				// convert the arraylist to an array
				String[] strings = new String[lines.size()];
				
				for (String l : lines) {
					strings[line.indexOf(l)] = l;
				}
			
				// create a new game and add it to games
				games.add(new LinesGame(strings));
				
				// clear lines for the next game
				lines.clear();
			}

			// only proceed if the string is not just a new line character
			if (!line.equals("\n")) {
				
				// add the line to the list of lines
				lines.add(line);
			}
		}
		
		s.close();
		
		return games;
	}



	/**
	 * Determines whether a line between two diagonally adjacent locations
	 * would cross any existing line in the given list.
	 * The check is based on the following test:
	 * <ul>
	 *  <li>Let (rOld, cOld) denote the current cell location and let (rNew, cNew) denote
	 * the new cell location.  
	 *  <li>Let rDiff = rNew - rOld and cDiff = cNew - cOld.
	 *  <li>If either rDiff or cDiff does not have absolute value 1, then
	 *  the two positions are not diagonally adjacent and the method returns false
	 *  <li>If the two positions are diagonally adjacent, then p0 = (rOld, cOld + cDiff) 
	 *  and p1 = (rOld + rDiff, cOld) always form the opposite diagonal (i.e., the 
	 *  line that could potentially be crossed).
	 *  <li>The method returns true if p0 and p1 occur consecutively, in either order,
	 *  in any existing line in the given array.
	 * </ul>
	 * 
	 * @param lines
	 *   list of Line objects
	 * @param currentLoc
	 *   any Location
	 * @param newLoc
	 *   any Location
	 * @return
	 *   true if the two locations are diagonally adjacent and some
	 *   existing line crosses the opposite diagonal
	 */
	public static boolean checkForPotentialCrossing(ArrayList<Line> lines, Location currentLoc, Location newLoc)
	{

		int rowDiff = Math.abs(currentLoc.row() - newLoc.row());
		int colDiff = Math.abs(currentLoc.col() - newLoc.col());

		// if the cells are not adjacent, return false
		if (!(rowDiff == 1 && colDiff == 1)) {
			return false;
		}

		// create the new "line segment" with points p0 and p1
		Location p0 = new Location(currentLoc.row(), currentLoc.col() + colDiff);
		Location p1 = new Location(currentLoc.row() + rowDiff, currentLoc.col());
		// iterate through lines
		for (int i = 0; i < lines.size(); i++) {
			// get the locations
			ArrayList<Location> locs = lines.get(i).getCells();

			// iterate through locs
			for (int j = 0; j < locs.size(); j++) {
				// check for location 1
				if (locs.get(j).equals(p0)) {
					// if we find it, check if location 2 is next
					if (j + 1 < locs.size()) {
						if (locs.get(j).equals(p1)) {
							// if they are back to back, there is a line there already
							return true;
						}
					}
				}

				// check for location 2
				if (locs.get(j).equals(p1)) {
					// if we find it, check if location 1 is next
					if (j + 1 < locs.size()) {
						if (locs.get(j).equals(p0)) {
							// if they are back to back, there is a line there already
							return true;
						}
					}
				}
			}

		}


		return false;
	}

	/**
	 * Determines whether any line in the given array already contains the segment between 
	 * the given locations; that is, whether the two given locations occur consecutively,
	 * in either order, in any of the given lines.
	 * @param lines 
	 *   any array of lines
	 * @param currentLoc
	 *   any position object
	 * @param newLoc
	 *   any position object
	 * @return
	 *   true if the two locations occur consecutively in some line
	 */
	public static boolean checkForLineSegment(ArrayList<Line> lines, Location currentLoc, Location newLoc)
	{
		
		// iterate through the lines
		for (Line l : lines) {
			
			// get the locations from the line
			ArrayList<Location> locs = l.getCells();
			
			// iterate through locs
			for (int j = 0; j < locs.size(); j++) {
				// check for current location
				if (locs.get(j).equals(currentLoc)) {
					// if we find it, check if new location is next
					if (j + 1 < locs.size()) {
						if (locs.get(j).equals(newLoc)) {
							// if they are back to back, there is a line there already
							return true;
						}
					}
				}

				// check for new location
				if (locs.get(j).equals(newLoc)) {
					// if we find it, check if current location is next
					if (j + 1 < locs.size()) {
						if (locs.get(j).equals(currentLoc)) {
							// if they are back to back, there is a line there already
							return true;
						}
					}
				}
			}
		}
		// if the loop finished, that means the locations were never found in order, so return false;
		return false;
	}

}
