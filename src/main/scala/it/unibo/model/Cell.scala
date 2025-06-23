import it.unibo.model.{Direction, Cardinals}

trait Tile:
  def x: Int
  def y: Int
  def visited: Boolean = false

sealed trait Passage extends Tile
case class Floor(visited: Boolean = false) extends Passage
case class Grass(visited: Boolean = false) extends Passage
case class Teleport(visited: Boolean = false) extends Passage
case class Arrow(direction: Direction, visited: Boolean = false) extends Passage

sealed trait Obstacle extends Tile
case class Wall(directions: Set[Cardinals]) extends Obstacle
case object Trap extends Obstacle
case object Water extends Obstacle
case object Lava extends Obstacle
case object Rock extends Obstacle

