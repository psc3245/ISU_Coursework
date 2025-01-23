package edu.iastate.cs228.hw3;

import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Implementation of the list interface based on linked nodes
 * that store multiple items per node.  Rules for adding and removing
 * elements ensure that each node (except possibly the last one)
 * is at least half full.
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E>
{
	/**
	 * Default number of elements that may be stored in each node.
	 */
	private static final int DEFAULT_NODESIZE = 4;

	/**
	 * Number of elements that can be stored in each node.
	 */
	private final int nodeSize;

	/**
	 * Dummy node for head.  It should be private but set to public here only  
	 * for grading purpose.  In practice, you should always make the head of a 
	 * linked list a private instance variable.  
	 */
	public Node head;

	/**
	 * Dummy node for tail.
	 */
	private Node tail;

	/**
	 * Number of elements in the list.
	 */
	private int size;

	/**
	 * Constructs an empty list with the default node size.
	 */
	public StoutList()
	{
		this(DEFAULT_NODESIZE);
	}

	/**
	 * Constructs an empty list with the given node size.
	 * @param nodeSize number of elements that may be stored in each node, must be 
	 *   an even number
	 */
	public StoutList(int nodeSize)
	{
		if (nodeSize <= 0 || nodeSize % 2 != 0) throw new IllegalArgumentException();

		// dummy nodes
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		this.nodeSize = nodeSize;
	}

	/**
	 * Constructor for grading only.  Fully implemented. 
	 * @param head
	 * @param tail
	 * @param nodeSize
	 * @param size
	 */
	public StoutList(Node head, Node tail, int nodeSize, int size)
	{
		this.head = head; 
		this.tail = tail; 
		this.nodeSize = nodeSize; 
		this.size = size; 
	}

	@Override
	public int size()
	{
		return size;
	}

	@Override
	public boolean add(E item)
	{

		if (item == null) { throw new NullPointerException(); }

		if (head.next == tail) {
			Node n = new Node();
			head.next = n;
			tail.previous = n;
			n.previous = head;
			n.next = tail;
			n.addItem(item);
			size ++;
			return true;
		}

		else if (tail.previous.count < nodeSize) {
			Node n = tail.previous;
			for (int i = 0; i < nodeSize; i++) {
				if (tail.previous.data[i] == null) {
					n.addItem(i, item);
					size ++;
					return true;
				}
			}
		}

		else if (tail.previous.count == nodeSize) {
			Node n = new Node();
			Node temp = tail.previous.previous;
			tail.previous.next = n;
			tail.previous = n;
			n.next = tail;
			n.previous = temp;
			n.addItem(item);
			size ++;
			return true;
		}



		return false;
	}

	@Override
	public void add(int pos, E item)
	{

		if (pos < 0 || pos > size) { throw new IndexOutOfBoundsException(); }

		if (head.next == tail) { add(item); }

		NodeInfo nodeInfo = find(pos);
		Node temp = nodeInfo.node;
		int offset = nodeInfo.offset;


		if (offset == 0) {

			if (temp.previous.count < nodeSize && temp.previous != head) {
				temp.previous.addItem(item);
				size++;
				return;
			}

			else if (temp == tail) {
				add(item);
				size++;
				return;
			}
		}

		if (temp.count < nodeSize) {
			temp.addItem(offset, item);
		}

		else {
			Node newNext = new Node();
			int midpoint = nodeSize / 2;
			int count = 0;
			while (count < midpoint) {
				newNext.addItem(temp.data[midpoint]);
				temp.removeItem(midpoint);
				count++;
			}

			Node oldNext = temp.next;

			temp.next = newNext;
			newNext.previous = temp;
			newNext.next = oldNext;
			oldNext.previous = newNext;

			if (offset <= nodeSize / 2) {
				temp.addItem(offset, item);
			}
			if (offset > nodeSize / 2) {
				newNext.addItem((offset - nodeSize / 2), item);
			}

		}
		size++;
	}

	@Override
	public E remove(int pos)
	{
		if (pos < 0 || pos > size) { throw new IndexOutOfBoundsException(); }

		NodeInfo inf = find(pos);
		Node temp = inf.node;
		int offset = inf.offset;
		E val = temp.data[offset];

		if (temp.next == tail && temp.count == 1) {

			Node before = temp.previous;
			before.next = temp.next;
			temp.next.previous = before;
			temp = null;
		}

		else if (temp.next == tail || temp.count > nodeSize / 2) {
			temp.removeItem(offset);
		}

		else {

			temp.removeItem(offset);
			Node after = temp.next;

			if (after.count > nodeSize / 2) {
				temp.addItem(after.data[0]);
				after.removeItem(0);
			}
			else if (after.count <= nodeSize / 2) {
				for (int i = 0; i < after.count; i++) {
					temp.addItem(after.data[i]);
				}
				temp.next = after.next;
				after.next.previous = temp;
				after = null;
			}
		}
		size--;
		return val;
	}

	/**
	 * Sort all elements in the stout list in the NON-DECREASING order. You may do the following. 
	 * Traverse the list and copy its elements into an array, deleting every visited node along 
	 * the way.  Then, sort the array by calling the insertionSort() method.  (Note that sorting 
	 * efficiency is not a concern for this project.)  Finally, copy all elements from the array 
	 * back to the stout list, creating new nodes for storage. After sorting, all nodes but 
	 * (possibly) the last one must be full of elements.  
	 *  
	 * Comparator<E> must have been implemented for calling insertionSort().    
	 */
	public void sort()
	{
		E[] toSort = (E[]) new Comparable[size];

		int tempIndex = 0;
		Node temp = head.next;
		while (temp != tail) {
			for (int i = 0; i < temp.count; i++) {
				toSort[tempIndex] = temp.data[i];
				tempIndex++;
			}
			temp = temp.next;
		}

		head.next = tail;
		tail.previous = head;

		insertionSort(toSort, new Comparator<E> () {
			public int compare(E arg0, E arg1) {
				// TODO Auto-generated method stub
				return arg0.compareTo(arg1);
			}

		});
		size = 0;
		for (int i = 0; i < toSort.length; i++) {
			add(toSort[i]);
		}
	}

	/**
	 * Sort all elements in the stout list in the NON-INCREASING order. Call the bubbleSort()
	 * method.  After sorting, all but (possibly) the last nodes must be filled with elements.  
	 *  
	 * Comparable<? super E> must be implemented for calling bubbleSort(). 
	 */
	public void sortReverse() 
	{
		E[] toReverseSort = (E[]) new Comparable[size];

		int tempIndex = 0;
		Node temp = head.next;
		while (temp != tail) {
			for (int i = 0; i < temp.count; i++) {
				toReverseSort[tempIndex] = temp.data[i];
				tempIndex++;
			}
			temp = temp.next;
		}

		head.next = tail;
		tail.previous = head;

		bubbleSort(toReverseSort);
		size = 0;
		for (int i = 0; i < toReverseSort.length; i++) {
			add(toReverseSort[i]);
		}
	}

	@Override
	public Iterator<E> iterator()
	{
		return (Iterator)(new StoutListIterator());
	}

	@Override
	public ListIterator<E> listIterator()
	{
		return (ListIterator) (new StoutListIterator());
	}

	@Override
	public ListIterator<E> listIterator(int index)
	{ 
		return (ListIterator) (new StoutListIterator(index));
	}

	/**
	 * Returns a string representation of this list showing
	 * the internal structure of the nodes.
	 */
	public String toStringInternal()
	{
		return toStringInternal(null);
	}

	/**
	 * Returns a string representation of this list showing the internal
	 * structure of the nodes and the position of the iterator.
	 *
	 * @param iter
	 *            an iterator for this list
	 */
	public String toStringInternal(ListIterator<E> iter) 
	{
		int count = 0;
		int position = -1;
		if (iter != null) {
			position = iter.nextIndex();
		}

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Node current = head.next;
		while (current != tail) {
			sb.append('(');
			E data = current.data[0];
			if (data == null) {
				sb.append("-");
			} else {
				if (position == count) {
					sb.append("| ");
					position = -1;
				}
				sb.append(data.toString());
				++count;
			}

			for (int i = 1; i < nodeSize; ++i) {
				sb.append(", ");
				data = current.data[i];
				if (data == null) {
					sb.append("-");
				} else {
					if (position == count) {
						sb.append("| ");
						position = -1;
					}
					sb.append(data.toString());
					++count;

					// iterator at end
					if (position == size && count == size) {
						sb.append(" |");
						position = -1;
					}
				}
			}
			sb.append(')');
			current = current.next;
			if (current != tail)
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}


	private NodeInfo find(int pos) {

		Node temp = head.next;
		int currPos = 0;
		while (temp != tail) {
			if (currPos + temp.count <= pos) {
				currPos += temp.count;
				temp = temp.next;
				continue;
			}

			NodeInfo nodeInfo = new NodeInfo(temp, pos - currPos);
			return nodeInfo;

		}
		return null;
	}

	/**
	 * Node type for this list.  Each node holds a maximum
	 * of nodeSize elements in an array.  Empty slots
	 * are null.
	 */
	private class Node
	{
		/**
		 * Array of actual data elements.
		 */
		// Unchecked warning unavoidable.
		public E[] data = (E[]) new Comparable[nodeSize];

		/**
		 * Link to next node.
		 */
		public Node next;

		/**
		 * Link to previous node;
		 */
		public Node previous;

		/**
		 * Index of the next available offset in this node, also 
		 * equal to the number of elements in this node.
		 */
		public int count;

		/**
		 * Adds an item to this node at the first available offset.
		 * Precondition: count < nodeSize
		 * @param item element to be added
		 */
		void addItem(E item)
		{
			if (count >= nodeSize)
			{
				return;
			}
			data[count++] = item;
			//useful for debugging
			// System.out.println("Added " + item.toString() + " at index " + count + " to node "  + Arrays.toString(data));
		}

		/**
		 * Adds an item to this node at the indicated offset, shifting
		 * elements to the right as necessary.
		 * 
		 * Precondition: count < nodeSize
		 * @param offset array index at which to put the new element
		 * @param item element to be added
		 */
		void addItem(int offset, E item)
		{
			if (count >= nodeSize)
			{
				return;
			}
			for (int i = count - 1; i >= offset; --i)
			{
				data[i + 1] = data[i];
			}
			++count;
			data[offset] = item;
			//useful for debugging 
			// System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + Arrays.toString(data));
		}

		/**
		 * Deletes an element from this node at the indicated offset, 
		 * shifting elements left as necessary.
		 * Precondition: 0 <= offset < count
		 * @param offset
		 */
		void removeItem(int offset)
		{
			E item = data[offset];
			for (int i = offset + 1; i < nodeSize; ++i)
			{
				data[i - 1] = data[i];
			}
			data[count - 1] = null;
			--count;
		}    
	}

	private class StoutListIterator implements ListIterator<E>
	{

		/**
		 * The index inside the array
		 */
		int inside;

		/**
		 * The "real" index, the index of the entire 
		 */
		int outside;

		/**
		 * The current node
		 */
		Node currNode;

		/**
		 * Shows the direction of movement, for deleting, adding and setting
		 */
		int dir;


		/**
		 * Default constructor 
		 */
		public StoutListIterator()
		{
			this(0);
		}

		/**
		 * Constructor finds node at a given position.
		 * @param pos
		 */
		public StoutListIterator(int pos)
		{
			if (pos > size) {
				throw new IllegalArgumentException();
			}

			currNode = head.next;
			dir = 0;

			if (pos != 0) {
				int counter = currNode.count;
				while (counter < pos) {
					currNode = currNode.next;
					counter += nodeSize;
				}
				inside = pos % nodeSize - 1;

				outside = pos - 1;
				for (int i = 0; i < pos - counter; i++) {
					next();
				}
			}
			else if (head.next == tail) {

			}
			else {
				inside = 0;
			}

		}

		@Override
		public boolean hasNext()
		{
			return (outside < size);
		}

		@Override
		public E next()
		{
			if (!hasNext()) {
				throw new NoSuchElementException();
			}


			if (inside < nodeSize) {
				for (int i = inside; i < nodeSize; i ++) {
					if (currNode.data[i] != null) {
						outside ++;
						dir = 1;
						inside ++;
						return currNode.data[i];
					}
				}
			}

			if (currNode.next == tail) {
				dir = 1;
				return null;
			}

			while (currNode.next != tail && currNode.next != null) {
				currNode = currNode.next;
				inside = 0;
				for (int i = inside; i < nodeSize; i ++) {
					if (currNode.data[i] != null) {
						outside ++;
						dir = 1;
						inside = i;
						inside ++;
						return currNode.data[i];
					}
				}
			}

			dir = 1;
			return null;
		}

		@Override
		public void remove()
		{
			if (dir == 1) {
				StoutList.this.remove(outside - 1);
				dir = -1;
				outside--;
				if (outside < 0) {
					outside = 0;
				}
			} else if (dir == -1) {
				StoutList.this.remove(outside);
				dir = -1;
			} else {
				throw new IllegalStateException();
			}
		}

		@Override
		public boolean hasPrevious() {
			return outside > 0;
		}

		@Override
		public E previous() {
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}

			dir = -1;

			// check the current node
			// if nothing in there, move backward and check
			if (currNode.count > 0) {
				if (inside >= 0) {
					E temp = currNode.data[inside];
					inside --;
					outside--;
					return temp;
				}
			}

			currNode = currNode.previous;


			for (int i = nodeSize - 1; i > -1; i--) {
				if (currNode.data[i] != null) {
					inside = i;
					outside --;
					return currNode.data[i];
				}
			}

			return null;
		}

		@Override
		public int nextIndex() {
			return outside;
		}

		@Override
		public int previousIndex() {
			return outside - 1;
		}

		@Override
		public void set(E e) {
			
			if (dir == 1) {
				NodeInfo nodeInfo = find(outside - 1);
				nodeInfo.node.data[nodeInfo.offset] = e;
			} else if (dir == -1) {
				NodeInfo nodeInfo = find(outside);
				nodeInfo.node.data[nodeInfo.offset] = e;
			} else {
				throw new IllegalStateException();
			}
		}

		@Override
		public void add(E e) {

			if (e == null) { throw new NullPointerException(); }

			StoutList.this.add(outside, e);
			outside++;
			dir = -1;

		}


	}


	/**
	 * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING order. 
	 * @param arr   array storing elements from the list 
	 * @param comp  comparator used in sorting 
	 */
	private void insertionSort(E[] arr, Comparator<? super E> comp)
	{
		int n = arr.length;
		for (int i = 0; i < n; i ++) {
			int low = i;
			for (int j = i; j < n; j++) {
				if (comp.compare(arr[j], arr[low]) < 0) {
					low = j;
				}
			}
			E temp = arr[i];
			arr[i] = arr[low];
			arr[low] = temp;
		}

	}

	/**
	 * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a 
	 * description of bubble sort please refer to Section 6.1 in the project description. 
	 * You must use the compareTo() method from an implementation of the Comparable 
	 * interface by the class E or ? super E. 
	 * @param arr  array holding elements from the list
	 */
	private void bubbleSort(E[] arr)
	{
		int n = arr.length;

		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - i - 1; j++) {
				if (arr[j].compareTo(arr[j + 1]) < 0) {
					E temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
				}
			}
		}
	}


	private class NodeInfo
	{
		public Node node;
		public int offset;
		public NodeInfo(Node node, int offset)
		{
			this.node = node;
			this.offset = offset;
		}


	}



}