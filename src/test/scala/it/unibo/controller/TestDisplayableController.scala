package it.unibo.controller

import DisplayableController.*
import it.unibo.model.fundamentals.Direction
import it.unibo.model.fundamentals.Position
import it.unibo.model.scenario.EmptyScenario
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestDisplayableController extends AnyFlatSpec with Matchers:

  "A ScenarioManager" should "correctly switch Scenario and give names" in :
    object TestScenarioManager extends ScenarioManager:
      def testScenarioChanging(index: Int): Unit =
        scenario = scenarios(index)(20, 20)

    TestScenarioManager.scenariosNames.zipWithIndex foreach: (name, index) =>
      TestScenarioManager testScenarioChanging index
      TestScenarioManager.scenario.toString should be (name)

  "An AlgorithmManager" should "correctly switch Algorithm and give names" in :
    object TestAlgorithmManager extends AlgorithmManager:
      def testAlgorithmChanging(index: Int): Unit = algorithm_=(algorithms(index))

    TestAlgorithmManager.algorithmsNames.zipWithIndex foreach : (name, index) =>
      TestAlgorithmManager testAlgorithmChanging index
      TestAlgorithmManager.algorithm.toString should be(name)

  "A DirectionManager" should "set and get directions correctly" in :
    object TestDirectionManager extends DirectionManager:
      def setDirections(dirs: Seq[Direction]): Unit = directions_=(dirs)

    val testDirections = Seq(Direction.Cardinals.Up, Direction.Cardinals.Left)
    TestDirectionManager.setDirections(testDirections)
    TestDirectionManager.directions shouldBe testDirections

  "A ScenarioManager" should "resize scenario correctly" in:
    object TestScenarioManager extends ScenarioManager:
      def resizeTo(rows: Int, cols: Int): Unit = resizeScenario(rows, cols)

    TestScenarioManager.resizeTo(5, 15)
    TestScenarioManager.nRows shouldBe 5
    TestScenarioManager.nCols shouldBe 15
    TestScenarioManager.scenario.nRows shouldBe 5
    TestScenarioManager.scenario.nCols shouldBe 15

  "A ScenarioManager" should "set and get init and goal positions" in :
    object TestScenarioManager extends ScenarioManager
    val pos1 = Position(1, 2)
    val pos2 = Position(3, 4)
    TestScenarioManager.init_=(pos1)
    TestScenarioManager.goal_=(pos2)
    TestScenarioManager.init shouldBe pos1
    TestScenarioManager.goal shouldBe pos2

  it should "throw exception if randomPosition is called on empty scenario" in :
    object TestScenarioManager extends ScenarioManager:
      override def randomPosition: Position =
        scenario = new EmptyScenario(0, 0)
        super.randomPosition
    an [IllegalStateException] should be thrownBy TestScenarioManager.randomPosition

  "A DirectionManager" should "set and get directions for empty and custom sequences" in :
    object TestDirectionManager extends DirectionManager:
      def setDirections(dirs: Seq[Direction]): Unit = directions_=(dirs)
    TestDirectionManager.setDirections(Seq())
    TestDirectionManager.directions shouldBe empty
    val customDirs = Seq(Direction.Cardinals.Down)
    TestDirectionManager.setDirections(customDirs)
    TestDirectionManager.directions shouldBe customDirs

