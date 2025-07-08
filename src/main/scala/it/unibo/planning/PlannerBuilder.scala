package it.unibo.planning

import it.unibo.model.Direction
import it.unibo.model.Tiling.Tile
import it.unibo.planning.{DFS, BFS}
import it.unibo.planning.prologplanner.{BFSBuilder, DFSBuilder}
import it.unibo.planning.scalaplanner.BaseScalaPlannerBuilder

object PlannerBuilder:
  def start: BuilderInit = new PlannerBuilder()

trait BuilderInit:
  var initPos: Option[(Int, Int)] = None
  def withInit(initPos: (Int, Int)): BuilderGoal

trait BuilderGoal:
  var goalPos: Option[(Int, Int)] = None
  def withGoal(goal: (Int, Int)): BuilderConstraints

trait BuilderConstraints:
  var maxMoves: Option[Int] = None
  def withMaxMoves(maxMoves: Option[Int]): BuilderEnvironment

trait BuilderEnvironment:
  var environmentTiles: Option[List[Tile]] = None
  def withTiles(tiles: List[Tile]): BuilderDirections

trait BuilderDirections:
  var directions: Option[List[Direction]] = None
  def withDirections(directions: List[Direction]): BuilderAlgorithm

trait BuilderAlgorithm:
  var algorithm: Option[PathFindingAlgorithm] = None
  def withAlgorithm(algorithm: PathFindingAlgorithm): CompleteBuilder

trait CompleteBuilder:
  def build: Planner

class PlannerBuilder extends BuilderInit, BuilderGoal, BuilderConstraints, BuilderEnvironment, BuilderDirections, BuilderAlgorithm, CompleteBuilder:
  def withInit(initPos: (Int, Int)): PlannerBuilder =
    this.initPos = Some(initPos)
    this

  def withGoal(goal: (Int, Int)): PlannerBuilder =
    this.goalPos = Some(goal)
    this

  def withMaxMoves(maxMoves: Option[Int]): PlannerBuilder =
    this.maxMoves = maxMoves
    this

  def withTiles(tiles: List[Tile]): PlannerBuilder =
    this.environmentTiles = Some(tiles)
    this

  def withDirections(directions: List[Direction]): PlannerBuilder =
    this.directions = Some(directions)
    this

  def withAlgorithm(algorithm: PathFindingAlgorithm): PlannerBuilder =
    this.algorithm = Some(algorithm)
    this

  def build(): Planner =
    val configuration = Configuration(
      initPos,
      goalPos,
      maxMoves,
      environmentTiles,
      directions)

    algorithm match
    case DFS => new DFSBuilder().build(configuration)
    case BFS => new BFSBuilder().build(configuration)
    case AStar => new BaseScalaPlannerBuilder()

class PrologBuilder
class ScalaBuilder

case class Configuration(initPos: Option[(Int, Int)],
                         goalPos: Option[(Int, Int)],
                         // theoryPath: Option[String],
                         maxMoves: Option[Int],
                         environmentTiles: Option[List[Tile]],
                         directions: Option[List[Direction]])