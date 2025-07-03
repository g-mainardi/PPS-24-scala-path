package it.unibo.prologintegration

import it.unibo.model.Plan
import it.unibo.model.Tiling.Tile

trait PlannerBuilder {
  def withTheoryFrom(path: String): PlannerBuilder
  def withInit(initPos: (Int, Int)): PlannerBuilder
  def withGoal(goal: (Int, Int)): PlannerBuilder
  def withMaxMoves(maxMoves: Option[Int]): PlannerBuilder
  def withTiles(tiles: List[Tile]): PlannerBuilder
  def run: Plan
}
