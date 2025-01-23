package edu.iastate.cs228.hw3;


import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.ListIterator;

import static org.junit.Assert.*;

/**
 * JUnit 4.13 tests for HW3.
 * Life pro tip: (Eclipse: click on the error on the left) or (IntelliJ: hover over)
 * one of the @Test or @Before annotations, and the IDE will give a suggestion to
 * automatically put JUnit in your project.
 *
 * @author Thanh Mai
 */
public class Tests {
	private StoutList<Character> st;

	@Before
	public void initStoutList() {
		st = new StoutList<>(4);
	}

	@Test
	public void testSimpleAdd() {
		st.add('A');
		st.add('B');
		st.add('C');
		st.add('D');
		st.add('E');
		st.add('F');
		st.add('G');
		st.add('H');
		st.add('I');

		assertEquals("[(A, B, C, D), (E, F, G, H), (I, -, -, -)]",
				st.toStringInternal()
				);
	}

	@Test
	public void testSplittingAdd() {
		st.add('A');
		st.add('B');
		st.add('C');
		st.add('D');
		st.add('E');
		st.add('V');

		st.add(2, 'X');
		st.add(2, 'Y');
		st.add(2, 'Z');

		assertEquals("[(A, B, Z, -), (Y, X, -, -), (C, D, -, -), (E, V, -, -)]",
				st.toStringInternal()
				);
	}


	@Test
	public void testSimpleRemove() {
		st.add('A');
		st.add('B');
		st.add('C');
		st.remove(1);
		assertEquals("[(A, C, -, -)]", st.toStringInternal());
	}

	@Test
	public void testMergingRemove() {
		st.add('A');
		st.add('B');
		st.add('#');
		st.add('#');
		st.add('C');
		st.add('D');
		st.add('E');
		st.remove(2);
		st.remove(2);
		st.add('V');
		st.add('W');
		st.add(2, 'X');
		st.add(2, 'Y');
		st.add(2, 'Z');
		st.remove(9);
		st.remove(3);
		st.remove(3);
		st.remove(5);
		st.remove(3);

		assertEquals("[(A, B, Z, -), (D, V, -, -)]", st.toStringInternal());
	}

	@Test
	public void testSize() {
		st.add('C');
		st.add('D');
		st.add('E');
		st.remove(2);
		st.add('G');
		st.add('H');
		st.remove(3);
		st.add('I');
		st.add(2, 'Z');
		st.add(4, 'Y');
		st.add('V');
		st.add('W');
		st.remove(5);
		st.add(2, 'K');
		st.remove(3);
		assertEquals(7, st.size());
	}

	@Test
	public void testContains() {
		st.add('A');
		st.add('B');
		st.add('C');
		st.add('D');
		st.add('E');
		assertTrue(st.contains('A'));
		assertTrue(st.contains('D'));
		assertTrue(st.contains('E'));
	}

