package lab3;

public class Basketball {
	
	private double diameter;
	private boolean isDribbleable;
	
	public Basketball(double givenDiameter) {
		diameter = givenDiameter;
		isDribbleable = false;
		
	}
	
	public boolean isDribbleable() {
		return isDribbleable;
	}
	
	public double getDiameter() {
		return diameter;
	}
	
	public double getCircumference() {
		return 0;
	}
	
	public void inflate() {
		isDribbleable = true;
	}
	
	public void deflate() {
		isDribbleable = false;
	}
}
