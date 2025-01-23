package edu.iastate.cs311.hw4;

/*
@author Peter Collins
netid: psc3245

Your task is to implement the Dijkstra algorithm described in lecture.
You just need to complete the TODO part.
You are not allowed to modify the rest of the file except for the main() method;
you can comment out the part of main() that produces the output from Dijkstra()
so that you run the resulting code to see the output from depthFirstSearch().
It would be helpful to study all the classes and methods defined in this file.

This template is put together by Xiaoqiu Huang.
*/

import java.util.*;

/**
 * An edge class as a pair class with two components of types V and C, where
 * V is a vertex type and C is a cost type.
 */

class Edge<V, C extends Comparable<? super C> > implements Comparable<Edge<V, C>>
   {
     private V  node;
     private C  cost;

     Edge(V n, C c)
     {
       node = n;
       cost = c;
     }

     public V getVertex() { return node;}
     public C getCost() { return cost;}
     public int compareTo( Edge<V, C> other )
     {
       return cost.compareTo(other.getCost() );
     }

     public String toString()
     {
       return "<" +  node.toString() + ", " + cost.toString() + ">";
     }

     public int hashCode()
     {
       return node.hashCode();
     }

     public boolean equals(Object obj)
     {
       if(this == obj) return true;
       if((obj == null) || (obj.getClass() != this.getClass()))
        return false;
       // object must be Edge at this point
       Edge<?, ?> test = (Edge<?, ?>)obj;
       return
         (node == test.node || (node != null && node.equals(test.node)));
     }

   }

public class DiGraph<V> {

    // symbol table: key = vertex, value = set of neighboring vertices
    private HashMap<V, HashSet< Edge<V, Integer> > > map;

    // number of edges
    private int E;

    // A (key, value) pair: key = vertex, value = its predecessor pi
    private HashMap<V, V> vertexPred;

    // A path from the source to the destination
    private LinkedStack<V> path;

    // create an empty graph
    public DiGraph() {
        map = new HashMap<V, HashSet< Edge<V, Integer> > >();
    }

    // add t to f's set of neighbors.
    public void addEdge(V f, V t, Integer c) {
        if (!hasEdge(f, t)) E++;
        if (!hasVertex(f)) addVertex(f);
        map.get(f).add( new Edge<V, Integer>(t, c) );
        if (!hasVertex(t)) addVertex(t);
    }

    // add a new vertex f with no neighbors (if vertex does not yet exist)
    public void addVertex(V f) {
        if (!hasVertex(f)) map.put(f, new HashSet< Edge<V, Integer> >());
    }

    // return iterator over all vertices in graph
    public Iterable<V> vertices()
    {
        return map.keySet();
    }

    // return an iterator over the neighbors of vertex f
    public Iterable<Edge<V, Integer>> adjacentTo(V f) {
        // return empty set if vertex isn't in graph
        if (!hasVertex(f)) return new HashSet<Edge<V, Integer>>();
        else               return map.get(f);
    }

    // is f a vertex in the graph?
    public boolean hasVertex(V f) {
        return map.containsKey(f);
    }

    // is v-w an edge in the graph?
    public boolean hasEdge(V f, V t) {
        if (!hasVertex(f)) return false;
        for (Edge<V, Integer> e : map.get(f))
	{
	  if ( t.equals( e.getVertex() ) )
	    return true;
	}
	return false;
    }

    // not very efficient, intended for debugging only
    public String toString() {
        String s = "";
        for (V f : map.keySet() ) {
            s += f.toString() + ": ";
            for (Edge<V, Integer> e : map.get(f)) {
                s += "[" + e.getVertex().toString() + ", "
		         + e.getCost().toString() + "] ";
            }
            s += "\n";
        }
        return s;
    }

    public HashMap<V, V> getPred() {
	    return vertexPred;
    }

    public LinkedStack<V> getPath() {
	    return path;
    }

