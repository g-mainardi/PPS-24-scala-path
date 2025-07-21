package it.unibo.controller

import Simulation.*
import ExecutionState.*
import UICommand.*
import it.unibo.model.fundamentals.{Direction, Position}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestSimulation extends AnyFlatSpec with Matchers:

  val speed = 2.5
  val scenarioIndex = 2
  val algorithmIndex = 1
  val init: (Int, Int) = (1, 2)
  val goal: (Int, Int) = (5, 6)

  "Simulation state" should "be initially Empty" in:
    Simulation set Empty
    Simulation.current shouldBe Empty

  it should "change state correctly when set" in:
    Simulation set Running
    Simulation.current shouldBe Running

    Simulation set Paused(true)
    Simulation.current shouldBe Paused(true)

    Simulation set Step
    Simulation.current shouldBe Step

  "ExecutionState" should "handle Paused state with user flag" in:
    val pausedByUser = Paused(true)
    val pausedBySystem = Paused()

    pausedByUser match
      case Paused(fromUser) => fromUser shouldBe true
      case _ => fail("Should match Paused case")

    pausedBySystem match
      case Paused(fromUser) => fromUser shouldBe false
      case _ => fail("Should match Paused case")

  "UICommand" should "correctly create ChangeScenario command" in:
    val command = ChangeScenario(scenarioIndex)
    command match
      case ChangeScenario(index) => index shouldBe scenarioIndex
      case _ => fail("Should match ChangeScenario case")

  it should "correctly create ChangeAlgorithm command" in:
    val command = ChangeAlgorithm(algorithmIndex)
    command match
      case ChangeAlgorithm(index) => index shouldBe algorithmIndex
      case _ => fail("Should match ChangeAlgorithm case")

  it should "correctly create DirectionsChoice command" in:
    val directions = Seq(Direction.Cardinals.Up, Direction.Cardinals.Down)
    val command = DirectionsChoice(directions)
    command match
      case DirectionsChoice(dirs) => dirs shouldBe directions
      case _ => fail("Should match DirectionsChoice case")

  it should "correctly create SetAnimationSpeed command" in:
    val command = SetAnimationSpeed(speed)
    command match
      case SetAnimationSpeed(s) => s shouldBe speed
      case _ => fail("Should match SetAnimationSpeed case")

  it should "correctly create SetScenarioSize command" in:
    val (nRows, nCols) = (10, 15)
    val command = SetScenarioSize(nRows, nCols)
    command match
      case SetScenarioSize(rows, cols) =>
        rows shouldBe nRows
        cols shouldBe nCols
      case _ => fail("Should match SetScenarioSize case")

  "SettablePosition" should "correctly handle Init position" in:
    val initPos = SettablePosition.Init(init)
    initPos.position shouldBe Position(init)

  it should "correctly handle Goal position" in:
    val goalPos = SettablePosition.Goal(goal)
    goalPos.position shouldBe Position(goal)

  "UICommand SetPosition" should "work with Init position" in:
    val initPos = SettablePosition.Init(init)
    val command = SetPosition(initPos)
    command match
      case SetPosition(pos) =>
        pos shouldBe initPos
        pos.position shouldBe Position(init)
      case _ => fail("Should match SetPosition case")

  it should "work with Goal position" in:
    val goalPos = SettablePosition.Goal(goal)
    val command = SetPosition(goalPos)
    command match
      case SetPosition(pos) =>
        pos shouldBe goalPos
        pos.position shouldBe Position(goal)
      case _ => fail("Should match SetPosition case")

  "Transition extractors" should "match FirstStep correctly" in:
    val transition = (Empty, Step)
    transition match
      case FirstStep() => succeed
      case _ => fail("Should match FirstStep")

  they should "match ContinueStep correctly" in:
    val transition = (Step, Step)
    transition match
      case ContinueStep() => succeed
      case _ => fail("Should match ContinueStep")

  they should "match Resume from Empty correctly" in:
    val transition = (Empty, Running)
    transition match
      case Resume() => succeed
      case _ => fail("Should match Resume")

  they should "match Resume from Paused correctly" in:
    val transition = (Paused(true), Running)
    transition match
      case Resume() => succeed
      case _ => fail("Should match Resume")

  they should "match Pause correctly" in:
    val transition = (Running, Paused(true))
    transition match
      case Pause() => succeed
      case _ => fail("Should match Pause")

  they should "match Reset from Running correctly" in:
    val transition = (Running, Empty)
    transition match
      case Reset() => succeed
      case _ => fail("Should match Reset")

  they should "match Reset from Paused correctly" in:
    val transition = (Paused(), Empty)
    transition match
      case Reset() => succeed
      case _ => fail("Should match Reset")

  they should "match ConfigurationChanged correctly" in:
    val transition = (Empty, ChangeScenario(scenarioIndex))
    transition match
      case ConfigurationChanged() => succeed
      case _ => fail("Should match ConfigurationChanged")

  they should "match ChangeSpeed correctly" in:
    val transition = (Running, SetAnimationSpeed(speed))
    transition match
      case ChangeSpeed(prevState, extractedSpeed) =>
        prevState shouldBe Running
        extractedSpeed shouldBe speed
      case _ => fail("Should match ChangeSpeed")

  "Simulation exec" should "execute actions in synchronized block" in:
    var executed = false
    Simulation.exec:
      executed = true
    executed shouldBe true