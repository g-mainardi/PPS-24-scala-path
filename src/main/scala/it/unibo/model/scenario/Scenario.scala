package it.unibo.model.scenario

import it.unibo.model.fundamentals.{Position, Tile}
import it.unibo.model.fundamentals.Tiling.Passage
import it.unibo.utils.PrettyPrint

object Scenario:
  val nRows = 7
  val nCols = 7
  case class Dimensions(nRows: Int, nCols: Int)

trait Scenario(val nRows: Int, val nCols: Int) extends PrettyPrint:
  import Scenario.Dimensions
  import it.unibo.utils.RandomGenerator.*
  protected var _tiles: List[Tile] = List()
  given Dimensions = Dimensions(nRows, nCols)

  def generate(): Unit

  def tiles: List[Tile] = _tiles

  def checkSpecial(position: Position): Option[Tile] = tiles.find(tile => tile.x == position.x && tile.y == position.y)

  def freePositions: List[Position] = tiles collect {case pass: Passage => Position(pass.x, pass.y)}

  def randomFreePosition: Option[Position] = freePositions.getRandomElement

  def randomFreePositions(n: Int): Set[Position] = freePositions getRandomElements n
