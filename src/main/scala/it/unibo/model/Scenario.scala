package it.unibo.model

import Tiling.*

object Scenario:
  val nRows = 10
  val nCols = 10

trait Scenario:
  var agent: Position = Position(0, 0)
  var tiles: List[Tile] = List()
  def generateScenario(): Unit

class DummyScenario extends Scenario:
  override def generateScenario(): Unit =
    tiles = for
      (tileType, ind) <- List(Floor(_), Grass(_), Teleport(_), Trap(_), Water(_), Lava(_), Rock(_)).zipWithIndex
      pos: Position = Position(ind, ind)
    yield
      tileType(pos)