    // Implements the Dijkstra algorithm using the following data structures.
    // Specifically, you need to compute dist, pred, and path correctly.
    public static <V> HashMap<V, Integer> Dijkstra(DiGraph<V> G, V source, V dst)
    {
      // (key, value) pairs where key = vertex, value = shortest distance from source to vertex
      HashMap<V, Integer> dist = new HashMap<V, Integer>();

      // (key, value) pairs where key = vertex, value = its predecessor pi
      HashMap<V, V> pred = new HashMap<V, V>();

      // A min heap of Edge objects
      // For each edge (u, v) with cost c,
      // an Edge object is created by using 'new Edge<V, Integer>(v, c)'
      // and added to the value set for the key vertex u, see the method 'addEdge()'.
      Heap<Edge<V, Integer>> priq = new Heap<Edge<V, Integer>>();

      // A set of vertices whose shortest path distances have been computed
      HashSet<V> vset = new HashSet<V>();

      // A stack of vertices from the source (top) to the destination dst (bottom)
      LinkedStack<V> path = new LinkedStack<V>();
      // Please do not modify the code above.

      // TODO  Your code goes here;
      // this is the only place to write your own code for this assignment.
      
      // set all distances to infinity and starting node distance to 0
      for (V v: G.vertices()) {
    	  dist.put(v, Integer.MAX_VALUE);
      }
      
      dist.put(source, 0);
      
      // add source to priq with distance 0
      priq.add(new Edge<V, Integer>(source, 0));
      
      // begin a while loop to use to move through the graph
      while (!priq.isEmpty()) {
    	  // pop priq to get current node
    	  V curr = priq.getMin().getVertex();
    	  // be sure to remove it to avoid infinite loop
    	  priq.removeMin();
    	  
    	  // add curr to the set of nodes
    	  vset.add(curr);
    	  
    	  // get and loop through neighbors
    	  for (Edge<V, Integer> e : G.adjacentTo(curr)) {
    		  
    		  // get next node
    		  V next = e.getVertex();
    		  
    		  // compute distance from current to next
    		  int t = dist.get(curr) + e.getCost();
    		  
    		  // if this new distance is lower than the current distance, set the distance to the new low
    		  // add the edge to predecessor set to keep track
    		  // add the edge to the priority queue to continue exploring
    		  if (t < dist.get(next)) {
    			  
    			  dist.put(next, t);
    			  pred.put(next, curr);
    			  priq.add(new Edge<V, Integer>(next, t));
    		  
    		  }
    	  }
      }
      
      // after this loop is complete, the priq will be empty
      // all nodes should have been assigned their lowest distance from source to that node
      
      // form the optimal path using the destination node and going backwards
      
      V curr = dst;
      while (curr != null) {
    	  path.push(curr);
    	  curr = pred.get(curr);
      }
    
      // Please do not modify the code below, except for the main() method.
      G.vertexPred = pred; 
      G.path = path;
      return dist;
    }

    // It visits the vertices of a graph
    // in depth-first traversal, produces a depth-first forest, and
    // generates a list of vertices in a topological order.
    public static <V> void depthFirstSearch(DiGraph<V> aGraph)
    {
      HashMap<V, String> color = new HashMap<V, String>();
      HashMap<V, V> pred = new HashMap<V, V>();
      LinkedStack<V> topoOrder = new LinkedStack<V>();
      System.out.println("Initialization");
      System.out.println();
      for ( V w : aGraph.vertices() )
        {
          color.put(w, "white"); // unreached
          pred.put(w, null);
          System.out.println("Vertex: " + w + "  Color: " + color.get(w) + "  Pred: null");
        }

      System.out.println();
      for ( V w : aGraph.vertices() )
        if ( color.get(w).equals( "white" ) )
	{
          recvisitDFS(aGraph, w, color, pred, topoOrder);
          System.out.println();
	}

      System.out.println("\nDFS Forest");  // This display part is omitted in class.
      for ( V w : aGraph.vertices() )
       if ( pred.get(w) == null )
         System.out.println( "The root of a DFS tree: " + w.toString() );
       else
         System.out.println( "Tree edge: "
	       + pred.get(w).toString()
	       + "->" +  w.toString() );

      System.out.println( "Topological Sorting:");
      while ( ! topoOrder.isEmpty() )
         System.out.print(" " +  topoOrder.pop().toString() );
      System.out.println();
    }

    /* This recursive method is covered in class. */
    private static <V> void recvisitDFS(DiGraph<V> aGraph, V s,
            HashMap<V, String> color, HashMap<V, V> pred, LinkedStack<V> topoOrder)
    {
      color.put(s, "gray"); // reached but not processed
      System.out.println("A search is started at vertex " + s + ".");
      String tmp = pred.get(s) == null ? "null" : pred.get(s).toString();
      System.out.println("Vertex: " + s + "  Color: " + color.get(s) + "  Pred: " + tmp );
      for ( Edge<V, Integer> tup: aGraph.adjacentTo(s) )
      {
	V w = tup.getVertex();
	if ( color.get(w).equals( "white" ) )
	{
          pred.put(w, s);
	  recvisitDFS(aGraph, w, color, pred, topoOrder);
	}
      }
      color.put(s, "black"); // processed
      topoOrder.push(s);
      System.out.println("The search is complted at vertex " + s + ".");
      tmp = pred.get(s) == null ? "null" : pred.get(s).toString();
      System.out.println("Vertex: " + s + "  Color: " + color.get(s) + "  Pred: " + tmp );
    }

