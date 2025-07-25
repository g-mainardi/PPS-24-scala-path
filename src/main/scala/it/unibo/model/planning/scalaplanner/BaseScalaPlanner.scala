package it.unibo.model.planning.scalaplanner

import it.unibo.model.fundamentals.Direction
import it.unibo.model.planning.Plan
import Plan.{FailedPlan, SucceededPlan, SucceededPlanWithMoves}

trait BaseScalaPlanner:
  def checkSolution(directions: Option[Seq[Direction]]): Plan =
    if directions.nonEmpty then
      SucceededPlanWithMoves(directions.get, directions.get.length)
    else
      FailedPlan("No valid plan found")