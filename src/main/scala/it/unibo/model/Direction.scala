package it.unibo.model

trait Move:
  def direction: Direction

case class BasicMove() extends Move
case class Diagonal(override val direction: Diagonals) extends Move

trait Direction

enum Cardinals extends Direction:
  case Up
  case Down
  case Left
  case Right
  
enum Diagonals extends Direction:
  case rightUp
  case rightDown
  case leftUp
  case leftDown

