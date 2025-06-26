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

trait SimulationController:
  var scenario: Scenario = DummyScenario()
  protected var view: Option[View] = None
  protected var planner: Planner = DummyPlanner()
  protected var currentPlan: List[Direction] = planner.plan.get

  def initSimulation(): Unit = scenario.generateScenario()
  def step(): Unit
  def generateScenario(): Unit
  def attachView(view: View): Unit
  def changeScenario(scenario: Scenario): Unit

  protected def pauseSimulation(): Unit
  protected def resumeSimulation(): Unit
  protected def resetSimulation(): Unit
  protected def resetScenario(): Unit

object SimulationControllerImpl extends SimulationController:
  override def attachView(view: View): Unit = this.view = Some(view)

  override def changeScenario(scenario: Scenario): Unit = this.scenario = scenario

  override def pauseSimulation(): Unit = ()

  override def resumeSimulation(): Unit = ()

  override def generateScenario(): Unit = ()

  override def initSimulation(): Unit =
    super.initSimulation()
    loop(GameState.current)

  override def resetSimulation(): Unit =
    scenario.resetAgent()
    updateView()

  override def resetScenario(): Unit =
    scenario.generateScenario()
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