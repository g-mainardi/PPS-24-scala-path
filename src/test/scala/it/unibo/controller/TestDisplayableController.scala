package it.unibo.controller

import it.unibo.model.Direction
import it.unibo.model.Direction.allDirections
import it.unibo.model.Tiling.Position
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestDisplayableController extends AnyFlatSpec with Matchers:

//  "A ScenarioManager" should "correctly switch Scenario and give names" in :
//    object TestScenarioManager extends ScenarioManager:
//      override protected def generateScenario(): Unit = ()
//      def testScenarioChanging(index: Int): Unit = scenario_=(scenarios(index))
//
//    TestScenarioManager.scenariosNames.zipWithIndex foreach: (name, index) =>
//      TestScenarioManager testScenarioChanging index
//      TestScenarioManager.scenario.toString should be (name)

  "An AlgorithmManager" should "correctly switch Algorithm and give names" in :
    object TestAlgorithmManager extends AlgorithmManager:
      def testAlgorithmChanging(index: Int): Unit = algorithm_=(algorithms(index))

    TestAlgorithmManager.algorithmsNames.zipWithIndex foreach : (name, index) =>
      TestAlgorithmManager testAlgorithmChanging index
      TestAlgorithmManager.algorithm.toString should be(name)
