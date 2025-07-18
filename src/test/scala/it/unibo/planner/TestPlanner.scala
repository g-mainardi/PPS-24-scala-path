package it.unibo.planner

import it.unibo.model.fundamentals.{Direction, Position, Tile}
import it.unibo.model.fundamentals.Tiling.*
import it.unibo.model.scenario.{Scenario, SpecialKind, SpecialTile, Specials}
import it.unibo.model.scenario.GridDSL.*
import it.unibo.model.scenario.TileSymbol.*

import scala.util.Random

trait TestPlanner(val gridrows: Int = 3,val gridcols: Int = 3):
  protected val initPos: Position = Position(1, 1)
  protected val goalPos: Position = Position(3, 3)
  protected val directions: Seq[Direction] = Direction.allDirections
  protected val maxMoves: Option[Int] = Some(10)

  val passableScenario: Scenario = new TestScenarioWithPassableTiles(3, 3)
  val blockingScenario: Scenario = new TestScenarioWithBlockingTiles(3, 3)
  // val specialsScenario: Scenario = new TestScenarioSpecialsTiles(3, 3)
  val scenarioWithClosedWalls: Scenario = new TestScenarioWithClosedWalls

  val specialsScenario = new Specials(5, 5)
  specialsScenario.generate()

  passableScenario.generate()
  blockingScenario.generate()
  scenarioWithClosedWalls.generate()

class TestScenarioWithPassableTiles(nRows: Int, nCols: Int)
  extends Scenario(nRows, nCols):
  override def generate(): Unit =
    _tiles = (
      for {
        x <- 0 to nRows
        y <- 0 to nCols
      } yield Floor(Position(x, y))
      ).toList

class TestScenarioWithBlockingTiles(nRows: Int, nCols: Int)
  extends Scenario(nRows, nCols):
  override def generate(): Unit =
    _tiles = (
      for {
        x <- 0 to nRows
        y <- 0 to nCols
      } yield Wall(Position(x, y))
      ).toList

class TestScenarioWithClosedWalls extends Scenario(10, 10):
  private val p = Position(7, 8)
  override def generate(): Unit =
    _tiles = grid:
      (F | F | F | F | F | F | F | F | F | F). ||
      (F | F | TP(p) | F | F | F | F | F | F | F). ||
      (F | F | F | F | F | F | F | F | F | F). ||
      (F | F | F | F | F | F | F | F | F | F). ||
      (F | F | F | F | F | F | F | F | F | F). ||
      (F | F | F | F | F | F | F | F | F | F). ||
      (F | F | F | F | F | F | W | W | W | W). ||
      (F | F | F | F | F | F | W | F | F | W). ||
      (F | F | F | F | F | F | W | F | F | W). ||
      (F | F | F | F | F | F | W | W | W | W). ||