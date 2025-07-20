package it.unibo.model.agent

import it.unibo.model.fundamentals.Direction

/** A trait that manages execution of a plan consisting of sequential directions.
 *
 * This trait provides functionality to:
 * <li>Store and access the current plan of directions
 * <li>Track progress through the plan
 * <li>Query plan state and remaining steps
 */
trait PlanManager:
  private var _currentPlan: Seq[Direction] = Seq.empty
  private var _planIndex: Int = 0
  
  def remainingSteps: Int = _currentPlan.length - _planIndex

  def currentPlan: Seq[Direction] = _currentPlan
  def currentPlan_=(newPlan: Seq[Direction]): Unit = _currentPlan = newPlan
  
  /** Checks if the current plan has been completed.
   *
   * @return true if all directions in the plan have been processed
   */
  def planOver: Boolean = _planIndex >= _currentPlan.length

  /** Resets the plan index to start from the beginning. */
  def resetPlan(): Unit = _planIndex = 0

  /** Returns the next direction in the plan and advances the plan index.
   *
   * @return the next direction to execute
   */
  protected def nextDirection: Direction =
    try _currentPlan(_planIndex)
    finally _planIndex += 1
  
  /** Returns the current direction without advancing the plan index.
   *
   * @return the current direction in the plan
   */
  protected def currentDirection: Direction = _currentPlan(_planIndex)
  