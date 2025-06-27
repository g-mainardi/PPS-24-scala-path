package it.unibo.model

import it.unibo.model.Tiling.{Floor, Position, Tile, Wall}
import scala.util.Random

class MazeScenario extends Scenario:

  def initialPosition: Position = Position(1, 1, false)
  override def resetAgent(): Unit = agent = Agent(initialPosition)

  override def generateScenario(): Unit =
    val rows = Scenario.nRows // logical rows
    val cols = Scenario.nCols // logical cols
    val gridRows = 2 * rows + 1
    val gridCols = 2 * cols + 1

    val visited = Array.fill(rows, cols)(false)
    var mazeTiles: Map[Position, Tile] = Map()

    // Initialize as walls
    for
      x <- 0 until gridRows
      y <- 0 until gridCols
    do
      mazeTiles += Position(x, y) -> Wall(Position(x, y))

    def inBounds(r: Int, c: Int): Boolean =
      r >= 0 && c >= 0 && r < rows && c < cols

    def neighbors(r: Int, c: Int): List[(Int, Int, Int, Int)] =
      Random.shuffle(List(
        (r - 1, c, -1, 0), // nord
        (r + 1, c, 1, 0),  // sud
        (r, c - 1, 0, -1), // ovest
        (r, c + 1, 0, 1)   // est
      )).filter((nr, nc, _, _) => inBounds(nr, nc) && !visited(nr)(nc))

    def carve(r: Int, c: Int): Unit =
      visited(r)(c) = true
      val x = 2 * r + 1
      val y = 2 * c + 1
      mazeTiles += Position(x, y) -> Floor(Position(x, y)) // room

      for (nr, nc, dr, dc) <- neighbors(r, c) do
        if !visited(nr)(nc) then
          // Muro intermedio da rimuovere
          val wallX = x + dr
          val wallY = y + dc
          mazeTiles += Position(wallX, wallY) -> Floor(Position(wallX, wallY))
          carve(nr, nc)

    carve(0, 0)

    // add exit
    val exitX = gridRows - 1
    val exitY = gridCols - 2
    mazeTiles += Position(exitX, exitY) -> Floor(Position(exitX, exitY))

    tiles = mazeTiles.values.toList
