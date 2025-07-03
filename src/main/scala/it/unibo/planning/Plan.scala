package it.unibo.planning

import it.unibo.model.Direction

enum Plan:
  case SucceededPlanWithMaxMoves(directions: List[Direction], numMoves: Int)
  case SucceededPlan(directions: List[Direction])
  case FailedPlan(errorMessage: String)
