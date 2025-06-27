package it.unibo.model

import Tiling.*

object Scenario:
  val nRows = 100
  val nCols = 100

class Agent(val initialPosition: Position):
  var pos: Position = initialPosition
  def x: Int = pos.x
  def y: Int = pos.y
  def computeCommand(direction: Direction): Unit =
    pos = pos + direction.vector

trait Scenario:
  def initialPosition: Position
  var agent: Agent = Agent(initialPosition)
  var tiles: List[Tile] = List()
  def generateScenario(): Unit
  def resetAgent(): Unit = agent = Agent(initialPosition)

class DummyScenario extends Scenario:
  def initialPosition: Position = Position(0, 0)
  override def generateScenario(): Unit =
    tiles = for
      (tileType, ind) <- List(Floor(_), Grass(_), Teleport(_), Trap(_), Water(_), Lava(_), Rock(_)).zipWithIndex
      pos: Position = Position(ind, ind)
    yield
      tileType(pos)