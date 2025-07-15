package it.unibo.model

import Tiling.*
import it.unibo.planning.Plan
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

trait Scenario(nRows: Int, nCols: Int) extends PrettyPrint:
  protected var _tiles: List[Tile] = List()

  def tiles: List[Tile] = _tiles
  def generate(): Unit
  def checkSpecial(position: Position): Option[Tile] = tiles.find(tile => tile.x == position.x && tile.y == position.y)

class DummyScenario(nRows: Int, nCols: Int) extends Scenario(nRows, nCols):
  override def generate(): Unit =
    _tiles = for
      (tileType, ind) <- List(Floor(_), Grass(_), Teleport(_), Trap(_), Water(_), Lava(_), Rock(_)).zipWithIndex
      pos: Position = Position(ind, ind)
    yield
      tileType(pos)