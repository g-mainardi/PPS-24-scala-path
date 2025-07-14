package it.unibo.controller

import it.unibo.model.Direction
import it.unibo.model.Direction.allDirections
import it.unibo.model.Tiling.Position
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestDisplayableController extends AnyFlatSpec with Matchers:

  "A ScenarioManager" should "correctly switch Scenario and give names" in :
    object TestScenarioManager extends ScenarioManager:
      override protected def generateScenario(): Unit = ()
      def testScenarioChanging(index: Int): Unit = changeScenario(scenarios(index))

    TestScenarioManager.scenariosNames.zipWithIndex foreach: (name, index) =>
      TestScenarioManager testScenarioChanging index
      TestScenarioManager.scenario.toString should be (name)

  "An AlgorithmManager" should "correctly switch Algorithm and give names" in :
    object TestAlgorithmManager extends AlgorithmManager:
      def testAlgorithmChanging(index: Int): Unit = changeAlgorithm(algorithms(index))

    TestAlgorithmManager.algorithmsNames.zipWithIndex foreach : (name, index) =>
      TestAlgorithmManager testAlgorithmChanging index
      TestAlgorithmManager.algorithm.toString should be(name)
  
  "A PathManager" should "correctly collect and reset positions" in :
    object TestPathManager extends PathManager:
      def testAdd(x: Int, y: Int, dir: Direction): Unit = addToPath(Position(x, y), dir)
      def testReset(): Unit = resetPath()

    // Initial state
    TestPathManager.path should be (empty)

    // Adding positions
    val dim: Int = 5
    for
      x <- 0 until dim
      y <- 0 until dim
      dir = allDirections((x + y) % allDirections.length)
    do
      TestPathManager testAdd (x, y, dir)
      TestPathManager.path.last should be (Position(x, y), dir)
    TestPathManager.path should have size dim * dim

    // Resetting
    TestPathManager.testReset()
    TestPathManager.path should be (empty)
