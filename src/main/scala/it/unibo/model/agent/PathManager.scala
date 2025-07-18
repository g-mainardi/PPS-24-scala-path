package it.unibo.model.agent

import it.unibo.model.fundamentals.{Direction, Position}


trait PathManager:
  private var _path: Seq[(Position, Direction)] = Seq.empty

  def path: Seq[(Position, Direction)] = _path
  protected def addToPath(p: Position, d: Direction): Unit = _path = _path :+ (p, d)
  def resetPath(): Unit = _path = Seq.empty

