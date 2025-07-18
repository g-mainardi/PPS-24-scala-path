package it.unibo.model

case object FailedPlannerBuildException extends Exception
case object FailedPlannerExecutionException extends Exception
case object AgentNotBuiltException extends Exception
case class PlanNotFoundException(msg: String) extends Exception(msg)
