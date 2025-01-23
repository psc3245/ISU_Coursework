package api;

import java.util.ArrayList;

/**
 * A Line represents a sequence of row/column locations from a 2D grid.
 * The Line object also permanently stores two designated locations, called
 * the endpoints, even when the sequence is empty, and has an integer id.  
 * The sequence is initially empty, and cells are added to the
 * sequence using the <code>add()</code> method. 
 */
public class Line
{
  /**
   * The sequence of cells in this Line.
   */
  private ArrayList<Location> cells;
  
  /**
   * The two endpoints associated with this Line.
   */
  private Location[] endpoints;
  
  /**
   * The id for this Line.
   */
  private int id;
  
  /**
   * Constructs a Line with the given id.  Note that the Line cannot be used
   * until two endpoints are added.
   * @param id
   *   any non-negative integer
   */
  public Line(int id)
  {
    endpoints = new Location[2];
    cells = new ArrayList<Location>();
    this.id = id;
  }
  
  /**
   * Constructs a Line with the given id and endpoints.
   * @param id
   *   a non-negative integer
   * @param e0
   *   Location for one endpoint
   * @param e1
   *   Location for other endpoint
   */
  public Line(int id, Location e0, Location e1)
  {
    endpoints = new Location[2];
    endpoints[0] = e0;
    endpoints[1] = e1;
    cells = new ArrayList<Location>();
    this.id = id;
  }
  
  /**
   * Adds the given Location as an endpoint for this cell. If there are currently 
   * no endpoints, the given location becomes endpoint 0; if there is one
   * endpoint, the given location becomes endpoint 1, and if there are already
   * two endpoints for this cell, the method throws an IllegalStateException.
   * @param e
   *   location to be added as an endpoint
   */
  public void addEndpoint(Location e)
  {
    if (endpoints[0] == null)
    {
      endpoints[0] = e;
    }
    else if (endpoints[1] == null)
    {
      endpoints[1] = e;
    }
    else
    {
      throw new IllegalStateException("Attempt to add third endpoint to Line object. ");
    }
  }
  
  /**
   * Returns the list of Locations in this line.  Clients should
   * not directly modify this list.
   * @return
   *   list of locations in this line
   */
  public ArrayList<Location> getCells()
  {
    return cells;
  }
  
  /**
   * Clears the list of cells in this line.
   */
  public void clear()
  {
    cells.clear();
  }
  
  /**
   * Adds the given Location to the list of cells for this line. 
   * @param loc
   *   given location
   */
  public void add(Location loc)
  {
    cells.add(loc);
  }
  
  /**
   * Returns one of the endpoints (0 or 1) associated with this line.
   * This method will return null if the corresponding endpoint 
   * has not been initialized.
   * @param i
   *   index (0 or 1) of the endpoint to return
   * @return
   *   an endpoint associated with this flow
   */
  public Location getEndpoint(int i)
  {
    return endpoints[i];
  }
  
  /**
   * Returns the id of this line.
   * @return
   *  id of this line
   */
  public int getId()
  {
    return id;
  }
  
  /**
   * Returns the last location in this line's list of locations, or null
   * if the list is empty.
   * @return
   *   last location in the list
   */
  public Location getLast()
  {
    if (cells.size() == 0)
    {
      return null;
    }
    return cells.get(cells.size() - 1);
  }
  
  /**
   * Determines whether this line's list of cells begins and ends at 
   * its two endpoints.
   * @return
   *   true if the line is connected
   * @throws IllegalStateException
   *   if this method is called without both endpoints set
   */
  public boolean isConnected()
  {
    if (endpoints[0] == null || endpoints[1] == null)
    {
      throw new IllegalStateException("Attempt to check connected status for a Line without two endpoints. ");
    }
    if (cells.size() >= 2)
    {
      Location first = cells.get(0);
      Location last = cells.get(cells.size() - 1);
      if (first.equals(endpoints[0]) && last.equals(endpoints[1]) ||
          first.equals(endpoints[1]) && last.equals(endpoints[0]))
      {
        return true;
      }         
    }
    return false;
  }
  
  /**
   * Determines whether this Line is equal to the given object. Two lines
   * are equal if they have the same id, the same list of Locations, and
   * the same endpoints.  Two lines are considered to have the same endpoints
   * if:
   * <ul>
   *   <li>both endpoints are null for both lines, or
   *   <li>the first endpoint matches and the second endpoint is null for both lines, or
   *   <li>both endpoints are non-null for both lines and and both match, possibly
   *   in the opposite order  
   * </ul>
   */
  public boolean equals(Object obj)
  {
    if (obj == null || obj.getClass() != this.getClass())
    {
      return false;
    }
    Line other = (Line) obj;
 
    if (id != other.id)
    {
      return false;
    }
    
    boolean endpointsMatch;
    if (endpoints[0] == null)
    {
      // if first endpoint is null, second has to be null too
      endpointsMatch = other.endpoints[0] == null && 
      endpoints[1] == null && other.endpoints[1] == null;
    }
    else 
    {
      if (endpoints[1] == null)
      {
        // second one is null, so first one has to match
        endpointsMatch =  endpoints[0].equals(other.endpoints[0]) &&
                          other.endpoints[1] == null;
      }
      else
      {
        // both non null, so both match in either order
        endpointsMatch = (endpoints[0].equals(other.endpoints[0]) && 
                          endpoints[1].equals(other.endpoints[1])) ||                    
                         (endpoints[0].equals(other.endpoints[1]) && 
                              endpoints[1].equals(other.endpoints[0]));
      }
    }
    
    // use ArrayList .equals method to check location list
    return endpointsMatch && cells.equals(other.cells);
    
  }
  
  /**
   * Returns a string representation of this line in the form:
   * <p>
   * id: {endpoint 0, endpoint 1} [list of locations]
   */
  @Override
  public String toString()
  {
    char c = '?';
    if (id >= 0 && id < StringUtil.COLOR_CODES.length)
    {
      c = StringUtil.COLOR_CODES[id];
    }
    return "id " + id +  "(" + c + "): {" + endpoints[0] + ", " + endpoints[1] + "} " + 
        cells.toString();    
  }
}
