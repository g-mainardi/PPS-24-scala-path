package it.unibo.planning

import it.unibo.model.Direction

enum Plan:
  case SucceededPlanWithMoves(directions: List[Direction], numMoves: Int)
  case SucceededPlan(directions: List[Direction])
  case FailedPlan(errorMessage: String)
  case EmptyPlan
  
  def next: (Option[Direction], Plan) = this match
    case SucceededPlan(head :: tail) => (Some(head), SucceededPlan(tail))
    case SucceededPlanWithMoves(head :: tail, n)  => (Some(head), SucceededPlanWithMoves(tail, n - 1))
    case SucceededPlan(head :: _) => (Some(head), EmptyPlan)
    case SucceededPlanWithMoves(head :: _, n)  => (Some(head), EmptyPlan)
    case _ => (None, EmptyPlan)
