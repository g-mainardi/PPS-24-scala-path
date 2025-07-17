package it.unibo.planner

import it.unibo.model.fundamentals.{Tile, Position}
import it.unibo.model.fundamentals.Tiling.*
import it.unibo.model.scenario.Scenario

import scala.util.Random

trait TestPlanner(gridrows: Int = 3,val gridcols: Int = 3) {
  
  val passableTiles: List[Tile] = (
    for {
      x <- 0 to gridrows
      y <- 0 to gridcols
    } yield Floor(Position(x, y))
    ).toList

  val blockingTiles: List[Tile] = (
    for {
      x <- 0 to gridrows
      y <- 0 to gridcols
    } yield Wall(Position(x, y))
    ).toList

  val specialTiles: List[Tile] = (
    for {
      x <- 0 to gridrows
      y <- 0 to gridcols
    } yield {
      val pos = Position(x, y)
      if (x == 1 && y == 1) Teleport(pos, pos)
      if (x == 2 && y == 2) Teleport(pos, pos)
      else Floor(pos)
    }).toList
    }

class TestScenarioWithPassableTiles(nRows: Int, nCols: Int)
  extends Scenario(nRows, nCols) with TestPlanner(nRows, nCols):
  override def generate(): Unit =
    _tiles = passableTiles

class TestScenarioWithBlockingTiles(nRows: Int, nCols: Int)
  extends Scenario(nRows, nCols) with TestPlanner(nRows, nCols):
  override def generate(): Unit =
    _tiles = blockingTiles