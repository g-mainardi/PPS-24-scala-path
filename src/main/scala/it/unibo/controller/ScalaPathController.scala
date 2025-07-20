package it.unibo.controller

import it.unibo.controller.Simulation.SettablePosition.{Goal, Init}
import it.unibo.model.planning.PlannerBuilder
import it.unibo.model.planning.algorithms.Algorithm
import it.unibo.model.scenario.Scenario

import scala.annotation.tailrec

/**
 * Main simulation controller for the Scala pathfinding environment.
 *
 * `ScalaPathController` orchestrates the entire simulation by extending core management traits:
 * <li> [[DisplayableController]] for scenario, algorithm, direction, and agent configuration
 * <li> [[SpeedManager]] for controlling simulation timing
 * <li> [[ViewManager]] for updating the UI  </li>
 *
 * It handles simulation state transitions, agent assembly and planning, and UI updates,
 * acting as the main reactive component of the system.
 */

object ScalaPathController
  extends DisplayableController
  with SpeedManager
  with ViewManager:

  def start(): Unit = loop(Simulation.current)

  override protected def scenario_=(newScenario: Scenario): Unit =
    super.scenario_=(newScenario)
    generateScenario()
    View.disableControls()
    resetSimulation()

  override protected def algorithm_=(newAlgorithm: Algorithm): Unit =
    super.algorithm_=(newAlgorithm)
    View.disableControls()
    resetSimulation()

  override def generateScenario(): Unit =
    super.generateScenario()
    init = randomPosition
    goal = randomPosition
    dropAgent()
    View.update()

  /**
   * Instantiates a new agent using the builder pipeline and attaches it to the view.
   */
  def assembleAgent(): Unit =
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
    View.update()

  private def resetSimulation(): Unit =
    resetAgent()
    View.update()

  /**
   * Executes one planning step, updating the view and checking completion.
   */
  private def step(): Unit =
    if planOver then View.over()
    else
      stepAgent()
      View.update()
      if planOver then View.over()

  protected def handleNoPlan(errorMessage: String): Unit =
    View noPlanFound s"Error: $errorMessage!\nTry to modify some parameters."

  protected def handleValidPlan(nMoves: Option[Int]): Unit =
    val withResult: String = nMoves map(n => s"with $n moves") getOrElse ""
    View planFound s"Plan found $withResult! Now you can execute it."

  protected def startSearch(): Unit = View.search()

  import Simulation.*
  import ExecutionState.*
  import UICommand.*

  /**
   * Main simulation loop, reacts to changes in the [[Simulation]] state.
   *
   * @param previous the last known simulation state
   */
  @tailrec
  private def loop(previous: State): Unit =
    val current = Simulation.current
    Simulation.exec:
      handleTransition(previous, current)
      handleState(current)
    delay()
    loop(current)

  import it.unibo.utils.PartialFunctionExtension.doOrNothing
  /**
   * Handles the transition between simulation states, triggering appropriate actions.
   *
   * @param from the previous state
   * @param to   the new state
   */
  private def handleTransition(from: State, to: State): Unit = doOrNothing(from, to):
    case Resume()                 => View.resume()
    case Pause()                  => View.pause()
    case FirstStep()              => View.firstStep()
    case ContinueStep()           => step(); Simulation set Paused()
    case Reset()                  => View.reset(); resetSimulation()
    case ChangeSpeed(previous, s) => speed = s; Simulation set previous
    case FirstScenario()          => View.firstScenarioChoice()

  /**
   * Handles logic specific to the current simulation state, such as updating configuration
   * or running the agent.
   *
   * @param state the current state
   */
  private def handleState(state: State): Unit = doOrNothing(state):
    case ChangeScenario(index) =>
      scenario = scenarios(index)(nRows, nCols)
      Simulation set Empty
    case ChangeAlgorithm(index) =>
      algorithm = algorithms(index)
      assembleAgent()
      searchPlan()
      Simulation set Empty
    case DirectionsChoice(selections) =>
      directions = selections
      Simulation set Empty
    case SetPosition(toChange) if toChange.position.isAvailable => toChange match
      case s: Goal => goal = s.position
      case s: Init => init = s.position
      dropAgent()
      View.disableControls()
      View.update()
      Simulation set Empty
    case SetScenarioSize(r, c) if r != nRows || c != nCols =>
      nRows = r
      nCols = c
      resizeScenario(nRows, nCols)
      Simulation set Empty
    case Running =>
      step()
      if planOver then Simulation set Paused() else shouldSleep()