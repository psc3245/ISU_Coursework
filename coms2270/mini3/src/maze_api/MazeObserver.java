package maze_api;

/**
 * Interface representing a component to be notified of status
 * changes in a MazeCell.
 */
public interface MazeObserver
{
  /**
   * Indicates to observer that the status of the given cell has changed.
   * @param cell
   *   a MazeCell whose status has changed
   */
  void statusChanged(MazeCell cell);
}
