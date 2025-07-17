package it.unibo.model.planning

import it.unibo.model.fundamentals.Direction

enum Plan:
  case SucceededPlanWithMoves(directions: List[Direction], numMoves: Int)
  case SucceededPlan(directions: List[Direction])
  case FailedPlan(errorMessage: String)
