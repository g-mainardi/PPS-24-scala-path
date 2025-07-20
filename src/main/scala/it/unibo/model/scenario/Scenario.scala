package it.unibo.model.scenario

import it.unibo.model.fundamentals.{Position, Tile}
import it.unibo.model.fundamentals.Tiling.Passage
import it.unibo.utils.PrettyPrint

object Scenario:
  val nRows = 10
  val nCols = 10
  case class Dimensions(nRows: Int, nCols: Int)

trait Scenario(val nRows: Int, val nCols: Int) extends PrettyPrint:
  import Scenario.Dimensions
  import it.unibo.utils.RandomGenerator.*
  protected var _tiles: Seq[Tile] = Seq.empty
  given Dimensions = Dimensions(nRows, nCols)

  def generate(): Unit

  def tiles: Seq[Tile] = _tiles

  def checkSpecial(position: Position): Option[Tile] = _tiles.find(tile => tile.x == position.x && tile.y == position.y)

  def freePositions: Seq[Position] = _tiles collect {case pass: Passage => Position(pass.x, pass.y)}

  def randomFreePosition: Option[Position] = freePositions.getRandomElement

  def randomFreePositions(n: Int): Seq[Position] = freePositions getRandomElements n

  def resize(nRows: Int, nCols: Int): Scenario =
    this.getClass getDeclaredConstructor(classOf[Int], classOf[Int]) newInstance(nRows, nCols)
