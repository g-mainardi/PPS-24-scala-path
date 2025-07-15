package it.unibo.model

import it.unibo.model.Scenario.*
import it.unibo.model.Tiling.*

object TrampolinePos:
  private var trampolines: Set[Position] = Set.empty
  def set(p: Set[Position]): Unit = trampolines = p
  def unapply(t: Tile): Option[Position] = t match
    case TilePos(pos) => trampolines find (_ == pos)

class Traps(nrows: Int, ncols: Int) extends Scenario(nrows: Int, ncols: Int):
  val nTrampolines = 4

  override def generate(): Unit =
    _tiles = (for
      x <- 0 to nRows
      y <- 0 to nCols
      pos: Position = Position(x, y)
    yield
      Floor(pos)).toList

    TrampolinePos set randomPositions(nTrampolines)
    _tiles = _tiles map:
      case TrampolinePos(p) => Teleport(p)
      case t => t
