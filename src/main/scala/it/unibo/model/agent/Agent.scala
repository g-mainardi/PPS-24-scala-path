package it.unibo.model.agent

import it.unibo.model.fundamentals.Tiling.Special
import it.unibo.model.exceptions.PlanNotFoundException
import it.unibo.model.fundamentals.{Direction, Position, Tile}
import it.unibo.model.planning.Plan
import Plan.{FailedPlan, SucceededPlan, SucceededPlanWithMoves}

/** An Agent represents an entity that can navigate through a grid-based environment
 * using planning algorithms.
 *
 * @param initialPosition The starting position of the agent
 * @param plan            A function that generates a navigation plan
 * @param getTileAt       A function to get the tile at a given position
 */
class Agent(val initialPosition: Position, plan: () => Plan, getTileAt: Position => Option[Tile])
  extends PathManager
  with PlanManager:
  private var _pos: Position = initialPosition
  
  def pos: Position = _pos
  def x: Int = _pos.x
  def y: Int = _pos.y
  
  /** Updates the agent's position based on the given direction
   *
   * @param direction The direction to move in
   */
  def computeCommand(direction: Direction): Unit =
    _pos = _pos + direction.vector
    checkSpecial()

  def resetPosition(): Unit = _pos = initialPosition

  /** Executes one step of the agent's plan, updating its position and path */
  def step(): Unit = 
    addToPath(_pos, currentDirection)
    this computeCommand nextDirection
  
  /** Attempts to find a valid plan for the agent
   *
   * @return Some(number of moves) if the plan has a move limit, None otherwise
   * @throws PlanNotFoundException if no valid plan can be found
   */
  def searchPlan: Option[Int] = plan() match
    case SucceededPlanWithMoves(directions, nMoves) =>
      currentPlan = directions
      Some(nMoves)
    case SucceededPlan(directions) =>
      currentPlan = directions
      None
    case FailedPlan(error) =>
      this.currentPlan_=(Seq.empty)
      throw PlanNotFoundException(error)

  private def checkSpecial(): Unit = getTileAt(_pos) match
    case Some(special: Special) => _pos = special.newPos
    case _ =>