    public static void main(String[] args) {

        DiGraph<String> G4 = new DiGraph<String>();
        G4.addEdge("A", "B", 11);
        G4.addEdge("A", "C", 3);
        G4.addEdge("A", "D", 8);
        G4.addEdge("B", "C", 4);
        G4.addEdge("B", "E", 5);
        G4.addEdge("C", "D", 2);
        G4.addEdge("C", "E", 2);
        G4.addEdge("C", "H", 1);
        G4.addEdge("E", "F", 1);
        G4.addEdge("E", "G", 4);
        G4.addEdge("E", "H", 6);
        G4.addEdge("F", "B", 3);
        G4.addEdge("F", "G", 2);
        G4.addEdge("G", "H", 1);
        G4.addEdge("H", "D", 1);

        HashMap<String, Integer> dist = Dijkstra(G4, "A", "D");

        HashMap<String, String> pred = G4.getPred();
        for ( String w: G4.vertices() )
        {
System.out.println( "Vertex: " + w.toString() +
                    " Dist: " + dist.get(w) +
		    " Pred: " + pred.get(w) );
        }

        LinkedStack<String> path = G4.getPath();
System.out.println( "Shortest path from the source to the destination:");
	while ( ! path.isEmpty() )
      		System.out.println( path.pop() );
System.out.println( "Its cost: " + dist.get("D"));

        DiGraph<String> G8 = new DiGraph<String>();
	G8.addEdge("A", "D", 8);
	G8.addEdge("A", "E", 5);
	G8.addEdge("B", "E", 10);
	G8.addEdge("C", "S", 11);
	G8.addEdge("D", "G", 7);
	G8.addEdge("F", "B", 3);
	G8.addEdge("F", "C", 2);
	G8.addEdge("G", "F", 2);
	G8.addEdge("S", "E", 9);
	G8.addEdge("S", "B", 10);
        System.out.println();
        System.out.println("Depth First Search");
        depthFirstSearch(G8);
    }
}

/* Example output
 
Vertex: A Dist: 0 Pred: null
Vertex: B Dist: 9 Pred: F
Vertex: C Dist: 3 Pred: A
Vertex: D Dist: 5 Pred: C
Vertex: E Dist: 5 Pred: C
Vertex: F Dist: 6 Pred: E
Vertex: G Dist: 8 Pred: F
Vertex: H Dist: 4 Pred: C
Shortest path from the source to the destination:
A
C
D
Its cost: 5

Depth First Search
Initialization

Vertex: A  Color: white  Pred: null
Vertex: B  Color: white  Pred: null
Vertex: C  Color: white  Pred: null
Vertex: S  Color: white  Pred: null
Vertex: D  Color: white  Pred: null
Vertex: E  Color: white  Pred: null
Vertex: F  Color: white  Pred: null
Vertex: G  Color: white  Pred: null

A search is started at vertex A.
Vertex: A  Color: gray  Pred: null
A search is started at vertex D.
Vertex: D  Color: gray  Pred: A
A search is started at vertex G.
Vertex: G  Color: gray  Pred: D
A search is started at vertex F.
Vertex: F  Color: gray  Pred: G
A search is started at vertex B.
Vertex: B  Color: gray  Pred: F
A search is started at vertex E.
Vertex: E  Color: gray  Pred: B
The search is complted at vertex E.
Vertex: E  Color: black  Pred: B
The search is complted at vertex B.
Vertex: B  Color: black  Pred: F
A search is started at vertex C.
Vertex: C  Color: gray  Pred: F
A search is started at vertex S.
Vertex: S  Color: gray  Pred: C
The search is complted at vertex S.
Vertex: S  Color: black  Pred: C
The search is complted at vertex C.
Vertex: C  Color: black  Pred: F
The search is complted at vertex F.
Vertex: F  Color: black  Pred: G
The search is complted at vertex G.
Vertex: G  Color: black  Pred: D
The search is complted at vertex D.
Vertex: D  Color: black  Pred: A
The search is complted at vertex A.
Vertex: A  Color: black  Pred: null


DFS Forest
The root of a DFS tree: A
Tree edge: F->B
Tree edge: F->C
Tree edge: C->S
Tree edge: A->D
Tree edge: B->E
Tree edge: G->F
Tree edge: D->G
Topological Sorting:
 A D G F C S B E
*/

