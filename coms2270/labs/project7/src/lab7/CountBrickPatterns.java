package lab7;

public class CountBrickPatterns {
	
	public static void main(String[] args) {
		
		System.out.println(countPatterns(55));
		
	}
	
	public static int countPatterns(int length) {
		
		if (length < 3) {
			return 1;
		}
		if (length == 3) {
			return 2;
		}
		
		int total = 0;
		
		total += countPatterns(length - 1);
		total += countPatterns(length - 3);
		
		return total;
	}

}
