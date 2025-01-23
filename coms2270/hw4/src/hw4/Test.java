package hw4;

import static api.Direction.DOWN;
import static api.Direction.LEFT;
import static api.Direction.RIGHT;
import static api.Direction.UP;

import java.util.ArrayList;

import api.Direction;
import api.Location;
import api.Mode;

public class Test {

	public static void main(String[] args) {

	



	}
	
	
//	private Direction getTargetDirection(int targetRow, int targetCol) {
//
//		Direction finalDir;
//
//
//		// create an array to store the distances
//		//double[] dists = new double[4];
//		ArrayList<Double> dists = new ArrayList<>();
//
//		int nextRow;
//		int nextCol;
//
//		// case up
//		nextRow = (int)getCurrentLocation().row() - 1;
//		nextCol = (int)getCurrentLocation().col();
//
//		if (maze.isWall(nextRow, nextCol)) {
//			dists.add( (double) Integer.MAX_VALUE);
//		}
//		else {
//			dists.add(getDistance(nextRow, targetRow, nextCol, targetCol));
//		}
//
//		// case left
//
//		nextRow = (int)getCurrentLocation().row();
//		nextCol = (int)getCurrentLocation().col() - 1;
//
//		if (maze.isWall(nextRow, nextCol)) {
//			dists.add( (double) Integer.MAX_VALUE);
//		}
//		else {
//			dists.add(getDistance(nextRow, targetRow, nextCol, targetCol));
//		}
//
//		// case down
//
//		nextRow = (int)getCurrentLocation().row() + 1;
//		nextCol = (int)getCurrentLocation().col();
//
//		if (maze.isWall(nextRow, nextCol)) {
//			dists.add( (double) Integer.MAX_VALUE);
//		}
//		else {
//			dists.add(getDistance(nextRow, targetRow, nextCol, targetCol));
//		}
//
//		// case right
//
//		nextRow = (int)getCurrentLocation().row();
//		nextCol = (int)getCurrentLocation().col() + 1;
//
//		if (maze.isWall(nextRow, nextCol)) {
//			dists.add( (double) Integer.MAX_VALUE);
//		}
//		else {
//			dists.add(getDistance(nextRow, targetRow, nextCol, targetCol));
//		}
//
//		
//		int[] order = new int[4];
//		order[0] = findLowest(dists);
//		dists.set(order[0], (double)Integer.MAX_VALUE);
//		order[1] = findLowest(dists);
//		dists.set(order[1], (double)Integer.MAX_VALUE);
//		order[2] = findLowest(dists);
//		dists.set(order[2], (double)Integer.MAX_VALUE);
//		order[3] = findLowest(dists);
//		dists.set(order[3], (double)Integer.MAX_VALUE);
//		
//		
//		
//		
//
//		// set nextdirection 
//		switch(order[0]) {
//		case 0:
//			if (mode != Mode.FRIGHTENED && currentDirection == DOWN) {
//				
//			}
//			finalDir = UP;
//			nextLoc = new Location( getCurrentLocation().row() - 1, getCurrentLocation().col() );
//		case 1:
//			if (mode != Mode.FRIGHTENED && currentDirection == RIGHT) {
//				
//			}
//			finalDir = LEFT;
//			nextLoc = new Location( getCurrentLocation().row(), getCurrentLocation().col() - 1 );
//		case 2:
//			if (mode != Mode.FRIGHTENED && currentDirection == UP) {
//
//			}
//			finalDir = DOWN;
//			nextLoc = new Location( getCurrentLocation().row() + 1, getCurrentLocation().col() );
//		case 3:
//			if (mode != Mode.FRIGHTENED && currentDirection == LEFT) {
//
//			}
//			finalDir = RIGHT;
//			nextLoc = new Location( getCurrentLocation().row(), getCurrentLocation().col() + 1 );
//		default:
//			finalDir = UP;
//			nextLoc = new Location( getCurrentLocation().row() - 1, getCurrentLocation().col() );
//		}
//
//		return finalDir;
//
//	}


}
