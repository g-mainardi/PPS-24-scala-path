package it.unibo.controller

import it.unibo.model.Tiling.Position
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestController extends AnyFlatSpec with Matchers:

  "A ScenarioManager" should "correctly switch Scenario and give names" in :
    object TestScenarioManager extends ScenarioManager:
      override protected def generateScenario(): Unit = ()
      def testScenarioChanging(index: Int): Unit = changeScenario(_scenarios(index))

    TestScenarioManager.scenariosNames.zipWithIndex foreach: (name, index) =>
      TestScenarioManager testScenarioChanging index
      TestScenarioManager.scenario.toString should be (name)

  "A PathManager" should "correctly collect and reset positions" in :
    object TestPathManager extends PathManager:
      def testAdd(x: Int, y: Int): Unit = addToPath(Position(x, y))
      def testReset(): Unit = resetPath()

    // Initial state
    TestPathManager.path should be (empty)

    // Adding positions
    val dim: Int = 5
    for
      x <- 0 until dim
      y <- 0 until dim
    do
      TestPathManager testAdd (x, y)
      TestPathManager.path.last should be (Position(x, y))
    TestPathManager.path should have size dim * dim

    // Resetting
    TestPathManager.testReset()
    TestPathManager.path should be (empty)
