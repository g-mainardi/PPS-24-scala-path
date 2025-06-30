package it.unibo.model

import Tiling.*

object Scenario:
  val nRows = 60
  val nCols = 30

  import scala.util.Random
  private val rand = Random(seed = 42)

  def randomPosition: Position =
    val positions: Seq[Position] = for
      x <- 0 to nRows
      y <- 0 to nCols
    yield
      Position(x, y)
    positions(rand nextInt positions.size)
  
  def randomPositions(size: Int): Set[Position] = Set.fill(size)(randomPosition)

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
  def generate(): Unit
  def resetAgent(): Unit = agent = Agent(initialPosition)
  override def toString: String = s"${getClass.getSimpleName}"

class DummyScenario extends Scenario:
  def initialPosition: Position = Position(0, 0)
  override def generate(): Unit =
    tiles = for
      (tileType, ind) <- List(Floor(_), Grass(_), Teleport(_), Trap(_), Water(_), Lava(_), Rock(_)).zipWithIndex
      pos: Position = Position(ind, ind)
    yield
      tileType(pos)


@main def TestScenario(): Unit =
  println(DummyScenario().initialPosition)