package it.unibo.model

import it.unibo.model.Direction.Cardinals.{Down, Left, Right, Up}
import it.unibo.model.Tiling.Position

import scala.util.Random

trait Direction:
  def vector: Position

trait Special extends Direction

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

  enum Specials extends Special:
    case Teleport
    override def vector: Position = this match
      case Teleport => Position (Random nextInt (Scenario.nRows - 1), Random nextInt (Scenario.nCols - 1))

  enum Arrows extends Special:
    case ArrowUp, ArrowDown, ArrowRight, ArrowLeft
    override def vector: Position = this match
      case ArrowUp    => Position( 0, -2)
      case ArrowDown  => Position( 0,  2)
      case ArrowLeft  => Position(-2,  0)
      case ArrowRight => Position( 2,  0)

  val allNormals: List[Direction] = Cardinals.values.toList ++ Diagonals.values.toList
  val allSpecials: List[Special] = Specials.values.toList ++ Arrows.values.toList
  val allDirections: List[Direction] = allNormals ++ allSpecials
