package it.unibo.model

import it.unibo.model.Tiling.{Floor, Position, Tile, Wall}

import scala.util.Random

class MazeScenario extends Scenario:
  def initialPosition: Position = Position(0,0,false)
  // var agent: Agent = Agent(initialPosition)
  // var tiles: List[Tile] = List()
  override def resetAgent(): Unit = agent = Agent(initialPosition)

  override def generateScenario(): Unit =
    val rows = Scenario.nRows
    val cols = Scenario.nCols
    val visited = Array.fill(rows, cols)(false)
    var mazeTiles: Map[Position, Tile] = Map()

    def inBounds(x: Int, y: Int) = 
      x >= 0 && y >= 0 && x < rows && y < cols

    def neighbors(x: Int, y: Int) =
      Random.shuffle(List(
        (x + 1, y),
        (x - 1, y),
        (x, y + 1),
        (x, y - 1)
      )).filter((nx, ny) => inBounds(nx, ny) && !visited(nx)(ny))

    // inizializza tutto come Wall
    for
      x <- 0 until rows
      y <- 0 until cols
    do
      mazeTiles += Position(x, y) -> Wall(Position(x, y))

    // carving: sovrascrive con Floor
    def carve(x: Int, y: Int): Unit =
      visited(x)(y) = true
      mazeTiles += Position(x, y) -> Floor(Position(x, y))
      for (nx, ny) <- neighbors(x, y) do
        if !visited(nx)(ny) then carve(nx, ny)

    carve(0, 0)
    tiles = mazeTiles.values.toList