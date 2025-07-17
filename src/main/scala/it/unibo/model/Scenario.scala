package it.unibo.model

import Tiling.*
import it.unibo.utils.PrettyPrint

object Scenario:
  val nRows = 7
  val nCols = 7

case class ScenarioDimensions(nRows: Int, nCols: Int)

trait Scenario(val nRows: Int, val nCols: Int) extends PrettyPrint:
  import it.unibo.utils.RandomGenerator.*
  protected var _tiles: List[Tile] = List()
  given ScenarioDimensions = ScenarioDimensions(nRows, nCols)

  def generate(): Unit

  def tiles: List[Tile] = _tiles

  def checkSpecial(position: Position): Option[Tile] = tiles.find(tile => tile.x == position.x && tile.y == position.y)

  def freePositions: List[Position] = tiles collect {case pass: Passage => Position(pass.x, pass.y)}

  def randomFreePosition: Option[Position] = freePositions.getRandomElement

  def randomFreePositions(n: Int): Set[Position] = freePositions getRandomElements n
