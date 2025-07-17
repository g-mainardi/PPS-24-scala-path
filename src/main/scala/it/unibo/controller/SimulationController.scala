package it.unibo.controller

import it.unibo.controller.Simulation.SettablePosition.{Goal, Init}
import it.unibo.model.*
import it.unibo.model.fundamentals.Position
import it.unibo.model.planning.PlannerBuilder
import it.unibo.model.planning.algorithms.Algorithm
import it.unibo.model.scenario.Scenario

import scala.annotation.tailrec

trait SimulationController:
  private val _delay: Int = 400
  private var _speed: Double = 1.0
  protected var shouldSleep: Boolean = false

  def start(): Unit
  def stepDelay: Long = (_delay * _speed).toLong
  protected def speed: Double = _speed
  protected def speed_=(newSpeed: Double): Unit = _speed = newSpeed
  protected def step(): Unit
  protected def over(): Unit
  protected def pause(): Unit
  protected def resume(): Unit
  protected def resetSimulation(): Unit
  protected def reset(): Unit

object ScalaPathController extends SimulationController
  with DisplayableController
  with AgentManager
  with ViewAttachable:

  def pause(): Unit =
    applyToView: v =>
      v.enableStepButton()

  def resume(): Unit =
    applyToView: v =>
      v.enableStartStopButton()
      v.enableResetButton()
      v.disableStepButton()

  def assembleAgent(): Unit =
    import it.unibo.model.planning.prologplanner.Conversions.given
    agent =
      PlannerBuilder.start
        .withInit(init)
        .withGoal(goal)
        .withMaxMoves(None)
        .withTiles(scenario)
        .withDirections(directions)
        .withAlgorithm(algorithm)
        .build
        .toAgent
    updateView()

  override def generateScenario(): Unit =
    super.generateScenario()
    init = randomPosition
    goal = randomPosition
    dropAgent()
    updateView()

  def resetSimulation(): Unit =
    resetAgent()
    updateView()

  override protected def scenario_=(newScenario: Scenario): Unit =
    super.scenario_=(newScenario)
    generateScenario()
    disableControls()
    applyToView: v =>
      v.enableGenerateScenarioButton()
    resetSimulation()

  override protected def algorithm_=(newAlgorithm: Algorithm): Unit =
    super.algorithm_=(newAlgorithm)
    disableControls()
    applyToView: v =>
      v.disableGenerateScenarioButton()
    resetSimulation()

  def start(): Unit = loop(Simulation.current)

  def reset(): Unit =
    applyToView: v =>
      v.disableResetButton()
      v.enableStartStopButton()
      v.enableStepButton()
    resetSimulation()

  import Simulation.*
  private def handleTransition(from: State, to: State): Unit = (from, to) match
    case (Empty | Paused(_), Running) => resume()
    case (Running, Paused(true)) => pause()
    case (Empty, Step) =>
      applyToView: v =>
        v.enableResetButton()
    case (Step, Step) =>
      step()
      Simulation set Paused()
    case (Running | Paused(_), Empty) => reset()
    case (previous, SetAnimationSpeed(speed)) =>
      this.speed = speed
      Simulation set previous
    case (Empty, ChangeScenario(_)) =>
      applyToView: v =>
        v.enableAlgorithmDropdown()
    case _ => ()

  private def handleState(state: State): Unit = state match
    case ChangeScenario(index) =>
      scenario = scenarios(index)(nRows, nCols)
      Simulation set Empty
    case ChangeAlgorithm(index) =>
      algorithm = algorithms(index)
      searchPlan()
      Simulation set Empty
    case DirectionsChoice(selections) =>
      directions = selections
      Simulation set Empty
    case SetPosition(Goal(pos)) if pos.isAvailable =>
      goal = Position(pos)
      dropAgent()
      disableControls()
      updateView()
      Simulation set Empty
    case SetPosition(Init(pos)) if pos.isAvailable =>
      init = Position(pos)
      dropAgent()
      disableControls()
      updateView()
      Simulation set Empty
    case SetScenarioSize(r, c) if r != nRows || c != nCols =>
      nRows = r
      nCols = c
      resizeScenario(nRows, nCols)
      Simulation set Empty
    case Running =>
      step()
      if planOver
      then Simulation set Paused()
      else shouldSleep = true
    case _ => ()

  @tailrec
  private def loop(previous: State): Unit =
    val current = Simulation.current
    Simulation.exec:
      handleTransition(previous, current)
      handleState(current)
    if shouldSleep then
      shouldSleep = false
      Thread sleep stepDelay
    loop(current)

  protected def step(): Unit =
    if planOver then over()
    else
      stepAgent()
      updateView()
      if planOver then over()

  protected def over(): Unit =
    applyToView: v =>
      v.disableStepButton()
      v.disableStartStopButton()
      v.showInfoMessage("Plan terminated! You can restart it or try a new one.", "End of plan")

  protected def handleNoPlan(errorMessage: String): Unit =
    applyToView: v =>
      v.closeLoadingDialog()
      v.enableGenerateScenarioButton()
      v.showErrorMessage(s"Error: $errorMessage!\nTry to modify some parameters.", "No plan found")

  protected def handleValidPlan(nMoves: Option[Int]): Unit =
    val withResult: String = nMoves map(n => s"with $n moves") getOrElse ""
    applyToView: v =>
      v.closeLoadingDialog()
      v.enableStepButton()
      v.enableStartStopButton()
      v.enableGenerateScenarioButton()
      v.showInfoMessage(s"Plan found $withResult! Now you can execute it.", "Plan found")

  protected def searchingPlan(): Unit =
    applyToView: v =>
      v.showLoadingDialog("Agent assembled! Now searching a plan...")