interface PurePriorityQueue<E>
{
  int size();
  boolean isEmpty();
  void add(E element);
  // Returns a high-priority element without removing it.
  E getMin();
  // Removes a high-priority element.
  E removeMin();
}

class Heap<E extends Comparable<? super E>>
   implements PurePriorityQueue<E>
{
  private static final int INIT_CAP = 10;
  private ArrayList<E> list;

  public Heap()
  {
    list = new ArrayList<E>(INIT_CAP);
  }

  public int size()
  {
    return list.size();
  }

  public boolean isEmpty()
  {
    return list.isEmpty();
  }

  public String toString()
  {
    return list.toString();
  }

  public void add(E element)
  {
    if ( element == null )
      throw new NullPointerException("add");
    list.add(element); // append it to the end of the list
    percolateUp(); // move it up to the proper place
  }

  // move the last element up to the proper place.
  private void percolateUp()
  {
     int child = list.size() - 1; // last element in the list
     int parent;
     while ( child > 0 )
     {
       parent = (child - 1) / 2; // use the (j-1)/2 formula
       if ( list.get(child).compareTo(list.get(parent)) >= 0 )
          break;
       swap(parent, child);
       child = parent;
     }
  }

  private void swap(int parent, int child)
  {
    E tmp = list.get(parent);
    list.set( parent, list.get(child) );
    list.set(child, tmp);
  }

  public E getMin()
  {
    if ( list.isEmpty() )
      throw new NoSuchElementException();
    return list.get(0);
  }

  public E removeMin()
  {
    if ( list.isEmpty() )
      throw new NoSuchElementException();
    E minElem = list.get(0); // get the min element at the root
    list.set(0, list.get(list.size() - 1) ); // copy the last element to the root
    list.remove( list.size() - 1 ); // remove the last element from the list
    if ( ! list.isEmpty() )
     percolateDown(0); // move the element at the root down to the proper place
    return minElem;
  }

  // Move the element at index start down to the proper place.
  private void percolateDown(int start)
  {
    if ( start < 0 || start >= list.size() )
      throw new RuntimeException("start < 0 or >= n");
    int parent = start;
    int child = 2 * parent + 1; // use the 2*i+1 formula
    while ( child < list.size() )
    {
      if ( child + 1 < list.size() &&
           list.get(child).compareTo(list.get(child + 1)) > 0 )
          child++; // select the smaller child
      if ( list.get(child).compareTo(list.get(parent)) >= 0 )
          break; // reach the proper place
      swap(parent, child);
      parent = child;
      child = 2 * parent + 1;
    }
  }
} // Heap

/**
* A stack is a special list of elements in which
* elements are added and removed at one end.
* A stack interface with common stack operations:
**/

interface PureStack<E>
{
  int size();
  boolean isEmpty();

// Adds an element to the top.
  void push(E element);

// Removes and returns the top element.
   E pop();

// Returns the top element without removing it.
   E peek();
}

/**
*
* A simple class that implements the PureStack interface with linked nodes.
* Each method of the class takes O(1) time.
*
a**/

class LinkedStack<E> implements PureStack<E>
{
  private class SNode
  {
    public E data;
    public SNode link;
  }

  private SNode top;    // refers to the top node of the stack.
  private int numItems; // number of elements in the stack.

  public LinkedStack()
  {
    top = null;
    numItems = 0;
  }

  public int size()
  {
    return numItems;
  }

  public boolean isEmpty()
  {
     return numItems == 0;
  }

  public void push(E element)
  {
    SNode toAdd = new SNode();
    toAdd.data = element;
    toAdd.link = top;
    top = toAdd;
    numItems++;
  }

  public E pop()
  {
    if ( top == null )
      throw new NoSuchElementException();
    E returnVal = top.data;
    top = top.link;
    if ( numItems <= 0 )
      throw new RuntimeException("An incorrect number of elements");
    numItems--;
    return returnVal;
  }

  public E peek()
  {
    if ( top == null )
      throw new NoSuchElementException();
    return top.data;
  }
} // LinkedStack
