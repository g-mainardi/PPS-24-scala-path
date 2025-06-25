package it.unibo.model

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestScenario extends AnyFlatSpec with Matchers:
  "A DummyScenario" should "have initial position" in:
    val scenario = DummyScenario()
    scenario.initialPosition.x should be (0)
    scenario.initialPosition.y should be (0)

  "A DummyScenario" should "generate some tiling" in :
    val scenario = DummyScenario()
    scenario.generateScenario()
    scenario.tiles should not be empty  
