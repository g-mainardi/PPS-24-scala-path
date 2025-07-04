package it.unibo.model

import Tiling.*

object Scenario:
  val nRows = 6
  val nCols = 6

  import scala.util.Random
  private val rand = Random(seed = 44)

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
  private var _agent: Agent = Agent(initialPosition)
  protected var _tiles: List[Tile] = List()

  def agent: Agent = _agent
  def tiles: List[Tile] = _tiles
  def initialPosition: Position = Position(0, 0)
  def goalPosition: Position = Position(Scenario.nRows - 1, Scenario.nCols - 1)
  def generate(): Unit
  def resetAgent(): Unit = _agent = Agent(initialPosition)
  override def toString: String = s"${getClass.getSimpleName}"

class DummyScenario extends Scenario:
  override def generate(): Unit =
    _tiles = for
      (tileType, ind) <- List(Floor(_), Grass(_), Teleport(_), Trap(_), Water(_), Lava(_), Rock(_)).zipWithIndex
      pos: Position = Position(ind, ind)
    yield
      tileType(pos)