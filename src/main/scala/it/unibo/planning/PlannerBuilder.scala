package it.unibo.planning

import it.unibo.model.Tiling.Tile

//trait BuilderInit:
//  protected var initPos: Option[(Int, Int)] = None
//  protected var goalPos: Option[(Int, Int)] = None
//
//  def withInit(initPos: (Int, Int)): BuilderInit =
//    this.initPos = Some(initPos)
//    this
//
//  def withGoal(goal: (Int, Int)): BuilderInit =
//    this.goalPos = Some(goal)
//    this
//
//trait BuilderConstraints:
//  protected var maxMoves: Int = 0
//  def withMaxMoves(maxMoves: Option[Int]): PlannerBuilder
//
//trait BuilderEnvironment:
//  protected var environmentTiles: List[Tile] = List.empty
//  def withTiles(tiles: List[Tile]): PlannerBuilder

//trait PlannerBuilder extends BuilderInit, BuilderConstraints, BuilderEnvironment:
//  def run: Plan

trait PlannerBuilder:
  protected var initPos: Option[(Int, Int)] = None
  protected var goalPos: Option[(Int, Int)] = None
  protected var environmentTiles: List[Tile] = List.empty
  protected var maxMoves: Option[Int] = None

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
    this.environmentTiles = tiles
    this

  def run: Plan

trait PrologBuilder extends PlannerBuilder:
  protected var theoryStr: String = ""
  def withTheoryFrom(path: String): PlannerBuilder

trait ScalaBuilder extends PlannerBuilder:
  def withAlgorithm(algorithm: PathFindingAlgorithm): PlannerBuilder