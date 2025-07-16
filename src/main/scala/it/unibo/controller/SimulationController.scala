package it.unibo.controller

import it.unibo.controller.Simulation.SettablePosition.{Goal, Init}
import it.unibo.model.*
import it.unibo.model.Direction.allDirections
import it.unibo.model.Tiling.Position
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

  def assembleAgent(): Unit = agent = 
    import it.unibo.planning.prologplanner.Conversions.given
    PlannerBuilder.start
      .withInit(init)
      .withGoal(goal)
      .withMaxMoves(None)
      .withTiles(scenario)
      .withDirections(directions)
      .withAlgorithm(algorithm)
      .build
      .toAgent

  def generateScenario(): Unit =
    _scenario.generate()
    updateView()

  def resetSimulation(): Unit =
    resetAgent()
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
    case _ => ()

  private def handleState(state: State): Unit = state match
    case ChangeScenario(scenario) =>
      changeScenario(scenarios(scenario))
      Simulation set Empty
    case ChangeAlgorithm(algorithm) =>
      changeAlgorithm(algorithms(algorithm))
      searchPlan()
      Simulation set Empty
    case DirectionsChoice(directions) =>
      setDirections(directions)
      Simulation set Empty
    case SetPosition(Goal(pos)) => 
      goal = Position(pos)
      updateView()
      Simulation set Empty
    case SetPosition(Init(pos)) => 
      init = Position(pos)
      updateView()
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
      v.showInfoMessage("Plan terminated! You can restart it or generate a new Scenario.", "End of plan")

  protected def handleNoPlan(errorMessage: String): Unit =
    applyToView: v =>
      v.enableGenerateScenarioButton()
      v.showErrorMessage(s"Error: $errorMessage!\nTry to modify some parameters.", "No plan found")

  protected def handleValidPlan(nMoves: Option[Int]): Unit =
    val withResult: String = nMoves match
      case Some(nMoves) => s"with $nMoves moves"
      case None         => s"within _ moves" //todo max moves inserted
    applyToView: v =>
      v.enableStepButton()
      v.enableStartStopButton()
      v.enableGenerateScenarioButton()
      v.showInfoMessage(s"Plan found $withResult! Now you can execute it.", "Plan found")
