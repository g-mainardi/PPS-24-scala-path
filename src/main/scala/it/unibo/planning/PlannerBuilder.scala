package it.unibo.planning

import it.unibo.model.Direction
import it.unibo.model.Tiling.Tile

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
  def withDirections(directions: List[Direction]): CompletePlanner

trait CompletePlanner:
  def run: Plan

trait PlannerBuilder extends BuilderInit, BuilderGoal, BuilderConstraints, BuilderEnvironment, BuilderDirections, CompletePlanner:
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

  def run: Plan

trait PrologBuilder extends PlannerBuilder:
  protected var theoryPath: Option[String] = None
  def withTheoryFrom(path: String): PlannerBuilder =
    this.theoryPath = Some(path)
    this

trait ScalaBuilder extends PlannerBuilder:
  protected var algorithm: Option[PathFindingAlgorithm]
  def withAlgorithm(algorithm: PathFindingAlgorithm): PlannerBuilder