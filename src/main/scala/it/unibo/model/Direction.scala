package it.unibo.model

import it.unibo.model.Tiling.Position

trait Direction:
  def vector: Position

object Direction:
  enum Cardinals extends Direction:
    case Up, Down, Left, Right
    override def vector: Position = this match
      case Up    => Position( 0, -1)
      case Down  => Position( 0,  1)
      case Left  => Position(-1,  0)
      case Right => Position( 1,  0)

  import Cardinals.*
  enum Diagonals extends Direction:
    case RightUp, RightDown, LeftUp, LeftDown
    override def vector: Position = this match
      case RightUp   => Right.vector + Up.vector
      case RightDown => Right.vector + Down.vector
      case LeftUp    => Left.vector + Up.vector
      case LeftDown  => Left.vector + Down.vector

  val allDirections: List[Direction] = Cardinals.values.toList ++ Diagonals.values.toList
