package it.unibo.planning

import alice.tuprolog.{SolveInfo, Term}
import it.unibo.model.Direction
import it.unibo.planning.Algorithm.*
import it.unibo.planning.Plan.*
import it.unibo.model.Tiling.{Position, Tile}
import it.unibo.model.Direction.{Cardinals, Diagonals, allDirections}
import it.unibo.planning.prologplanner.{BasePrologBuilder, BasePrologPlanner, PrologDFSBuilder}
import it.unibo.planning.scalaplanner.BaseScalaPlanner
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

class ScalaPlanner() extends Planner, BaseScalaPlanner:
  override def plan: Plan =
    ???
    