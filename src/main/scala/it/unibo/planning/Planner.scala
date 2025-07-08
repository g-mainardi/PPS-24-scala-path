package it.unibo.planning

import it.unibo.model.Direction
import it.unibo.planning.Plan.*
import it.unibo.model.Tiling.Tile
import it.unibo.model.Direction.{Cardinals, allDirections}
import it.unibo.planning.prologplanner.{BasePrologPlannerBuilder, DFSBuilder}

// Cardinals.values.toList ++ Diagonals.values.toList

trait Planner:
  def plan: Plan

class DummyPlanner extends Planner:
  override def plan: Plan = SucceededPlanWithMoves(List.fill(5)(Cardinals.Down), 5)

class PlannerWithTiles(initPos: (Int, Int), goal: (Int, Int), tiles: List[Tile], maxMoves: Option[Int] = None, directions: List[Direction] = allDirections) extends Planner:
  override def plan: Plan =
    println(s"Tiles: $tiles")
    DFSBuilder()
      .withTheoryFrom("src/main/prolog/plannerWithTiles.pl")
      .withTiles(tiles)
      .withInit(initPos)
      .withGoal(goal)
      .withMaxMoves(maxMoves)
      .withDirections(directions)
      .run

class PlannerWithSpecials(initPos: (Int, Int), goal: (Int, Int), tiles: List[Tile], maxMoves: Option[Int] = None, directions: List[Direction] = allDirections) extends Planner:
  override def plan: Plan =
    println(s"Tiles: $tiles")
    DFSBuilder()
      .withTheoryFrom("src/main/prolog/plannerWithSpecials.pl")
      .withTiles(tiles)
      .withInit(initPos)
      .withGoal(goal)
      .withMaxMoves(maxMoves)
      .withDirections(allDirections)
      .run