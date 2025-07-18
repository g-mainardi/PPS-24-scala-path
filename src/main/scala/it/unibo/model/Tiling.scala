package it.unibo.model

object Tiling:
  case class Position(x: Int, y: Int):
    def +(vector: Position): Position = Position(x + vector.x, y + vector.y)
    def -(other: Position): Position = Position(x - other.x, y - other.y)
  
  object Position:
    def apply(tuple: (Int, Int)): Position = Position(tuple._1, tuple._2)
    def unapply(tile: Tile): Option[(Int, Int)] = Some(tile.x, tile.y)

  trait Tile:
    protected def pos: Position
    def x: Int = pos.x
    def y: Int = pos.y
  
  object Tile:
    def unapply(t: Tile): Option[Position] = Some(Position(t.x, t.y))

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
