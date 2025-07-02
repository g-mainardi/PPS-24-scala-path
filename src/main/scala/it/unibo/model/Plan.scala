package it.unibo.model

trait Plan

case class SucceededPlan(directions: List[Direction], numMoves: Int) extends Plan
case class FailedPlan() extends Plan
