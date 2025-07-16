package it.unibo.planning

import alice.tuprolog.{SolveInfo, Term}
import it.unibo.model.{Agent, Direction}
import it.unibo.planning.Algorithm.*
import it.unibo.planning.Plan.*
import it.unibo.model.Tiling.{Position, Tile}
import it.unibo.model.Direction.{Cardinals, Diagonals, allDirections}
import it.unibo.planning.prologplanner.{BasePrologPlanner, PrologBuilder}
import it.unibo.planning.scalaplanner.BaseScalaPlanner
import it.unibo.prologintegration.Prolog2Scala.{extractListFromTerm, extractTerm}
import it.unibo.prologintegration.Scala2Prolog.Engine

import scala.util.Try
import it.unibo.planning.AStarAlgorithm

import it.unibo.planning.prologplanner.Conversions.given_Conversion_Int_Int_Position

trait Planner(using configuration: Configuration):
  def plan: Plan
  def toAgent: Agent = new Agent(
      configuration.initPos,
      () => this.plan,
      configuration.environmentTiles.checkSpecial,
    )

class DummyPlanner(using configuration: Configuration) extends Planner:
  override def plan: Plan = SucceededPlanWithMoves(List.fill(5)(Cardinals.Down), 5)

class PrologPlanner(engine: Engine, goal: Term)(using configuration: Configuration) extends Planner, BasePrologPlanner:
  override def plan: Plan =
    checkSolutions(engine(goal))

class ScalaPlanner(start: Position, goal: Position, tiles: List[Tile], directions: List[Direction], algorithm: PathFindingAlgorithm)(using configuration: Configuration) extends Planner, BaseScalaPlanner:
  override def plan: Plan =
    checkSolution(algorithm.run(start, goal, tiles, directions))