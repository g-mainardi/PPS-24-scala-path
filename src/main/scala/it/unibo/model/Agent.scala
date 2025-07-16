package it.unibo.model

import it.unibo.model.Tiling.{Position, Special, Tile}
import it.unibo.planning.Plan
import it.unibo.planning.Plan.{FailedPlan, SucceededPlan, SucceededPlanWithMoves}

class Agent(val initialPosition: Position, plan: () => Plan, getTileAt: Position => Option[Tile]):
  private var _currentPlan: List[Direction] = List.empty
  private var _planIndex: Int = 0
  private var _pos: Position = initialPosition
  private var _path: List[(Position, Direction)] = List()
  
  def pos: Position = _pos
  def x: Int = _pos.x
  def y: Int = _pos.y
  
  def path: List[(Position, Direction)] = _path

  def resetPath(): Unit = _path = List()

  def computeCommand(direction: Direction): Unit =
    _pos = _pos + direction.vector
    checkSpecial()

  def resetPosition(): Unit = _pos = initialPosition

  def planOver: Boolean = _planIndex >= _currentPlan.length

  def remainingSteps: Int = _currentPlan.length - _planIndex

  def resetPlan(): Unit = _planIndex = 0
  
  def step(): Unit = 
    addToPath(_pos, currentDirection)
    this computeCommand nextDirection
  
  def searchPlan: Option[Int] = plan() match
    case SucceededPlanWithMoves(directions, nMoves) =>
      _currentPlan = directions
      Some(nMoves)
    case SucceededPlan(directions) =>
      _currentPlan = directions
      None
    case FailedPlan(error) =>
      _currentPlan = List.empty
      throw PlanNotFoundException(error)

  private def checkSpecial(): Unit = getTileAt(_pos) match
    case Some(special: Special) => _pos = special.newPos
    case _ =>

  protected def addToPath(p: Position, d: Direction): Unit = _path = _path :+ (p, d)
  
  private def currentDirection: Direction = _currentPlan(_planIndex)
  
  private def nextDirection: Direction =
    try _currentPlan(_planIndex)
    finally _planIndex += 1