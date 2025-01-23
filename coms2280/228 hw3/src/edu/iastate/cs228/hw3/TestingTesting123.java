package edu.iastate.cs228.hw3;

import java.util.ListIterator;

public class TestingTesting123 {

	
	public static void main(String[] args) {
		
		
		StoutList<Integer> s = new StoutList<Integer>();
		
		s.add(5);
		s.add(2);
		s.add(54);
		s.add(2);
		s.add(512);
		s.add(4, 6);
		s.add(4, 623);
		s.add(2);
		s.add(54);
		
		ListIterator<Integer> i = s.listIterator();
		
		System.out.println(s.toStringInternal());
		
		System.out.println(i.next());
		System.out.println(i.next());
		System.out.println(i.next());
		System.out.println(i.next());
		System.out.println(i.next());
	}
}
