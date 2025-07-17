package it.unibo.model.agent

import it.unibo.model.fundamentals.{Direction, Position}


trait PathManager:
  private var _path: List[(Position, Direction)] = List()

  def path: List[(Position, Direction)] = _path
  protected def addToPath(p: Position, d: Direction): Unit = _path = _path :+ (p, d)
  def resetPath(): Unit = _path = List()

