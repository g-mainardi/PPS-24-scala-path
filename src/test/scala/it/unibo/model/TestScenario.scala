package it.unibo.model

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestScenario extends AnyFlatSpec with Matchers:
  "A Scenario" should "generate some tiling" in :
    val scenario: Scenario = DummyScenario()
    scenario.generateScenario()
    scenario.tiles should not be empty  

  "A Scenario" should "have agent in initial position" in :
    val scenario: Scenario = DummyScenario()
    scenario.generateScenario()
    scenario.agent shouldEqual scenario.initialPosition

  "A DummyScenario" should "have initial position (0, 0)" in :
    val scenario: DummyScenario = DummyScenario()
    scenario.initialPosition.x should be(0)
    scenario.initialPosition.y should be(0)