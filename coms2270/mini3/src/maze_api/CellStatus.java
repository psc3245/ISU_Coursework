package maze_api;

/**
 * Constants representing possible states of a maze cell in
 * a recursive search.
 */
public enum CellStatus
{
  NOT_STARTED, 
  SEARCHING_UP, 
  SEARCHING_DOWN, 
  SEARCHING_LEFT, 
  SEARCHING_RIGHT, 
  DEAD_END, 
  FOUND_IT
}
