package it.unibo.model

import it.unibo.model.Tiling.Position

//trait Move:
//  def direction: Direction

//case class BasicMove() extends Move
//case class Diagonal(override val direction: Diagonals) extends Move

trait Direction:
  def vector: Position

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
