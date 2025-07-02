package it.unibo.model

import it.unibo.model.Tiling.Tile
import it.unibo.prologintegration.PlannerBuilder

trait Planner:
  def plan: Option[List[Direction]]
  
class DummyPlanner extends Planner:
  override def plan: Option[List[Direction]] =
    Some(List.fill(5)(Cardinals.Down))

class PlannerWithoutTiles(initPos:(Int, Int), goal:(Int, Int), maxMoves:Int) extends Planner:
  override def plan: Option[List[Direction]] =
    PlannerBuilder()
      .withTheoryFrom("src/main/prolog/plannerWithoutTiles.pl")
      .withInit(initPos)
      .withGoal(goal)
      .run

class PlannerWithMaxMoves(initPos:(Int, Int), goal:(Int, Int), maxMoves:Int, tiles: List[Tile]) extends Planner:
  override def plan: Option[List[Direction]] =
    println(s"Tiles: $tiles")
    PlannerBuilder()
      .withTiles(tiles)
      .withTheoryFrom("src/main/prolog/plannerWithMaxMoves.pl")
      .withInit(initPos)
      .withMaxMoves(maxMoves)
      .withGoal(goal)
      .run

class PlannerWithMoves(initPos:(Int, Int), goal:(Int, Int), tiles: List[Tile]) extends Planner:
  override def plan: Option[List[Direction]] =
    println(s"Tiles: $tiles")
    PlannerBuilder()
      .withTiles(tiles)
      .withTheoryFrom("src/main/prolog/plannerWithMoves.pl")
      .withInit(initPos)
      .withGoal(goal)
      .run