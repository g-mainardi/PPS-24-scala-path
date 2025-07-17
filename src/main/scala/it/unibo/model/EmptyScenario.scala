package it.unibo.model

import it.unibo.model.Tiling.{Floor, Position, Tile}

class EmptyScenario(nRows: Int, nCols: Int) extends Scenario(nRows, nCols):
  
  override def generate(): Unit =
    generate((x,y) => Floor(Position(x,y)))
  
  def generate(generator: (x: Int, y:Int) => Tile): Unit =
    _tiles = (for
      x <- 0 until nCols
      y <- 0 until nRows
      pos: Position = Position(x, y)
    yield
      generator(x,y)).toList