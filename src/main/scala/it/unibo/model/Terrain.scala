package it.unibo.model
import scala.util.Random
import Tiling.*

object PerlinNoise:
  def getNoise(x: Int, y: Int): Double =
    Random.nextDouble()


class Terrain extends Scenario:

  private def getTileFromNoise(noise: Double)(position: Position): Tile =
    if noise < 0.2 then
      Water(position)
    else if noise < 0.4 then
      Grass(position)
    else if noise < 0.7 then
      Rock(position)
    else
      Lava(position)


  def initialPosition: Position = Position(0, 0)

  override def generateScenario(): Unit =
    tiles =
      (for
        x <- 0 until Scenario.nCols
        y <- 0 until Scenario.nRows
      yield
        getTileFromNoise(PerlinNoise.getNoise(x, y))(Position(x, y))).toList

