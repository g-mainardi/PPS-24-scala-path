package it.unibo.controller

import it.unibo.model.Direction
import it.unibo.planning.Plan.{FailedPlan, SucceededPlan, SucceededPlanWithMoves}
import it.unibo.planning.Planner

trait PlanManager:
  private var _planner: Option[Planner] = None
  private var _currentPlan: List[Direction] = List()
  private var _planIndex: Int = 0

  protected def handleNoPlan(): Unit
  protected def handleValidPlan(): Unit
  protected def refreshPlanner(): Unit

  protected def planOver: Boolean = _planIndex >= _currentPlan.length

  protected def nextDirection: Direction =
    try _currentPlan(_planIndex)
    finally _planIndex += 1

  protected def resetPlan(): Unit = _planIndex = 0

  protected def refreshPlan(): Unit =
    refreshPlanner()
    resetPlan()
    println("Planner built! Now searching a plan...") //todo change with loading screen
    _currentPlan = _planner match
      case ValidPlanner(directions) if directions.nonEmpty =>
        handleValidPlan()
        directions
      case _ =>
        handleNoPlan()
        List.empty

  protected def setPlanner(planner: Planner): Unit = _planner = Some(planner)

  private object ValidPlanner:
    def unapply(plannerOpt: Option[Planner]): Option[List[Direction]] = plannerOpt map: p =>
      p.plan match
        case SucceededPlanWithMoves(directions, _) => directions //todo output n moves
        case SucceededPlan(directions)             => directions
        case FailedPlan(error)                     => println(error); List.empty
