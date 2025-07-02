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
  @volatile private var _current: State = Empty
  def current: State = synchronized{_current}
  def set(s: State): Unit = synchronized{_current = s}

trait ScenarioManager:
  protected var _scenarios: List[Scenario] = Terrain() :: Maze() :: Traps() :: Nil
  protected var _scenario: Scenario = _scenarios.head

  def scenariosNames: List[String] = _scenarios map(_.toString)
  def scenario: Scenario = _scenario
  protected def changeScenario(newScenario: Scenario): Unit = _scenario = newScenario
  protected def generateScenario(): Unit

trait PlannerManager:
  protected var planner: Option[Planner] = None
  protected var _currentPlan: List[Direction] = List()

  protected def noPlan: List[Direction] = List()
  protected def refreshPlan(): Unit = _currentPlan = planner match
    case Some(p) => p.plan getOrElse noPlan
    case None    => noPlan
  protected def refreshPlanner(): Unit

trait PathManager:
  private var _path: List[Position] = List()

  protected def addToPath(p: Position): Unit = _path = _path :+ p
  protected def resetPath(): Unit = _path = List()
  def path: List[Position] = _path

trait DisplayableController extends ScenarioManager with PathManager

trait ViewAttachable:
  protected var _view: Option[View] = None

  final def attachView(v: View): Unit = _view = Some(v)
  protected def updateView(): Unit = _view foreach {_.repaint()}

trait ControllableSimulation:
  protected def pause(): Unit
  protected def resume(): Unit
  protected def resetSimulation(): Unit
  protected def resetScenario(): Unit

trait SimulationController extends ScenarioManager:
  protected def step(): Unit
  def initSimulation(): Unit

object SimulationControllerImpl extends SimulationController
  with DisplayableController
  with PlannerManager
  with ViewAttachable
  with ControllableSimulation:

  // planner = Some(DummyPlanner())
  // planner = Some(BasePlanner((0,0), (2,2), 5))
  // planner = Some(PlannerWithTiles((0,0), (5,5), 100, scenario.tiles))

  override def pause(): Unit = ()

  override def resume(): Unit = ()

  import it.unibo.prologintegration.Conversions.given

  def refreshPlanner(): Unit = planner =
    Some(PlannerWithMoves(_scenario.initialPosition, _scenario.goalPosition, _scenario.tiles))

  override def generateScenario(): Unit =
    _scenario.generate()
    updateView()

  override def initSimulation(): Unit =
    generateScenario()
    refreshPlanner()
    refreshPlan()
    loop(GameState.current)

  override def resetSimulation(): Unit =
    _scenario.resetAgent()
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
        if _currentPlan.nonEmpty
        then
          step()
          Thread sleep 500
        else
          planOver()
      case ChangeScenario(scenarioIndex) =>
        changeScenario(_scenarios(scenarioIndex))
        refreshPlanner()
        refreshPlan()
        println("Current plan " + _currentPlan)
        println("Current tiles " + _scenario.tiles)
        GameState set Empty

    loop(GameState.current)

  override def step(): Unit =
    addToPath(_scenario.agent.pos)
    _scenario.agent computeCommand _currentPlan.head
    _currentPlan = _currentPlan.tail
    updateView()

  private def planOver(): Unit =
    println("Plan terminated!")
    GameState set Reset
