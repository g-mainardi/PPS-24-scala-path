package it.unibo.model.planning

import it.unibo.model.fundamentals.Direction

enum Plan:
  case SucceededPlanWithMoves(directions: Seq[Direction], numMoves: Int)
  case SucceededPlan(directions: Seq[Direction])
  case FailedPlan(errorMessage: String)
