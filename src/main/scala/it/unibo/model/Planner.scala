package it.unibo.model

import it.unibo.prologintegration.PlannerBuilder

trait Planner:
  def plan: Option[LazyList[Direction]]
  
class DummyPlanner extends Planner:
  override def plan: Option[LazyList[Direction]] =
    Some(LazyList.fill(5)(Cardinals.Down))

class BasePlanner extends Planner:
  override def plan: Option[LazyList[Direction]] =
    PlannerBuilder()
      .withTheoryFrom("src/main/prolog/basePlanner.pl")
      .withInit(0, 0)
      .withGoal(2, 2)
      .withMaxMoves(5)
      .run()

