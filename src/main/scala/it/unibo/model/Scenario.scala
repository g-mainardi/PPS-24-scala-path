package it.unibo.model

import Tiling.*

object Scenario:
  val nRows = 10
  val nCols = 10

trait Scenario: 
  def generateScenario(): List[Tile]

class DummyScenario extends Scenario:
  override def generateScenario(): List[Tile] =
    for
      (tileType, ind) <- List(Floor(_), Grass(_), Teleport(_), Trap(_), Water(_), Lava(_), Rock(_)).zipWithIndex
      pos: Position = Position(ind, ind)
    yield
      tileType(pos)