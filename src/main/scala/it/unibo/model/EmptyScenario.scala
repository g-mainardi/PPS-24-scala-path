package it.unibo.model

import it.unibo.model.Tiling.{Floor, Position}

class EmptyScenario(nRows: Int, nCols: Int) extends Scenario(nRows, nCols):
  override def generate(): Unit =
    _tiles = (for
      x <- 0 until nRows
      y <- 0 until nCols
      pos: Position = Position(x, y)
    yield
      Floor(pos)).toList