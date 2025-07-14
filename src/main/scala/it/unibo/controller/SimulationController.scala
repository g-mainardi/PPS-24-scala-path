package it.unibo.controller

import it.unibo.model.*
import it.unibo.view.View
import Tiling.Position
import it.unibo.model.Direction.allDirections
import it.unibo.planning.Plan.*
import it.unibo.planning.{Algorithm, Planner, PlannerBuilder}

import scala.annotation.tailrec
import scala.swing.Swing.onEDT

trait ScenarioManager:
  import SpecialTileBuilder.*
  define("Teleport")(_ => Scenario.randomPosition)
  define("JumpDown")(pos => Position(pos.x + 2, pos.y))
  define("StairsUp")(pos => Position(pos.x - 2, pos.y))

  protected var _scenarios: List[Scenario] =  Terrain() :: Maze() :: Specials() :: Nil
  protected var _scenario: Scenario = _scenarios.head

  def scenariosNames: List[String] = _scenarios map(_.toString)
  def scenario: Scenario = _scenario
  protected def changeScenario(newScenario: Scenario): Unit = _scenario = newScenario
  protected def generateScenario(): Unit

trait AlgorithmManager:
  protected var _algorithms: List[Algorithm] = Algorithm.values.toList
  protected var _algorithm: Algorithm = _algorithms.head

  def algorithmsNames: List[String] = _algorithms map (_.toString)
  def algorithm: Algorithm = _algorithm
  protected def changeAlgorithm(newAlgorithm: Algorithm): Unit = _algorithm = newAlgorithm

trait PathManager:
  private var _path: List[Position] = List()

  protected def addToPath(p: Position): Unit = _path = _path :+ p
  protected def resetPath(): Unit = _path = List()
  def path: List[Position] = _path

trait DisplayableController extends ScenarioManager with PathManager with AlgorithmManager

trait PlannerManager:
  protected var planner: Option[Planner] = None
  private var _currentPlan: List[Direction] = List()
  private var _planIndex: Int = 0

  protected def handleNoPlan(): Unit
  protected def handleValidPlan(): Unit
  protected def planOver: Boolean = _planIndex >= _currentPlan.length
  protected def nextDirection: Direction =
    try _currentPlan(_planIndex)
    finally _planIndex += 1
  protected def resetPlan(): Unit = _planIndex = 0

  private object ValidPlanner:
    def unapply(plannerOpt: Option[Planner]): Option[List[Direction]] = plannerOpt map: p =>
      p.plan match
        case SucceededPlanWithMoves(directions, _) => directions
        case SucceededPlan(directions) =>  directions
        case FailedPlan(error) => println(error); List.empty

  protected def refreshPlan(): Unit =
    refreshPlanner()
    resetPlan()
    println("Planner built! Now searching a plan...")
    _currentPlan = planner match
    case ValidPlanner(directions) if directions.nonEmpty =>
      handleValidPlan()
      println("Plan found!")
      directions
    case _                        =>
      handleNoPlan()
      List.empty

  protected def refreshPlanner(): Unit

trait ViewAttachable:
  private var _view: Option[View] = None

  final def attachView(v: View): Unit = _view = Some(v)
  final protected def applyToView(viewAction: View => Unit): Unit = onEDT:
    _view foreach viewAction
  protected def updateView(): Unit = applyToView: v =>
      v.repaint()
  protected def disableControls(): Unit = applyToView: v =>
    v.disableStepButton()
    v.disableResetButton()
    v.disableStartButton()
    v.disablePauseResumeButton()

trait ControllableSimulation:
  protected def pause(): Unit
  protected def resume(): Unit
  protected def resetSimulation(): Unit

trait SimulationController:
  val stepDelay = 500
  protected var shouldSleep: Boolean = false

  def start(): Unit
  protected def step(): Unit
  protected def over(): Unit

object ScalaPathController extends SimulationController
  with DisplayableController
  with PlannerManager
  with ViewAttachable
  with ControllableSimulation:

  override def pause(): Unit =
    applyToView: v =>
      v.enableStepButton()

  override def resume(): Unit =
    applyToView: v =>
      v.enablePauseResumeButton()
      v.enableResetButton()
      v.disableStepButton()

  import it.unibo.planning.prologplanner.Conversions.given

  override def refreshPlanner(): Unit = planner = Some:
    PlannerBuilder.start
      .withInit(_scenario.initialPosition)
      .withGoal(_scenario.goalPosition)
      .withMaxMoves(None)
      .withTiles(_scenario.tiles)
      .withDirections(allDirections)  //todo implement GUI integration
      .withAlgorithm(_algorithm)
      .build

  override def generateScenario(): Unit =
    _scenario.generate()
    updateView()

  override def resetSimulation(): Unit =
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

  override def start(): Unit = loop(Simulation.current)

  private def reset(): Unit =
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

  private def handleState(state: State): Unit = current match
    case ChangeScenario(scenario) =>
      changeScenario(_scenarios(scenario))
      Simulation set Empty
    case ChangeAlgorithm(algorithm) =>
      changeAlgorithm(_algorithms(algorithm))
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

  override protected def step(): Unit =
    if planOver then over()
    else
      addToPath(_scenario.agent.pos)
      _scenario.agent computeCommand nextDirection
      updateView()
      if planOver then over()

  override protected def over(): Unit =
    println("Plan terminated!")
    applyToView: v =>
      v.disableStepButton()
      v.disableStartButton()
      v.disablePauseResumeButton()

  override protected def handleNoPlan(): Unit =
    applyToView: v =>
      v.enableGenerateScenarioButton()

  override protected def handleValidPlan(): Unit =
    applyToView: v =>
      v.enableStepButton()
      v.enableStartButton()
      v.enableGenerateScenarioButton()
