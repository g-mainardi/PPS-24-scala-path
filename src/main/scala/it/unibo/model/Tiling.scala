package it.unibo.model

object Tiling:
  case class Position(x: Int, y: Int, visited: Boolean = false):
    def +(vector: Position): Position = Position(x + vector.x, y + vector.y)
  
  trait Tile:
    protected def pos: Position
    def x: Int = pos.x
    def y: Int = pos.y
  
  sealed trait Passage extends Tile:
    def visited: Boolean = pos.visited
  
  case class Floor(protected val pos: Position) extends Passage
  case class Grass(protected val pos: Position) extends Passage
  case class Teleport(protected val pos: Position) extends Passage
  case class Arrow(protected val pos: Position, direction: Direction) extends Passage
  
  sealed trait Obstacle extends Tile
  case class Wall(protected val pos: Position) extends Obstacle
  case class Trap(protected val pos: Position) extends Obstacle
  case class Water(protected val pos: Position) extends Obstacle
  case class Lava(protected val pos: Position) extends Obstacle
  case class Rock(protected val pos: Position) extends Obstacle

@main def testArchitecture(): Unit = {
  import Tiling.*
  val f = Floor(Position(x = 1, y = 2))
  // f.pos not accessibile
  println(s"The floor is at (${f.x}, ${f.y}), has it been visited? ${if f.visited then "yes" else "no"}")
}
