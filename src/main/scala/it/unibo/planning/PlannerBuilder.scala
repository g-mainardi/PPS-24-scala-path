package it.unibo.planning

import it.unibo.model.Direction
import it.unibo.model.Tiling.Tile
import it.unibo.planning.Algorithm.*
import it.unibo.planning.prologplanner.{PrologBFSBuilder, PrologDFSBuilder}
import it.unibo.planning.scalaplanner.ScalaAStarBuilder

// type Configuration = (Option[(Int, Int)], Option[(Int, Int)], Option[Int], Option[List[Tile]], Option[List[Direction]])

object PlannerBuilder:
  def start: BuilderInit = new PlannerBuilder()

trait BuilderInit:
  protected var initPos: Option[(Int, Int)] = None
  def withInit(initPos: (Int, Int)): BuilderGoal

trait BuilderGoal:
  protected var goalPos: Option[(Int, Int)] = None
  def withGoal(goal: (Int, Int)): BuilderConstraints

trait BuilderConstraints:
  protected var maxMoves: Option[Int] = None
  def withMaxMoves(maxMoves: Option[Int]): BuilderEnvironment

trait BuilderEnvironment:
  protected var environmentTiles: Option[List[Tile]] = None
  def withTiles(tiles: List[Tile]): BuilderDirections

trait BuilderDirections:
  protected var directions: Option[List[Direction]] = None
  def withDirections(directions: List[Direction]): BuilderAlgorithm

trait BuilderAlgorithm:
  protected var algorithm: Option[Algorithm] = None
  def withAlgorithm(algorithm: Algorithm): CompleteBuilder

trait CompleteBuilder:
  def build: Planner

private class PlannerBuilder extends BuilderInit, BuilderGoal, BuilderConstraints, BuilderEnvironment, BuilderDirections, BuilderAlgorithm, CompleteBuilder:
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

  def withAlgorithm(algorithm: Algorithm): PlannerBuilder =
    this.algorithm = Some(algorithm)
    this

  def build: Planner =
    val configuration: Configuration = Configuration (
      initPos,
      goalPos,
      maxMoves,
      environmentTiles,
      directions)

    algorithm match
      case Some(DFS) => new PrologDFSBuilder().build(configuration)
      case Some(BFS) => new PrologBFSBuilder().build(configuration)
      case Some(AStar) => new ScalaAStarBuilder().build(configuration)
      case _ => throw new IllegalArgumentException("No algorithm specified")

case class Configuration(initPos: Option[(Int, Int)],
                         goalPos: Option[(Int, Int)],
                         maxMoves: Option[Int],
                         environmentTiles: Option[List[Tile]],
                         directions: Option[List[Direction]])