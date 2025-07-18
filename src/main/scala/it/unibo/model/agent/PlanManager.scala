package it.unibo.model.agent

import it.unibo.model.fundamentals.Direction

trait PlanManager:
  private var _currentPlan: Seq[Direction] = Seq.empty
  private var _planIndex: Int = 0
  
  def remainingSteps: Int = _currentPlan.length - _planIndex

  def currentPlan: Seq[Direction] = _currentPlan
  def currentPlan_=(newPlan: Seq[Direction]): Unit = _currentPlan = newPlan
  
  def planOver: Boolean = _planIndex >= _currentPlan.length

  def resetPlan(): Unit = _planIndex = 0

  protected def nextDirection: Direction =
    try _currentPlan(_planIndex)
    finally _planIndex += 1
  
  protected def currentDirection: Direction = _currentPlan(_planIndex)
  