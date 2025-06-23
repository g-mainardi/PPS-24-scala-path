import it.unibo.model.{Direction, Cardinals}

trait Tile:
  def x: Int
  def y: Int
  def visited: Boolean = false

sealed trait Passage extends Tile
case class Floor(override val x:Int, override val y:Int, override val visited: Boolean = false) extends Passage
case class Grass(override val x:Int, override val y:Int, override val visited: Boolean = false) extends Passage
case class Teleport(override val x:Int, override val y:Int, override val visited: Boolean = false) extends Passage
case class Arrow(override val x:Int, override val y:Int, override val visited: Boolean = false, direction: Direction) extends Passage

sealed trait Obstacle extends Tile
case class Wall(override val x:Int, override val y:Int, override val visited: Boolean = false, directions: Set[Cardinals]) extends Obstacle
case class Trap(override val x:Int, override val y:Int, override val visited: Boolean = false) extends Obstacle
case class Water(override val x:Int, override val y:Int, override val visited: Boolean = false) extends Obstacle
case class Lava(override val x:Int, override val y:Int, override val visited: Boolean = false) extends Obstacle
case class Rock(override val x:Int, override val y:Int, override val visited: Boolean = false) extends Obstacle

