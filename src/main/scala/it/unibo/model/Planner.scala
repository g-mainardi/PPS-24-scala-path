package it.unibo.model

trait Planner:
  def plan(): Seq[Direction]