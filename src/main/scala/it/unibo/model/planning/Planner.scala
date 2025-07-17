package it.unibo.model.planning

import alice.tuprolog.{SolveInfo, Term}
import it.unibo.model.planning.algorithms.Algorithm.*
import Plan.*
import it.unibo.model.fundamentals.Direction.{Cardinals, Diagonals, allDirections}
import it.unibo.model.agent.Agent
import it.unibo.model.fundamentals.{Direction, Position, Tile}
import it.unibo.model.planning.algorithms.PathFindingAlgorithm
import it.unibo.utils.prologintegration.Prolog2Scala.{extractListFromTerm, extractTerm}
import it.unibo.utils.prologintegration.Scala2Prolog.Engine

import scala.util.Try
import it.unibo.model.planning.prologplanner.Conversions.given_Conversion_Int_Int_Position
import it.unibo.model.planning.prologplanner.{BasePrologPlanner, PrologBuilder}
import it.unibo.model.planning.scalaplanner.BaseScalaPlanner

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