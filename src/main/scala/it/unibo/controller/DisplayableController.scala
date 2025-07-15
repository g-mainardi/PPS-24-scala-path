package it.unibo.controller

import it.unibo.model.Direction.allDirections
import it.unibo.model.{Direction, Maze, Scenario, SpecialTileBuilder, Specials, Terrain}
import it.unibo.model.Tiling.Position
import it.unibo.planning.Algorithm

trait ScenarioManager:
  val builder = new SpecialTileBuilder
  builder tile "Teleport" does (_ => Scenario.randomPosition)
  builder tile "JumpDown" does (pos => Position(pos.x + 2, pos.y))
  builder tile "StairsUp" does (pos => Position(pos.x - 2, pos.y))

  val scenarios: List[Scenario] =  Terrain(7,7) :: Maze(7,7) :: Specials(7,7) :: Nil
  protected var _scenario: Scenario = scenarios.head //todo correctly encapsulates with private

  protected def generateScenario(): Unit
  
  def scenariosNames: List[String] = scenarios map(_.toString)
  def scenario: Scenario = _scenario
  protected def changeScenario(newScenario: Scenario): Unit = _scenario = newScenario

trait AlgorithmManager:
  val algorithms: List[Algorithm] = Algorithm.values.toList
  private var _algorithm: Algorithm = algorithms.head

  def algorithmsNames: List[String] = algorithms map (_.toString)
  def algorithm: Algorithm = _algorithm
  protected def changeAlgorithm(newAlgorithm: Algorithm): Unit = _algorithm = newAlgorithm

trait DirectionManager:
  private var _directions: List[Direction] = allDirections

  def directions: List[Direction] = _directions
  protected def setDirections(directions: List[Direction]): Unit = _directions = directions

trait PathManager:
  private var _path: List[(Position, Direction)] = List()

  def path: List[(Position, Direction)] = _path
  protected def addToPath(p: Position, d: Direction): Unit = _path = _path :+ (p, d)
  protected def resetPath(): Unit = _path = List()

trait DisplayableController extends ScenarioManager with PathManager with AlgorithmManager with DirectionManager