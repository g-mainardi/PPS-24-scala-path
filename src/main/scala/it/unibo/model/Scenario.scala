package it.unibo.model

import Tiling.*
import it.unibo.planning.Plan
import it.unibo.planning.Plan.FailedPlan
import it.unibo.utils.PrettyPrint

object Scenario:
  val nRows = 7
  val nCols = 7

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

class Agent(val initialPosition: Position, var plan: Plan, var tiles: List[Tile]):
  var pos: Position = initialPosition
  def x: Int = pos.x
  def y: Int = pos.y

  def isFailedPlan: Boolean = plan match
      case FailedPlan(_) => true
      case _ => false

  def computeCommand(): Unit = plan.next match
      case (Some(direction), nextPlan) =>
        pos = pos + direction.vector
        plan = nextPlan
      case (None, _) => ()
  checkSpecial()

  def getTileAt(position: Position): Option[Tile] = tiles.find(tile => tile.x == position.x && tile.y == position.y)
  
  private def checkSpecial(): Unit =
    getTileAt(pos) match
      case Some(special: Special) => pos = special.newPos
      case _ =>

trait Scenario(nrows: Int, ncols: Int) extends PrettyPrint:
  // private var _agent: Agent = Agent(initialPosition, getTileAt)
  protected var _tiles: List[Tile] = List()

  // def agent: Agent = _agent
  def tiles: List[Tile] = _tiles
  // def initialPosition: Position = Position(0, 0)
  // def goalPosition: Position = Position(Scenario.nRows - 1, Scenario.nCols - 1)
  def generate(): Unit
  // def resetAgent(): Unit = _agent = Agent(initialPosition, getTileAt)
  // def getTileAt(position: Position): Option[Tile] = tiles.find(tile => tile.x == position.x && tile.y == position.y)

class DummyScenario(nrows: Int, ncols: Int) extends Scenario(nrows, ncols):
  override def generate(): Unit =
    _tiles = for
      (tileType, ind) <- List(Floor(_), Grass(_), Teleport(_), Trap(_), Water(_), Lava(_), Rock(_)).zipWithIndex
      pos: Position = Position(ind, ind)
    yield
      tileType(pos)