package it.unibo.controller

import it.unibo.model.*
import it.unibo.view.View
import Tiling.Position
import it.unibo.model.Direction.allDirections
import it.unibo.planning.Plan.*
import it.unibo.planning.{Algorithm, Planner, PlannerBuilder}

import javax.swing.SwingUtilities
import scala.annotation.tailrec

trait ScenarioManager:
  import SpecialTileBuilder.*
  define("Teleport")(_ => Scenario.randomPosition)
  define("JumpDown")(pos => Position(pos.x + 2, pos.y))
  define("StairsUp")(pos => Position(pos.x - 2, pos.y))

  protected var _scenarios: List[Scenario] = Terrain() :: Maze() :: ScenarioWithSpecials() :: Nil
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
  protected var _currentPlan: List[Direction] = List()

  protected def handleNoPlan(): Unit
  protected def handleValidPlan(): Unit

  private object ValidPlanner:
    def unapply(plannerOpt: Option[Planner]): Option[List[Direction]] = plannerOpt map: p =>
      p.plan match
        case SucceededPlanWithMoves(directions, _) => directions
        case SucceededPlan(directions) =>  directions
        case FailedPlan(error) => println(error); List.empty

  protected def refreshPlan(): Unit =
    refreshPlanner()
    _currentPlan = planner match
    case ValidPlanner(directions) if directions.nonEmpty =>
      handleValidPlan()
      directions
    case _                        =>
      handleNoPlan()
      List.empty

  protected def refreshPlanner(): Unit

trait ViewAttachable:
  private var _view: Option[View] = None

  final def attachView(v: View): Unit = _view = Some(v)
  final protected def applyToView(viewAction: View => Unit): Unit = SwingUtilities invokeLater: () =>
    _view foreach:
      viewAction(_)
  protected def updateView(): Unit = applyToView: v =>
      v.repaint()

trait ControllableSimulation:
  protected def pause(): Unit
  protected def resume(): Unit
  protected def resetSimulation(): Unit

trait SimulationController:
  def init(): Unit
  def start(): Unit
  protected def step(): Unit
  protected def over(): Unit

object ScalaPathController extends SimulationController
  with DisplayableController
  with PlannerManager
  with ViewAttachable
  with ControllableSimulation:

  override def pause(): Unit = ()

  override def resume(): Unit = ()

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
    applyToView: v =>
      v.disableGenerateScenarioButton()
      v.disableStepButton()
      v.disableResetButton()
      v.disableStartButton()
      v.disablePauseResumeButton()
    _scenario.generate()
    updateView()

  override def init(): Unit =
    generateScenario()
    refreshPlan()

  override def resetSimulation(): Unit =
    _scenario.resetAgent()
    resetPath()
    updateView()

  override protected def changeScenario(newScenario: Scenario): Unit =
    super.changeScenario(newScenario)
    generateScenario()
    resetSimulation()

  override def start(): Unit = loop(Simulation.current)

  import Simulation.*
  @tailrec
  private def loop(s: State): Unit =
    Simulation.exec:
      s match
        case Empty => ()
        case Reset   =>
          applyToView: v =>
            v.disableResetButton()
            v.enableStartButton()
            v.enableStepButton()
          resetSimulation()
          Simulation set Empty
        case Paused  => ()
        case Step =>
          applyToView: v =>
            v.enableResetButton()
          step()
          Simulation set Paused
        case Running =>
          applyToView: v =>
            v.enablePauseResumeButton()
            v.enableResetButton()
          step()
          if _currentPlan.isEmpty
          then Simulation set Paused
          else Thread sleep 500

        case ChangeScenario(scenarioIndex) =>
          changeScenario(_scenarios(scenarioIndex))
          println("Current plan " + _currentPlan)
          println("Current tiles " + _scenario.tiles)
          Simulation set Empty
        case ChangeAlgorithm(algorithmIndex) =>
          changeAlgorithm(_algorithms(algorithmIndex))
          refreshPlan()
          Simulation set Empty

    loop(Simulation.current)

  override protected def step(): Unit = _currentPlan match
    case h :: t =>
      addToPath(_scenario.agent.pos)
      _scenario.agent computeCommand h
      _currentPlan = t
      updateView()
      if t.isEmpty then over()
    case _ =>
      over()

  override protected def over(): Unit =
    println("Plan terminated!")
    applyToView: v =>
      v.disableStepButton()
      v.disableStartButton()
      v.disablePauseResumeButton()
    Thread sleep 1000

  override protected def handleNoPlan(): Unit =
    applyToView: v =>
      v.enableGenerateScenarioButton()

  override protected def handleValidPlan(): Unit =
    applyToView: v =>
      v.enableStepButton()
      v.enableStartButton()
      v.enableGenerateScenarioButton()
