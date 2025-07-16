package it.unibo.model

import it.unibo.model.Tiling.{Position, Special, Tile}
import it.unibo.planning.Plan
import it.unibo.planning.Plan.{FailedPlan, SucceededPlan, SucceededPlanWithMoves}

class Agent(val initialPosition: Position, plan: () => Plan, getTileAt: Position => Option[Tile])
  extends PathManager
  with PlanManager:
  private var _pos: Position = initialPosition
  
  def pos: Position = _pos
  def x: Int = _pos.x
  def y: Int = _pos.y
  
  def computeCommand(direction: Direction): Unit =
    _pos = _pos + direction.vector
    checkSpecial()

  def resetPosition(): Unit = _pos = initialPosition

  def step(): Unit = 
    addToPath(_pos, currentDirection)
    this computeCommand nextDirection
  
  def searchPlan: Option[Int] = plan() match
    case SucceededPlanWithMoves(directions, nMoves) =>
      currentPlan = directions
      Some(nMoves)
    case SucceededPlan(directions) =>
      currentPlan = directions
      None
    case FailedPlan(error) =>
      this.currentPlan_=(List.empty)
      throw PlanNotFoundException(error)

  private def checkSpecial(): Unit = getTileAt(_pos) match
    case Some(special: Special) => _pos = special.newPos
    case _ =>