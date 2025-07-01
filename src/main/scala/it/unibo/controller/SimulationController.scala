package it.unibo.controller

import it.unibo.model.*
import it.unibo.view.View
import Tiling.Position

import scala.annotation.tailrec

object GameState:
  enum State:
    case Reset
    case Running
    case Paused
    case Step
    case Empty
    case ChangeScenario(scenarioIndex: Int)
  export State.*
  @volatile private var currentState: State = Empty
  def current: State = synchronized{currentState}
  def set(s: State): Unit = synchronized{currentState = s}

trait ScenarioManager:
  var scenarios: List[Scenario] = Terrain() :: Maze() :: Traps() :: Nil
  var scenario: Scenario = scenarios.head

  def getScenarioNames: List[String] = scenarios.map(_.toString)
  protected def changeScenario(newScenario: Scenario): Unit = scenario = newScenario
  protected def generateScenario(): Unit

trait PlannerManager:
  protected var planner: Option[Planner] = None
  protected var currentPlan: List[Direction] = List()

  protected def refreshPlan(): Unit = currentPlan = planner match
    case Some(p) => p.plan getOrElse List()
    case None    => List()

trait PathManager:
  protected var path: List[Position] = List()

  protected def addToPath(p: Position): Unit = path = path :+ p
  protected def resetPath(): Unit = path = List()
  def getPath: List[Position] = path

trait ViewAttachable:
  protected var view: Option[View] = None

  final def attachView(v: View): Unit = view = Some(v)

trait ControllableSimulation:
  protected def pause(): Unit
  protected def resume(): Unit
  protected def resetSimulation(): Unit
  protected def resetScenario(): Unit

trait SimulationController extends ScenarioManager:
  def step(): Unit
  def initSimulation(): Unit

object SimulationControllerImpl extends SimulationController
  with ScenarioManager
  with PlannerManager
  with PathManager
  with ViewAttachable
  with ControllableSimulation:

  // planner = Some(DummyPlanner())
  // planner = Some(BasePlanner((0,0), (2,2), 5))
  // planner = Some(PlannerWithTiles((0,0), (5,5), 100, scenario.tiles))

  override def pause(): Unit = ()

  override def resume(): Unit = ()

  def refreshPlanner(): Unit = planner =
    Some(PlannerWithTiles((1, 1), (4, 3), 10, scenario.tiles))

  override def generateScenario(): Unit =
    scenario.generate()
    updateView()

  override def initSimulation(): Unit =
    generateScenario()
    planner = Some(PlannerWithTiles((0,0), (3,3), 5, scenario.tiles))
    refreshPlan()
    loop(GameState.current)

  override def resetSimulation(): Unit =
    scenario.resetAgent()
    resetPath()
    updateView()

  override protected def changeScenario(newScenario: Scenario): Unit =
    super.changeScenario(newScenario)
    generateScenario()

  override def resetScenario(): Unit =
    generateScenario()
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
      case ChangeScenario(scenarioIndex) =>
        changeScenario(scenarios(scenarioIndex))
        refreshPlanner()
        refreshPlan()
        println("Current plan " + currentPlan)
        println("Current tiles " + scenario.tiles)
        GameState set Empty

    loop(GameState.current)

  override def step(): Unit =
    addToPath(scenario.agent.pos)
    scenario.agent computeCommand currentPlan.head
    currentPlan = currentPlan.tail
    updateView()

  private def planOver(): Unit =
    println("Plan terminated!")
    GameState set Reset

  private def updateView(): Unit =
    view foreach {_.repaint()}