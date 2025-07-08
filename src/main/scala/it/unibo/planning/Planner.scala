package it.unibo.planning

import alice.tuprolog.{SolveInfo, Term}
import it.unibo.model.Direction
import it.unibo.planning.{BFS, DFS}
import it.unibo.planning.Plan.*
import it.unibo.model.Tiling.{Position, Tile}
import it.unibo.model.Direction.{Cardinals, Diagonals, allDirections}
import it.unibo.planning.prologplanner.{PrologDFSBuilder, BasePrologBuilder, BasePrologPlanner}
import it.unibo.prologintegration.Prolog2Scala.{extractListFromTerm, extractTerm}
import it.unibo.prologintegration.Scala2Prolog.Engine

import scala.util.Try

trait Planner:
  def plan: Plan

class DummyPlanner extends Planner:
  override def plan: Plan = SucceededPlanWithMoves(List.fill(5)(Cardinals.Down), 5)

class PrologPlanner(engine: Engine, goal: Term, maxMoves: Option[Int]) extends Planner, BasePrologPlanner:
  override def plan: Plan =
    checkSolutions(engine(goal), maxMoves)

class PlannerWithTiles(initPos: (Int, Int), goal: (Int, Int), tiles: List[Tile], maxMoves: Option[Int] = None, directions: List[Direction] = allDirections) extends Planner:
  override def plan: Plan =
    println(s"Tiles: $tiles")
    PlannerBuilder()
      .withInit(initPos)
      .withGoal(goal)
      .withMaxMoves(maxMoves)
      .withTiles(tiles)
      .withDirections(directions)
      .withAlgorithm(DFS)
      .build
      .plan

class PlannerWithSpecials(initPos: (Int, Int), goal: (Int, Int), tiles: List[Tile], maxMoves: Option[Int] = None, directions: List[Direction] = allDirections) extends Planner:
  override def plan: Plan =
    println(s"Tiles: $tiles")
    PlannerBuilder()
      .withInit(initPos)
      .withGoal(goal)
      .withMaxMoves(maxMoves)
      .withTiles(tiles)
      .withDirections(directions)
      .withAlgorithm(BFS)
      .build
      .plan