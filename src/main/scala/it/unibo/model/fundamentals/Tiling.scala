package it.unibo.model.fundamentals

object Tiling:
  sealed trait Passage extends Tile
  case class Floor(protected val pos: Position) extends Passage
  case class Grass(protected val pos: Position) extends Passage

  trait Special extends Passage:
    def newPos: Position

  case class Teleport(protected val pos: Position, newPos: Position) extends Special
  case class Arrow(protected val pos: Position, direction: Direction) extends Special:
    val newPos: Position = pos + direction.vector
  
  sealed trait Obstacle extends Tile
  case class Wall(protected val pos: Position) extends Obstacle
  case class Trap(protected val pos: Position) extends Obstacle
  case class Water(protected val pos: Position) extends Obstacle
  case class Lava(protected val pos: Position) extends Obstacle
  case class Rock(protected val pos: Position) extends Obstacle
