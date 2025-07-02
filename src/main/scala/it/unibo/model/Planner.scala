package it.unibo.model

import it.unibo.model.Tiling.Tile
import it.unibo.prologintegration.PlannerBuilder

trait Planner:
  def plan: Plan
  
class DummyPlanner extends Planner:
  override def plan: Option[List[Direction]] =
    Some(List.fill(5)(Cardinals.Down))

class PlannerWithoutTiles(initPos:(Int, Int), goal:(Int, Int), maxMoves: Option[Int]) extends Planner:
  override def plan: Plan =
    PlannerBuilder()
      .withTheoryFrom("src/main/prolog/plannerWithoutTiles.pl")
      .withInit(initPos)
      .withGoal(goal)
      .withMaxMoves(maxMoves)
      .run

class PlannerWithTiles(initPos:(Int, Int), goal:(Int, Int), tiles: List[Tile], maxMoves: Option[Int]) extends Planner:
  override def plan: Plan =
    println(s"Tiles: $tiles")
    PlannerBuilder()
      .withTiles(tiles)
      .withTheoryFrom("src/main/prolog/plannerWithTiles.pl")
      .withInit(initPos)
      .withGoal(goal)
      .withMaxMoves(maxMoves)
      .run