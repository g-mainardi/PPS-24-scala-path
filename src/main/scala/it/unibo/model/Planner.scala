package it.unibo.model

trait Planner:
  def plan(): LazyList[Direction]
  
class DummyPlanner extends Planner:
  override def plan(): LazyList[Direction] =
    LazyList.fill(5)(Cardinals.Down)