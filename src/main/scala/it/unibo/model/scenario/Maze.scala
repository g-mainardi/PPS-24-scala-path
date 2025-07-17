package it.unibo.model.scenario

import it.unibo.model.fundamentals.{Position, Tile}
import it.unibo.model.fundamentals.Tiling.{Floor, Wall}

import scala.util.Random

class Maze(nRows: Int, nCols: Int) extends EmptyScenario(nRows, nCols):
  private val logicalRows = nRows / 2
  private val logicalCols = nCols / 2
  private val gridRows = 2 * logicalRows + 1
  private val gridCols = 2 * logicalCols + 1
  private val exitX = gridRows - 1
  private val exitY = gridCols - 2

  override def generate(): Unit =
    val allWalls: Map[Position, Tile] =
      (for
        x <- 0 until gridCols
        y <- 0 until gridRows
      yield Position(x, y) -> Wall(Position(x, y))
        ).toMap

    def inBounds(row: Int, col: Int): Boolean =
      row >= 0 && col >= 0 && row < logicalRows && col < logicalCols

    def neighbors(row: Int, col: Int): List[(Int, Int, Int, Int)] =
      Random.shuffle(List(
        (row - 1, col, -1, 0),
        (row + 1, col, 1, 0),
        (row, col - 1, 0, -1),
        (row, col + 1, 0, 1)
      ))

    def carve(row: Int, col: Int, visited: Set[(Int, Int)], maze: Map[Position, Tile]): (Set[(Int, Int)], Map[Position, Tile]) =
      val newVisited = visited + ((row, col))
      val x = 2 * row + 1
      val y = 2 * col + 1
      val mazeWithRoom = maze + (Position(x, y) -> Floor(Position(x, y)))

      neighbors(row, col).foldLeft((newVisited, mazeWithRoom)) {
        case ((v, m), (nr, nc, dr, dc)) if inBounds(nr, nc) && !v.contains((nr, nc)) =>
          val wallX = x + dr
          val wallY = y + dc
          val m1 = m + (Position(wallX, wallY) -> Floor(Position(wallX, wallY)))
          val (v2, m2) = carve(nr, nc, v + ((nr, nc)), m1)
          (v2, m2)

        case ((v, m), _) => (v, m)
      }

    val (_, carvedMaze) = carve(0, 0, Set.empty, allWalls)

    val finalMaze = carvedMaze + (Position(exitX, exitY) -> Floor(Position(exitX, exitY)))
    _tiles = finalMaze.values.toList
