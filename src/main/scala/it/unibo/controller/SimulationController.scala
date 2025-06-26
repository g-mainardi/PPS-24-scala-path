package it.unibo.controller

import it.unibo.model.{Direction, DummyPlanner, DummyScenario, Planner, Scenario}
import it.unibo.view.View

import scala.annotation.tailrec

object GameState:
  enum State:
    case Reset
    case Running
    case Paused
  export State.*
  @volatile private var current: State = Reset
  def getCurrent: State = synchronized{current}
  def setCurrent(s: State): Unit = synchronized{current = s}

trait SimulationController:
  def startSimulation(): Unit
  def pauseSimulation(): Unit
  def resumeSimulation(): Unit
  def resetSimulation(): Unit
  def resetScenario(): Unit
  def simulationStep(): Unit
  def generateScenario(): Unit
  def initSimulation(): Unit = scenario.generateScenario()
  def attachView(view: View): Unit
  def changeScenario(scenario: Scenario): Unit
  var scenario: Scenario = DummyScenario()
  protected var view: Option[View] = None
  protected var planner: Planner = DummyPlanner()
  protected var currentPlan: List[Direction] = planner.plan.get

object SimulationControllerImpl extends SimulationController:
  override def attachView(view: View): Unit =
    this.view = Some(view)

  override def changeScenario(scenario: Scenario): Unit =
    this.scenario = scenario

  /**
   * <li> Start a simulation: init to goal
   * <li> It can be paused/resume
   * <li> Reset stops it and it has to be re-started
   */
  override def startSimulation(): Unit =
    planner.plan foreach : p => 
      p take 3 foreach : cmd =>
        println(s"Executing... $cmd")
        scenario.agent computeCommand cmd
        view foreach {_.repaint()}
    println("Plan executed")

  override def pauseSimulation(): Unit = ()

  override def resumeSimulation(): Unit = ()
  override def generateScenario(): Unit = ()

  override def initSimulation(): Unit =
    super.initSimulation()
    loop(GameState.getCurrent)

  override def resetSimulation(): Unit =
    scenario.resetAgent()
  override def simulationStep(): Unit = ()
  override def resetScenario(): Unit =
    scenario.generateScenario()
    view foreach(_.repaint())
    resetSimulation()

  import GameState.*
  @tailrec
  private def loop(s: State): Unit =
    s match
      case Reset   => ()
      case Paused  => ()
      case Running =>
        if currentPlan.nonEmpty
        then
          step()
        else
          planOver()
    loop(GameState.getCurrent)

  private def step(): Unit =
    scenario.agent computeCommand currentPlan.head
    currentPlan = currentPlan.tail
    view foreach {_.repaint()}
    Thread sleep 500

  private def planOver(): Unit =
    println("Plan terminated!")
    GameState setCurrent Reset