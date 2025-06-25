package it.unibo.model

import Tiling.*

object Scenario:
  val nRows = 10
  val nCols = 10

trait Scenario:
  def initialPosition: Position
  var agent: Position = initialPosition
  var tiles: List[Tile] = List()
  def generateScenario(): Unit
  def resetAgent(): Unit = agent = initialPosition

class DummyScenario extends Scenario:
  def initialPosition: Position = Position(0, 0)
  override def generateScenario(): Unit =
    tiles = for
      (tileType, ind) <- List(Floor(_), Grass(_), Teleport(_), Trap(_), Water(_), Lava(_), Rock(_)).zipWithIndex
      pos: Position = Position(ind, ind)
    yield
      tileType(pos)


@main def TestScenario(): Unit =
  println(DummyScenario().initialPosition)