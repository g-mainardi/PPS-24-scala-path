package it.unibo.model

import it.unibo.model.Tiling.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class TestScenario extends AnyFlatSpec with Matchers:
  class DummyScenario(nRows: Int, nCols: Int) extends Scenario(nRows, nCols):
    override def generate(): Unit =
      _tiles = for
        (tileType, ind) <- List(Floor(_), Grass(_), Trap(_), Water(_), Lava(_), Rock(_)).zipWithIndex
        pos: Position = Position(ind, ind)
      yield
        tileType(pos)
  "A Scenario" should "generate some tiling" in :
    val scenario: Scenario = DummyScenario(3, 3)
    scenario.generate()
    scenario.tiles should not be empty

  "A Maze Scenario" should "generate some tiling" in :
    val scenario: Scenario = Maze(3,3)
    scenario.generate()
    scenario.tiles should not be empty

  "A Terrain Scenario" should "generate some tiling" in :
    val scenario: Scenario = Terrain(3, 3)
    scenario.generate()
    scenario.tiles should not be empty