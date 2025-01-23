package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 *  
 * @author peter.collins
 *
 */

class PlainTest {

	
	@Test
	void testPlainConstructor1() {
		Plain p = new Plain(5);
		assertEquals(p.getWidth(), 5);
	}
	

}
