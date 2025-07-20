package it.unibo.model.agent

import it.unibo.model.fundamentals.{Direction, Position}


/** A trait that manages the path history of an agent's movement.
 *
 * [[PathManager]] keeps track of the positions and directions an agent has moved through,
 * storing them as a sequence of position-direction pairs. This history can be used
 * for visualization or analysis of the agent's movement pattern.
 */
trait PathManager:
  private var _path: Seq[(Position, Direction)] = Seq.empty

  def path: Seq[(Position, Direction)] = _path
  protected def addToPath(p: Position, d: Direction): Unit = _path = _path :+ (p, d)
  def resetPath(): Unit = _path = Seq.empty

