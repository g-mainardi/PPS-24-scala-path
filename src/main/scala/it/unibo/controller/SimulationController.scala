package it.unibo.controller

import it.unibo.model.{Direction, DummyPlanner, DummyScenario, Planner, Scenario}
import it.unibo.view.View

import scala.annotation.tailrec

object GameState:
  enum State:
    case Reset
    case Running
    case Paused
    case Step
    case Empty
  export State.*
  @volatile private var currentState: State = Empty
  def current: State = synchronized{currentState}
  def set(s: State): Unit = synchronized{currentState = s}

trait ScenarioManager:
  var scenario: Scenario = DummyScenario()

  def changeScenario(newScenario: Scenario): Unit = scenario = newScenario
  def generateScenario(): Unit

trait PlannerManager:
  protected var planner: Option[Planner] = Some(DummyPlanner())
  protected var currentPlan: List[Direction] = List()

  def refreshPlan(): Unit = currentPlan = planner match
    case Some(p) => p.plan getOrElse List()
    case None    => List()

trait ViewAttachable:
  protected var view: Option[View] = None

  final def attachView(v: View): Unit = view = Some(v)

trait ControllableSimulation:
  def pause(): Unit
  def resume(): Unit
  def resetSimulation(): Unit
  def resetScenario(): Unit

trait SimulationController extends ScenarioManager:
  def step(): Unit
  def initSimulation(): Unit

object SimulationControllerImpl extends SimulationController
  with ScenarioManager
  with PlannerManager
  with ViewAttachable
  with ControllableSimulation:

  override def pause(): Unit = ()

  override def resume(): Unit = ()

  override def generateScenario(): Unit = scenario.generateScenario()

  override def initSimulation(): Unit =
    generateScenario()
    refreshPlan()
    loop(GameState.current)

  override def resetSimulation(): Unit =
    scenario.resetAgent()
    updateView()

  override def resetScenario(): Unit =
    generateScenario()
    updateView()
    resetSimulation()

  import GameState.*
  @tailrec
  private def loop(s: State): Unit =
    s match
      case Empty => ()
      case Reset   =>
        resetSimulation()
        GameState set Empty
      case Paused  => ()
      case Step =>
        step()
        GameState set Paused
      case Running =>
        if currentPlan.nonEmpty
        then
          step()
          Thread sleep 500
        else
          planOver()
    loop(GameState.current)

  override def step(): Unit =
    scenario.agent computeCommand currentPlan.head
    currentPlan = currentPlan.tail
    updateView()

  private def planOver(): Unit =
    println("Plan terminated!")
    GameState set Reset
  
  private def updateView(): Unit =
    view foreach {_.repaint()}