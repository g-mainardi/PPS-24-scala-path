package it.unibo.controller

import it.unibo.model.{Maze, Scenario, SpecialTileBuilder, Specials, Terrain}
import it.unibo.model.Tiling.Position
import it.unibo.planning.Algorithm

trait ScenarioManager:
  import SpecialTileBuilder.*
  define("Teleport")(_ => Scenario.randomPosition)
  define("JumpDown")(pos => Position(pos.x + 2, pos.y))
  define("StairsUp")(pos => Position(pos.x - 2, pos.y))

  val scenarios: List[Scenario] =  Terrain() :: Maze() :: Specials() :: Nil
  protected var _scenario: Scenario = scenarios.head //todo correctly encapsulates with private

  def scenariosNames: List[String] = scenarios map(_.toString)
  def scenario: Scenario = _scenario
  protected def changeScenario(newScenario: Scenario): Unit = _scenario = newScenario
  protected def generateScenario(): Unit

trait AlgorithmManager:
  val algorithms: List[Algorithm] = Algorithm.values.toList
  private var _algorithm: Algorithm = algorithms.head

  def algorithmsNames: List[String] = algorithms map (_.toString)
  def algorithm: Algorithm = _algorithm
  protected def changeAlgorithm(newAlgorithm: Algorithm): Unit = _algorithm = newAlgorithm

trait PathManager:
  private var _path: List[Position] = List()

  protected def addToPath(p: Position): Unit = _path = _path :+ p
  protected def resetPath(): Unit = _path = List()
  def path: List[Position] = _path

trait DisplayableController extends ScenarioManager with PathManager with AlgorithmManager