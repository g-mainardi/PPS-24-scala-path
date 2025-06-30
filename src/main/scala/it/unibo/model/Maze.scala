package it.unibo.model

import it.unibo.model.Tiling.{Floor, Position, Tile, Wall}
import scala.util.Random

class Maze extends Scenario:

  def initialPosition: Position = Position(1, 1, false)

  override def resetAgent(): Unit = agent = Agent(initialPosition)

  override def generateScenario(): Unit =
    val logicalRows = Scenario.nRows / 2
    val logicalCols = Scenario.nCols / 2
    val gridRows = 2 * logicalRows + 1
    val gridCols = 2 * logicalCols + 1

    val visited = Array.fill(logicalRows, logicalCols)(false)
    var mazeTiles: Map[Position, Tile] = Map()

    // Initialize as walls
    for
      x <- 0 until gridRows
      y <- 0 until gridCols
    do
      mazeTiles += Position(x, y) -> Wall(Position(x, y))

    def inBounds(row: Int, col: Int): Boolean =
      row >= 0 && col >= 0 && row < logicalRows && col < logicalCols

    def neighbors(row: Int, col: Int): List[(Int, Int, Int, Int)] =
      Random.shuffle(List(
        (row - 1, col, -1, 0), // nord
        (row + 1, col, 1, 0), // sud
        (row, col - 1, 0, -1), // ovest
        (row, col + 1, 0, 1) // est
      )).filter((neighborRow, neighborCol, _, _) => inBounds(neighborRow, neighborCol) && !visited(neighborRow)(neighborCol))

    def carve(row: Int, col: Int): Unit =
      visited(row)(col) = true
      val x = 2 * row + 1
      val y = 2 * col + 1
      mazeTiles += Position(x, y) -> Floor(Position(x, y)) // room

      for (neighborRow, neighborCol, diffRow, diffCol) <- neighbors(row, col) do
        if !visited(neighborRow)(neighborCol) then
          val wallX = x + diffRow
          val wallY = y + diffCol
          mazeTiles += Position(wallX, wallY) -> Floor(Position(wallX, wallY))
          carve(neighborRow, neighborCol)

    carve(0, 0)

    // add exit
    val exitX = gridRows - 1
    val exitY = gridCols - 2
    mazeTiles += Position(exitX, exitY) -> Floor(Position(exitX, exitY))

    println(s"exit at: (${exitX}, ${exitY})")

    tiles = mazeTiles.values.toList
