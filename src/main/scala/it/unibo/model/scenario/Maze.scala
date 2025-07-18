package it.unibo.model.scenario

import it.unibo.model.fundamentals.{Position, Tile}
import it.unibo.model.fundamentals.Tiling.{Floor, Wall}

import scala.util.Random

/**
 * A randomly generated maze scenario using recursive backtracking.
 *
 * The maze is generated on a grid with odd dimensions, where:
 * - Rooms are placed at odd positions.
 * - Walls are placed at even positions.
 *
 * @param nRows the number of rows in the visible grid
 * @param nCols the number of columns in the visible grid
 */
class Maze(nRows: Int, nCols: Int) extends EmptyScenario(nRows, nCols):
  private val logicalRows = nRows / 2
  private val logicalCols = nCols / 2
  private val gridRows = 2 * logicalRows + 1
  private val gridCols = 2 * logicalCols + 1
  private val exitX = gridCols - 1
  private val exitY = gridRows - 2

  /**
   * Generates the maze by carving paths recursively.
   * It overrides the base scenario generation method.
   */
  override def generate(): Unit =
    val allWalls: Map[Position, Tile] =
      (for
        x <- 0 until gridCols
        y <- 0 until gridRows
      yield Position(x, y) -> Wall(Position(x, y))
        ).toMap

    /**
     * Checks whether the given logical coordinates are within maze bounds.
     *
     * @param row logical row index
     * @param col logical column index
     * @return true if within bounds, false otherwise
     */
    def inBounds(row: Int, col: Int): Boolean =
      row >= 0 && col >= 0 && row < logicalRows && col < logicalCols

    /**
     * Returns the list of neighboring logical cells in randomized order.
     *
     * @param row current logical row
     * @param col current logical column
     * @return a list of neighbor cell coordinates and direction deltas
     */
    def neighbors(row: Int, col: Int): List[(Int, Int, Int, Int)] =
      Random.shuffle(List(
        (row - 1, col, -1, 0),
        (row + 1, col, 1, 0),
        (row, col - 1, 0, -1),
        (row, col + 1, 0, 1)
      ))

    /**
     * Recursively carves out the maze by turning walls into floor tiles
     * starting from the given logical cell.
     *
     * @param row     current logical row
     * @param col     current logical column
     * @param visited set of already visited logical cells
     * @param maze    current state of the maze grid
     * @return updated set of visited cells and updated maze
     */
    def carve(row: Int, col: Int, visited: Set[(Int, Int)], maze: Map[Position, Tile]): (Set[(Int, Int)], Map[Position, Tile]) =
      val updatedVisited = visited + ((row, col))
      val x = 2 * col + 1
      val y = 2 * row + 1
      val updatedMaze = maze + (Position(x, y) -> Floor(Position(x, y)))
      neighbors(row, col).foldLeft((updatedVisited, updatedMaze)) {
        case ((currentVisited, currentMaze), (neighborRow, neighborCol, deltaRow, deltaCol))
          if inBounds(neighborRow, neighborCol) && !currentVisited.contains((neighborRow, neighborCol)) =>
          val wallX = x + deltaCol
          val wallY = y + deltaRow
          val carvedMaze = currentMaze + (Position(wallX, wallY) -> Floor(Position(wallX, wallY)))
          carve(neighborRow, neighborCol, currentVisited, carvedMaze)
        case ((visited, maze), _) => (visited, maze)
      }

    val (_, carvedMaze) = carve(0, 0, Set.empty, allWalls)

    val finalMaze = carvedMaze + (Position(exitX, exitY) -> Floor(Position(exitX, exitY)))
    _tiles = finalMaze.values.toList
