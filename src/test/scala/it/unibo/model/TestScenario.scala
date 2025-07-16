package it.unibo.model

import it.unibo.model.Tiling.{Position, Teleport, Tile}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class TestScenario extends AnyFlatSpec with Matchers:
  "A Scenario" should "generate some tiling" in :
    val scenario: Scenario = DummyScenario(3, 3)
    scenario.generate()
    scenario.tiles should not be empty

//  "A Scenario" should "have agent in initial position" in :
//    val scenario: Scenario = DummyScenario(3, 3)
//    scenario.generate()
//    scenario.agent.pos shouldEqual scenario.initialPosition

//  "A DummyScenario" should "have initial position (0, 0)" in :
//    val scenario: DummyScenario = DummyScenario(3, 3)
//    scenario.initialPosition.x should be(0)
//    scenario.initialPosition.y should be(0)

  "A TrampolineScenario" should "have a maximum of traps" in :
    val scenario: Traps = Traps(3,3)
    def countTrampoline(tiles: List[Tile]): Int = tiles count :
      case _: Teleport => true
      case _ => false
    countTrampoline(scenario.tiles) should be (0)
    // scenario.generate()
    // countTrampoline(scenario.tiles) shouldEqual scenario.nTrampolines

//  "A Maze Scenario" should "have initial position (1, 1)" in :
//    val scenario: Maze = Maze()
//    scenario.initialPosition.x should be(1)
//    scenario.initialPosition.y should be(1)

  "A Maze Scenario" should "generate some tiling" in :
    val scenario: Scenario = Maze(3,3)
    scenario.generate()
    scenario.tiles should not be empty

  "A Terrain Scenario" should "generate some tiling" in :
    val scenario: Scenario = Terrain(3, 3)
    scenario.generate()
    scenario.tiles should not be empty

  "A Traps Scenario" should "generate some tiling" in :
    val scenario: Scenario = Traps(3, 3)
    scenario.generate()
    scenario.tiles should not be empty