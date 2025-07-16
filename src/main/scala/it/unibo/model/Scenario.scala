package it.unibo.model

import Tiling.*
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

trait Scenario(val nRows: Int, val nCols: Int) extends PrettyPrint:
  protected var _tiles: List[Tile] = List()

  def tiles: List[Tile] = _tiles
  def generate(): Unit
  def checkSpecial(position: Position): Option[Tile] = tiles.find(tile => tile.x == position.x && tile.y == position.y)