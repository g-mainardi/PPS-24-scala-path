package it.unibo.planning

import it.unibo.model.Direction
import it.unibo.model.Tiling.Tile
import it.unibo.planning.Algorithm.*
import it.unibo.planning.prologplanner.PrologBuilder
import it.unibo.planning.scalaplanner.ScalaAStarBuilder
import it.unibo.planning.AStarAlgorithm

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
  protected var environmentTiles: List[Tile] = List.empty
  def withTiles(tiles: List[Tile]): BuilderDirections

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
    DFS -> "src/main/prolog/dfsWithSpecials.pl",
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

  def withTiles(tiles: List[Tile]): PlannerBuilder =
    this.environmentTiles = tiles
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
      case DFS => new PrologBuilder(configuration.copy(theoryPath = Some(theoryPaths(DFS)))).build
      case BFS => new PrologBuilder(configuration.copy(theoryPath = Some(theoryPaths(BFS)))).build
      case AStar => new ScalaAStarBuilder(configuration.copy(algorithm = Some(AStarAlgorithm))).build

case class Configuration(initPos: (Int, Int),
                         goalPos: (Int, Int),
                         maxMoves: Option[Int] = None,
                         environmentTiles: List[Tile],
                         directions: List[Direction],
                         theoryPath: Option[String] = None,
                         algorithm: Option[PathFindingAlgorithm] = None
                        )