package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

/**
 *  
 * @author peter.collins
 *
 */

class EmptyTest {

	@Test
	public void emptyTest() // Tests the example given for Empty in project documentation
	{
		Plain p = null;
		try {
			p = new Plain("src/edu/iastate/cs228/hw1/EmptyExample.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Plain newPlain = new Plain(3);
		Living nextLiving = p.grid[0][1].next(newPlain);
		assertEquals(nextLiving.who(), State.RABBIT);
		assertEquals(((Animal) nextLiving).myAge(), 0);
	}

}
