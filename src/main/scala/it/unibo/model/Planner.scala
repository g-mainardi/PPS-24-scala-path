package it.unibo.model

import it.unibo.prologintegration.PlannerBuilder

trait Planner:
  def plan: Option[List[Direction]]
  
class DummyPlanner extends Planner:
  override def plan: Option[List[Direction]] =
    Some(List.fill(5)(Cardinals.Down))

class BasePlanner(initPos:(Int, Int), goal:(Int, Int), maxMoves:Int) extends Planner:
  override def plan: Option[List[Direction]] =
    PlannerBuilder()
      .withTheoryFrom("src/main/prolog/basePlanner.pl")
      .withInit(initPos)
      .withGoal(goal)
      .withMaxMoves(maxMoves)
      .run()

