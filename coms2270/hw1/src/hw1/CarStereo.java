package hw1;

public class CarStereo {
	
	/**
	 * A static variable representing one "notch" of volume
	 */
	public static final double VOLUME_STEP = 0.16;
	
	/**
	 * A variable representing the current volume
	 */
	private double currVolume = 0.5;
	
	/**
	 * Variables storing the frequency range and the current frequency
	 */
	private double minFrequency;
	private double maxFrequency;
	private double currFrequency;
	private double interval;
	
	/** 
	 * Variable to store a preset station
	 */
	private double presetStation;
	
	/**
	 * Variable to store the total number of stations
	 */
	private int numStations;
	
	/**
	 * 
	 * @param givenMinFrequency the minimum frequency the stereo can be set to
	 * @param givenMaxFrequency the maximum frequency the stereo can be set to
	 * @param givenNumStations the number of stations in the range (max - min) 
	 * Constructor to initialize variables and caluclate interval
	 */
	public CarStereo(double givenMinFrequency, double givenMaxFrequency, int givenNumStations) {
		
		minFrequency = givenMinFrequency;
		maxFrequency = givenMaxFrequency;
		numStations = givenNumStations;
		
		// Makes sure currFrequency is not initialized as 0
		// If currFrequency was 0, it would be less than the minimum
		currFrequency = minFrequency;
		
		double freqDif = maxFrequency - minFrequency;
		interval = freqDif / numStations;
		
	}
	
	/**
	 * getVolune is an accessor function that will return the volume.
	 * @return currVolume the current volume
	 */
	public double getVolume() {
		return currVolume;
	}
	
	/**
	 * louder is a mutator function that increases the volume.
	 */
	public void louder() {
		
		currVolume = Math.min(1.0, currVolume + VOLUME_STEP);
	}
	
	/**
	 * quieter is a mutator function that decreases the volume
	 */
	public void quieter() {
		
		currVolume = Math.max(0, currVolume - VOLUME_STEP);
	}
	
	/**
	 * getTuner is an accessor function that returns the current frequency
	 * @return currFrequency the variable storing the current frequency.
	 */
	public double getTuner() {
		return currFrequency;
	}
	
	/**
	 * @param givenFrequency what we are setting the frequency to
	 * setTuner is an mutator function that sets the current frequency
	 * to the parameter givenFrequency
	 */
	public void setTuner(double givenFrequency) {
		double temp = Math.min(maxFrequency, givenFrequency);
		temp  = Math.max(minFrequency, temp);
		currFrequency = temp;
	}
	
	/**
	 * @param degrees is how far the dial is turned
	 * turnDial is a mutator function that changes the frequency based
	 * on the degrees turned. 1 full turn goes from min to max frequency.
	 */
	public void turnDial(double degrees) {
		
		double turn = degrees / 360.0;
		double freqMovement = turn * (maxFrequency - minFrequency);
		
		this.setTuner(currFrequency + freqMovement);
		
	}
	/**
	 * @param stationNumber the station the user wants to turn to
	 * setTunerFromStationNumber is a mutator function that takes a station
	 * number and sets the current frequency to a value within that 
	 * station's range.
	 */
	public void setTunerFromStationNumber(int stationNumber) {
		int statNum = Math.min(stationNumber, numStations - 1);
		statNum = Math.max(statNum, 0);
		double temp = statNum * interval;
		currFrequency = Math.min(maxFrequency, temp + minFrequency) + interval / 2;
	}
	
	/**
	 * findStationNumber calculates the station number based on the current
	 * frequency. 
	 * @return the station number
	 */
	public int findStationNumber() {
		
		// goal = if midpoint, then add 1 to currFrequency, else leave it
		
		double ifModulo = currFrequency % interval; // will either be 0 (midpoint) or greater than 0 (not midpoint)
		ifModulo = Math.min(ifModulo - 1, 0); // if ifModulo > 0, will return 0 and not affect function, otherwise will return -1
		
		double past0 = (currFrequency - ifModulo - minFrequency) / interval; // subtract 0 if not midpoint, subtract -1 if midpoint
		// Need to round up
		past0 = Math.ceil(past0);
		// Ensures we don't go below station 0
		past0 = Math.max(0, past0 - 1);
		int tempInt = (int)(past0);
		return tempInt;
		
	}
	
	/**
	 * seekDown turns the frequency down 1 station
	 */
	public void seekDown() {
		
		setTunerFromStationNumber((findStationNumber() + numStations - 1) % numStations );
		
	}
	
	/**
	 * seekUp turns the frequency up 1 station
	 */
	public void seekUp() {
		
		setTunerFromStationNumber((findStationNumber() + 1) % numStations);
		
	}
	
	/**
	 * savePreset saves a station with the frequency.
	 */
	public void savePreset() {
		
		presetStation = findStationNumber();
	}
	
	/**
	 * goToPreset sets the frequency to the preset made in savePreset
	 */
	public void goToPreset() {
		
		currFrequency = minFrequency + (presetStation) * interval + (interval / 2);
	}
	
	
	
}
