package it.unibo.model

trait Planner:
  def plan(): LazyList[Direction]