package lab7;

public class BallsInPyramid {
	
	public static void main(String[] args) {
		
		System.out.println(countBalls(5));
		
	}
	
	public static int countBalls(int height) {
		// base cases
		if (height == 0) {
			return 0;
		}
		
		if (height == 1) {
			return 1;
		}
		
		// accumulator
		int total = 0;
		
		// accumulate and recurse
		total += height * height + countBalls(height - 1);
		
		// return when finished
		return total;
	}

}
