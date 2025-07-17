package it.unibo.model.fundamentals


case class Position(x: Int, y: Int):
  def +(vector: Position): Position = Position(x + vector.x, y + vector.y)

  def -(other: Position): Position = Position(x - other.x, y - other.y)

object Position:
  def apply(tuple: (Int, Int)): Position = Position(tuple._1, tuple._2)

  def unapply(tile: Tile): Option[(Int, Int)] = Some(tile.x, tile.y)
