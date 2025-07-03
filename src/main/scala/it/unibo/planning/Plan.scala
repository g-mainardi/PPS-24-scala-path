package it.unibo.planning

import it.unibo.model.Direction

enum Plan:
  case SucceededPlan(directions: List[Direction], numMoves: Int)
  case SucceededPlanWithoutMaxMoves(directions: List[Direction])
  case FailedPlan(errorMessage: String)
