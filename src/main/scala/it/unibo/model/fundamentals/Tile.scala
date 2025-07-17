package it.unibo.model.fundamentals

trait Tile:
  protected def pos: Position
  def x: Int = pos.x
  def y: Int = pos.y

object Tile:
  def unapply(t: Tile): Option[Position] = Some(Position(t.x, t.y))