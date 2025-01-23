package edu.iastate.cs228.hw1;

/**
 *  
 * @author peter.collins
 *
 */

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AnimalTest {

	@Test
	void test() {
		Badger b = new Badger(new Plain(5), 3, 3, 4);
		int age = ((Animal)b).age;
		assertEquals(age, 4);
	}

}