	@Test
	public void testListIteratorForward() {
		st.add('G');
		st.add('H');
		st.add('I');
		st.add('J');
		st.add('K');
		st.add('L');
		ListIterator<Character> iterator = st.listIterator();

		for (int i = 0; i < 6; i++) {
			assertTrue(iterator.hasNext());
			assertEquals((char) (71 + i), iterator.next().charValue());
		}
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testListIteratorBackward() {
		st.add('P');
		st.add('Q');
		st.add('R');
		st.add('S');
		st.add('T');
		st.add('U');
		st.add('V');
		ListIterator<Character> iterator = st.listIterator(st.size());
		
		System.out.println(st.toStringInternal());

		for (int i = 6; i >= 0; i--) {
			assertTrue(iterator.hasPrevious());
			assertEquals((char) (80 + i), iterator.previous().charValue());
		}
		assertFalse(iterator.hasPrevious());
	}

	@Test
	public void testListIteratorAdd() {
		st.add('A');
		st.add('B');
		ListIterator<Character> iterator = st.listIterator();
		iterator.next();
		iterator.previous();
		iterator.next();
		iterator.add('C');
		assertEquals("[(A, C, B, -)]", st.toStringInternal());
		assertEquals('B', iterator.next().charValue());
		assertEquals('B', iterator.previous().charValue());
		assertEquals('C', iterator.previous().charValue());
		iterator.add('X');
		iterator.add('Y');
		assertEquals("[(A, X, Y, -), (C, B, -, -)]", st.toStringInternal());
	}

	@Test
	public void testListIteratorRemove() {
		st.add('X');
		st.add('Y');
		st.add('#');
		st.add('Z');
		st.addAll(Arrays.asList('P', 'Q', 'R', 'S', 'T', 'U', 'V'));
		assertEquals("[(X, Y, #, Z), (P, Q, R, S), (T, U, V, -)]", st.toStringInternal());
		ListIterator<Character> iterator = st.listIterator();
		iterator.next();
		iterator.next();
		iterator.next();
		iterator.previous();
		assertEquals('#', iterator.next().charValue());
		iterator.remove();
		assertEquals("[(X, Y, Z, -), (P, Q, R, S), (T, U, V, -)]", st.toStringInternal());
		assertEquals('Z', iterator.next().charValue());
		iterator.remove();
		assertEquals("[(X, Y, -, -), (P, Q, R, S), (T, U, V, -)]", st.toStringInternal());
		iterator.next();
		iterator.next();
		iterator.next();
		assertEquals('R', iterator.previous().charValue());
		iterator.remove();
		assertEquals("[(X, Y, -, -), (P, Q, S, -), (T, U, V, -)]", st.toStringInternal());
		assertEquals('S', iterator.next().charValue());
		iterator.remove();
		assertEquals("[(X, Y, -, -), (P, Q, -, -), (T, U, V, -)]", st.toStringInternal());
		assertEquals('Q', iterator.previous().charValue());
		iterator.previous();
		iterator.previous();
		iterator.remove();
		assertEquals("[(X, P, Q, -), (T, U, V, -)]", st.toStringInternal());

	}

	@Test
	public void testListIteratorSet() {
		st.add('I');
		st.add('J');
		st.add('K');
		ListIterator<Character> iterator = st.listIterator();
		iterator.next();
		iterator.next();
		iterator.set('W');
		assertEquals("[(I, W, K, -)]", st.toStringInternal());
		iterator.next();
		iterator.set('X');
		assertEquals("[(I, W, X, -)]", st.toStringInternal());
		assertFalse(iterator.hasNext());
		assertEquals('X', iterator.previous().charValue());
		assertEquals('W', iterator.previous().charValue());
		assertEquals('I', iterator.previous().charValue());
		iterator.set('#');
		assertFalse(iterator.hasPrevious());
		assertEquals("[(#, W, X, -)]", st.toStringInternal());
	}


	@Test
	public void testInsertionSorting() {
		st.add('A');
		st.add('B');
		st.add('D');
		st.add(2, 'Z');
		st.add('C');
		st.add('J');
		st.remove(4);
		st.add('C');
		st.add('E');
		st.remove(2);
		st.add('V');
		st.add(2, 'X');
		st.add(2, 'Y');
		st.remove(2);
		st.add('W');
		st.remove(5);
		st.remove(3);
		st.remove(3);
		st.remove(3);
		st.sort();

		assertEquals("[(A, B, V, W), (X, -, -, -)]", st.toStringInternal());
	}

	@Test
	public void testBubbleSorting() {
		st.add('A');
		st.add('B');
		st.add('C');
		st.add('D');
		st.add('E');
		st.add('F');
		st.remove(2);
		st.add('G');
		st.add('H');
		st.remove(3);
		st.add('I');
		st.add('J');
		st.add(2, 'X');
		st.add(2, 'Y');
		st.add('V');
		st.remove(9);
		st.add('W');
		st.remove(5);
		st.add(2, 'Z');
		st.remove(3);
		st.sortReverse();

		assertEquals("[(Z, X, W, V), (I, H, G, D), (B, A, -, -)]", st.toStringInternal());
	}
}