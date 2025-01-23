package lab3;

import balloon4.Balloon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class BalloonTests {
	
	private Balloon bb;
	
	@Before
	public void setUp() {
		bb = new Balloon(5);
	}
	
	@Test
	public void testInital() {
		assertEquals(bb.getRadius(), 0);
		assertEquals(bb.isPopped(), false);
	}
	
	@Test
	public void testBlow() {
		bb.blow(3);
		assertEquals(bb.getRadius(), 3);
		bb.blow(3);
		assertEquals(bb.isPopped(), true);
		assertEquals(bb.getRadius(), 0);
		
	}
	
	@Test
	public void testDeflate() {
		bb.deflate();
		assertEquals(bb.getRadius(), 0);
		assertEquals(bb.isPopped(), false);
		
	}
	
	@Test
	public void testGetRadius() {
		bb.blow(3);
		assertEquals(bb.getRadius(), 3);
	}
	
	@Test
	public void testIsPopped() {
		assertEquals(bb.isPopped(), false);
	}
	
	@Test
	public void testPop() {
		assertEquals(bb.isPopped(), false);
		bb.pop();
		assertEquals(bb.isPopped(), true);
		bb.blow(5);
		assertEquals(bb.getRadius(), 0);
		
	}
	
	
	

}
