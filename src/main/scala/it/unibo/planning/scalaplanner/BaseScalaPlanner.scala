package it.unibo.planning.scalaplanner

import it.unibo.model.Direction
import it.unibo.planning.Plan
import it.unibo.planning.Plan.{FailedPlan, SucceededPlan, SucceededPlanWithMoves}

trait BaseScalaPlanner:
  def checkSolution(directions: Option[List[Direction]]): Plan =
    if directions.nonEmpty then
      // SucceededPlan(directions.get)
      SucceededPlanWithMoves(directions.get, directions.get.length)
    else
      FailedPlan("No valid plan found")