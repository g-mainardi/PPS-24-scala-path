package it.unibo.planning

import it.unibo.model.{Agent, Direction, Scenario}
import it.unibo.model.Tiling.Tile
import it.unibo.planning.Algorithm.*
import it.unibo.planning.prologplanner.PrologBuilder
import it.unibo.planning.scalaplanner.ScalaBuilder
import it.unibo.planning.AStarAlgorithm


case class Configuration(initPos: (Int, Int),
                         goalPos: (Int, Int),
                         maxMoves: Option[Int] = None,
                         environmentTiles: Scenario,
                         directions: List[Direction],
                         theoryPath: Option[String] = None,
                         algorithm: Option[PathFindingAlgorithm] = None
                          )

object PlannerBuilder:
  def start: BuilderInit = new PlannerBuilder()

trait BuilderInit:
  protected var initPos: (Int, Int) = (0, 0)
  def withInit(initPos: (Int, Int)): BuilderGoal

trait BuilderGoal:
  protected var goalPos: (Int, Int) = (0,0)
  def withGoal(goal: (Int, Int)): BuilderConstraints

trait BuilderConstraints:
  protected var maxMoves: Option[Int] = None
  def withMaxMoves(maxMoves: Option[Int]): BuilderEnvironment

trait BuilderEnvironment:
  protected var environmentTiles: Scenario = _
  def withTiles(scenario: Scenario): BuilderDirections

trait BuilderDirections:
  protected var directions: List[Direction] = List.empty
  def withDirections(directions: List[Direction]): BuilderAlgorithm

trait BuilderAlgorithm:
  protected var algorithm: Algorithm = BFS
  def withAlgorithm(algorithm: Algorithm): CompleteBuilder

trait CompleteBuilder:
  def build: Planner

private class PlannerBuilder extends BuilderInit, BuilderGoal, BuilderConstraints, BuilderEnvironment, BuilderDirections, BuilderAlgorithm, CompleteBuilder:
  private val theoryPaths: Map[Algorithm, String] = Map(
    DFS -> "src/main/prolog/dfs.pl",
    BFS -> "src/main/prolog/bfs.pl"
  )

  def withInit(initPos: (Int, Int)): PlannerBuilder =
    this.initPos = initPos
    this

  def withGoal(goal: (Int, Int)): PlannerBuilder =
    this.goalPos = goal
    this

  def withMaxMoves(maxMoves: Option[Int]): PlannerBuilder =
    this.maxMoves = maxMoves
    this

  def withTiles(scenario: Scenario): PlannerBuilder =
    this.environmentTiles = scenario
    this

  def withDirections(directions: List[Direction]): PlannerBuilder =
    this.directions = directions
    this

  def withAlgorithm(algorithm: Algorithm): PlannerBuilder =
    this.algorithm = algorithm
    this

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