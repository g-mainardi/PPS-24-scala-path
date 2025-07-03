package it.unibo.planning

import it.unibo.planning.Plan.*
import it.unibo.model.Tiling.Tile
import it.unibo.model.Cardinals
import it.unibo.planning.BasePrologPlannerBuilder

trait Planner:
  def plan: Plan
  
class DummyPlanner extends Planner:
  override def plan: Plan = SucceededPlan(List.fill(5)(Cardinals.Down), 5)

class PlannerWithoutTiles(initPos:(Int, Int), goal:(Int, Int), maxMoves: Option[Int] = None) extends Planner:
  override def plan: Plan =
    BasePrologPlannerBuilder()
      .withTheoryFrom("src/main/prolog/plannerWithoutTilesOptimized.pl")
      .withInit(initPos)
      .withGoal(goal)
      .withMaxMoves(maxMoves)
      .run

class PlannerWithTiles(initPos:(Int, Int), goal:(Int, Int), tiles: List[Tile], maxMoves: Option[Int] = None) extends Planner:
  override def plan: Plan =
    println(s"Tiles: $tiles")
    BasePrologPlannerBuilder()
      .withTiles(tiles)
      .withTheoryFrom("src/main/prolog/plannerWithTiles.pl")
      .withInit(initPos)
      .withGoal(goal)
      .withMaxMoves(maxMoves)
      .run