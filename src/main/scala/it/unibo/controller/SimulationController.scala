package it.unibo.controller

import it.unibo.model.*
import it.unibo.model.Direction.allDirections
import it.unibo.planning.{Algorithm, PlannerBuilder}

import scala.annotation.tailrec

trait SimulationController:
  val stepDelay = 500
  protected var shouldSleep: Boolean = false

  def start(): Unit
  protected def step(): Unit
  protected def over(): Unit
  protected def pause(): Unit
  protected def resume(): Unit
  protected def resetSimulation(): Unit
  protected def reset(): Unit

object ScalaPathController extends SimulationController
  with DisplayableController
  with PlanManager
  with ViewAttachable:

  def pause(): Unit =
    applyToView: v =>
      v.enableStepButton()

  def resume(): Unit =
    applyToView: v =>
      v.enablePauseResumeButton()
      v.enableResetButton()
      v.disableStepButton()

  def refreshPlanner(): Unit = setPlanner:
    import it.unibo.planning.prologplanner.Conversions.given
    PlannerBuilder.start
      .withInit(_scenario.initialPosition)
      .withGoal(_scenario.goalPosition)
      .withMaxMoves(None)
      .withTiles(_scenario.tiles)
      .withDirections(allDirections)  //todo implement GUI integration
      .withAlgorithm(algorithm)
      .build

  def generateScenario(): Unit =
    _scenario.generate()
    updateView()

  def resetSimulation(): Unit =
    _scenario.resetAgent()
    resetPath()
    updateView()

  override protected def changeScenario(newScenario: Scenario): Unit =
    super.changeScenario(newScenario)
    generateScenario()
    disableControls()
    applyToView: v =>
      v.enableGenerateScenarioButton()
    resetSimulation()

  override protected def changeAlgorithm(newAlgorithm: Algorithm): Unit =
    super.changeAlgorithm(newAlgorithm)
    disableControls()
    applyToView: v =>
      v.disableGenerateScenarioButton()
    resetSimulation()

  def start(): Unit = loop(Simulation.current)

  def reset(): Unit =
    resetPlan()
    applyToView: v =>
      v.disablePauseResumeButton()
      v.disableResetButton()
      v.enableStartButton()
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
    case _ => ()

  private def handleState(state: State): Unit = state match
    case ChangeScenario(scenario) =>
      changeScenario(scenarios(scenario))
      Simulation set Empty
    case ChangeAlgorithm(algorithm) =>
      changeAlgorithm(algorithms(algorithm))
      refreshPlan()
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
      addToPath(_scenario.agent.pos, currentDirection)
      _scenario.agent computeCommand nextDirection
      updateView()
      if planOver then over()

  protected def over(): Unit =
    applyToView: v =>
      v.disableStepButton()
      v.disableStartButton()
      v.disablePauseResumeButton()
      v.showInfoMessage("Plan terminated! You can restart it or generate a new Scenario.", "End of plan")

  protected def handleNoPlan(errorMessage: String): Unit =
    applyToView: v =>
      v.enableGenerateScenarioButton()
      v.showErrorMessage(s"Error: $errorMessage!\nTry to generate some parameters.", "No plan found")

  protected def handleValidPlan(nMoves: Option[Int]): Unit =
    val withResult: String = nMoves match
      case Some(nMoves) => s"with $nMoves moves"
      case None         => s"within _ moves" //todo max moves inserted
    applyToView: v =>
      v.enableStepButton()
      v.enableStartButton()
      v.enableGenerateScenarioButton()
      v.showInfoMessage(s"Plan found $withResult! Now you can execute it.", "Plan found")
