package hw1;

public class SimpleTests {
	
	public static void main(String[] args) {
		CarStereo c = new CarStereo(100, 200, 5);
		
	
/*
		// Volume Tests
		System.out.println(c.getVolume()); //expected 0.5
		c.louder();
		c.louder();
		System.out.println(c.getVolume()); //expected 0.82
		c.louder();
		c.louder();
		System.out.println(c.getVolume()); // expected 1.0
		c.quieter();
		System.out.println(c.getVolume()); // expected 0.84
		

		System.out.println(c.getVolume()); // expected 0.5
		c.quieter();
		System.out.println(c.getVolume());
		c.quieter();
		System.out.println(c.getVolume()); // expected 0.02
		c.quieter();
		c.quieter();
		System.out.println(c.getVolume()); // expected 0

		// Tuner and findStationNumber Tests
		System.out.println(c.getTuner()); // expected 100
		c.setTuner(120);
		System.out.println(c.getTuner()); // expected 120
		System.out.println(c.findStationNumber()); // expected 0
		c.setTuner(131);
		System.out.println(c.findStationNumber()); // expected 1
		c.setTuner(180);
		System.out.println(c.findStationNumber()); // expected 3
		c.setTuner(181);
		System.out.println(c.findStationNumber()); // expected 4

		// Seek up and Seek down tests
		c.setTuner(100);
		System.out.println(c.getTuner());
		System.out.println(c.findStationNumber());
		c.seekUp();
		System.out.println(c.getTuner());
		System.out.println(c.findStationNumber());
		c.seekUp();
		System.out.println(c.getTuner());
		System.out.println(c.findStationNumber());
		c.seekDown();
		System.out.println(c.getTuner());
		System.out.println(c.findStationNumber());


		
		c.setTunerFromStationNumber(2);
		System.out.println(c.getTuner());
		System.out.println(c.findStationNumber());

		c.setTuner(100);
		System.out.println(c.getTuner());
		System.out.println(c.findStationNumber());
		c.setTuner(119);
		System.out.println(c.getTuner());
		System.out.println(c.findStationNumber());
*/
		
		CarStereo c2 = new CarStereo(100, 200, 5);
		c2.setTuner(120);
		System.out.println(c2.findStationNumber());
	}
}
