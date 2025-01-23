package lab7;

public class MaxValueOfArray {
	
	public static void main(String[] args) {
		
		
		int[] arr = {1, 2, 8, 4, 5, 6};
		
		
		System.out.println(findMax(arr, 0, 5));
		
	}
	
	public static int findMax(int[] arr, int start, int end) {
		
		if (start == end) {
			return arr[start];
		}
		
		
		else {
			
			int mid = (start + end) / 2;
			
			int leftMax = findMax(arr, start, mid);
			int rightMax = findMax(arr, mid + 1, end);
			
			if (leftMax >= rightMax) {
				return leftMax;
			}
			else {
				return rightMax;
			}
		}
	
		
	}

}
