package it.unibo.model
import Tiling.*

class Terrain extends Scenario:


  private def getNoise(x: Int, y: Int): Double =
    1.0

  private def getTileFromNoise(noise: Double)(position: Position): Tile =
    if noise < 0.2 then
      Water(position)
    else if noise < 0.4 then
      Grass(position)
    else if noise < 0.7 then
      Rock(position)
    Lava(position)


  def initialPosition: Position = Position(0, 0)

  override def generateScenario(): Unit = {
    tiles =
      (for
        x <- 0 until Scenario.nCols
        y <- 0 until Scenario.nRows
      yield
        getTileFromNoise(getNoise(x, y))(Position(x, y))).toList
  }

