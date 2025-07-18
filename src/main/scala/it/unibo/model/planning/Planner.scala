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
import it.unibo.model.planning.prologplanner.{BasePrologPlanner, PrologBuilder}
import it.unibo.model.planning.scalaplanner.BaseScalaPlanner

/**
 * A generic interface for all planners, indipendently from the algorithm implementation.
 * A Planner is responsible for computing a valid Plan to reach the goal from the initial position.
 *
 * @param configuration the implicit configuration of the planning problem
 */
trait Planner(using configuration: Configuration):
  def plan: Plan
  def toAgent: Agent = new Agent(
      configuration.initPos,
      () => this.plan,
      configuration.environmentTiles.checkSpecial,
    )

/**
 * A trivial planner implementation that always returns a hardcoded downward plan.
 * Useful for testing or placeholder logic.
 */
class DummyPlanner(using configuration: Configuration) extends Planner:
  override def plan: Plan = SucceededPlanWithMoves(List.fill(5)(Cardinals.Down), 5)

/**
 * A planner based on a Prolog engine. Executes a Prolog query
 * to generate a movement plan from the initial state to the goal.
 *
 * @param engine the Prolog engine instance
 * @param goal   the Prolog term representing the goal state
 */
class PrologPlanner(engine: Engine, goal: Term)(using configuration: Configuration) extends Planner, BasePrologPlanner:
  override def plan: Plan =
    checkSolutions(engine(goal))

/**
 * A planner implemented purely in Scala using a pathfinding algorithm
 * (e.g., A*). Computes a plan over a tile-based environment.
 *
 * @param start      the starting position
 * @param goal       the target position
 * @param tiles      the environment tiles
 * @param directions allowed movement directions
 * @param algorithm  the pathfinding algorithm to use
 */
class ScalaPlanner(start: Position, goal: Position, tiles: Seq[Tile], directions: Seq[Direction], algorithm: PathFindingAlgorithm)(using configuration: Configuration) extends Planner, BaseScalaPlanner:
  override def plan: Plan =
    checkSolution(algorithm.run(start, goal, tiles, directions))