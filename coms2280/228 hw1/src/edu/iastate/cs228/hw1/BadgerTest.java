package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;



import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

/**
 *  
 * @author peter.collins
 *
 */

class BadgerTest {

	@Test
	public void testBadgerConstructor() // Tests the badger constructor
	{
		Plain p = new Plain(3);
		Badger b = new Badger(p, 1, 2, 3);
		assertEquals(b.row, 1);
		assertEquals(b.column, 2);
		assertEquals(b.plain, p);
		assertEquals(b.age, 3);
	}

	@Test
	public void badgerTest() // Tests the example given for Badger in project documentation
	{
		Plain p = null;
		try {
			p = new Plain("src/edu/iastate/cs228/hw1/BadgerExample.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Plain newPlain = new Plain (3);
		Living nextLiving = p.grid[1][1].next(newPlain);
		assertEquals(nextLiving.who(), State.BADGER);
		assertEquals(((Animal) nextLiving).myAge(), 3);
	}

}
