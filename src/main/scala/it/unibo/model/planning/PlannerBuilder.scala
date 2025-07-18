package it.unibo.model.planning

import it.unibo.model.fundamentals.{Direction, Position, Tile}
import it.unibo.model.agent.Agent
import it.unibo.model.planning.prologplanner.PrologBuilder
import it.unibo.model.planning.scalaplanner.ScalaBuilder
import it.unibo.model.scenario.Scenario
import it.unibo.model.planning.algorithms.Algorithm.*
import it.unibo.model.planning.algorithms.{AStarAlgorithm, Algorithm, PathFindingAlgorithm}

/**
 * Configuration for a pathfinding task, containing all necessary parameters.
 *
 * @param initPos          starting position (x, y)
 * @param goalPos          goal position (x, y)
 * @param maxMoves         optional movement limit
 * @param environmentTiles the scenario (tiles map)
 * @param directions       the set of directions the agent can move
 * @param theoryPath       optional path to a Prolog file (if using Prolog planner)
 * @param algorithm        optional Scala algorithm (e.g., AStar)
 */
case class Configuration(initPos: Position,
                         goalPos: Position,
                         maxMoves: Option[Int] = None,
                         environmentTiles: Scenario,
                         directions: Seq[Direction],
                         theoryPath: Option[String] = None,
                         algorithm: Option[PathFindingAlgorithm] = None
                          )

/**
 * Entry point for creating a Planner using the fluent builder API.
 */
object PlannerBuilder:
  def start: BuilderInit = new PlannerBuilder()

/**
 * Abstract builder that collects configuration,
 * enforcing a specific method order.
 * It offers a fluent interface
 */
trait BuilderInit:
  protected var initPos: Position = Position(0, 0)
  def withInit(initPos: Position): BuilderGoal

trait BuilderGoal:
  protected var goalPos: Position = Position(0,0)
  def withGoal(goal: Position): BuilderConstraints

trait BuilderConstraints:
  protected var maxMoves: Option[Int] = None
  def withMaxMoves(maxMoves: Option[Int]): BuilderEnvironment

trait BuilderEnvironment:
  protected var environmentTiles: Scenario = _
  def withTiles(scenario: Scenario): BuilderDirections

trait BuilderDirections:
  protected var directions: Seq[Direction] = Seq.empty
  def withDirections(directions: Seq[Direction]): BuilderAlgorithm

trait BuilderAlgorithm:
  protected var algorithm: Algorithm = BFS
  def withAlgorithm(algorithm: Algorithm): CompleteBuilder

trait CompleteBuilder:
  def build: Planner

/**
 * Concrete builder that collects configuration data step-by-step,
 * then instantiates either a Prolog or Scala planner.
 */
private class PlannerBuilder extends BuilderInit, BuilderGoal, BuilderConstraints, BuilderEnvironment, BuilderDirections, BuilderAlgorithm, CompleteBuilder:
  private val theoryPaths: Map[Algorithm, String] = Map(
    DFS -> "src/main/prolog/dfs.pl",
    BFS -> "src/main/prolog/bfs.pl"
  )

  def withInit(initPos: Position): PlannerBuilder =
    this.initPos = initPos
    this

  def withGoal(goal: Position): PlannerBuilder =
    this.goalPos = goal
    this

  def withMaxMoves(maxMoves: Option[Int]): PlannerBuilder =
    this.maxMoves = maxMoves
    this

  def withTiles(scenario: Scenario): PlannerBuilder =
    this.environmentTiles = scenario
    this

  def withDirections(directions: Seq[Direction]): PlannerBuilder =
    this.directions = directions
    this

  def withAlgorithm(algorithm: Algorithm): PlannerBuilder =
    this.algorithm = algorithm
    this

  /**
   * Builds the planner based on the algorithm type
   * @return a concrete Planner instance
   */
  def build: Planner =
    val configuration: Configuration = Configuration (
      initPos,
      goalPos,
      maxMoves,
      environmentTiles,
      directions)

    algorithm match
      case DFS | BFS =>
        given Configuration = configuration.copy(theoryPath = Some(theoryPaths(algorithm)))
        new PrologBuilder().build
      case AStar =>
        given Configuration = configuration.copy(algorithm = Some(AStarAlgorithm))
        new ScalaBuilder().build