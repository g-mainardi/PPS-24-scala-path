package it.unibo.controller

import it.unibo.model.Direction.{allDirections, randomDirections}
import it.unibo.model.{Direction, EmptyScenario, Maze, Scenario, SpecialTileBuilder, Specials, Terrain}
import it.unibo.model.Tiling.Position
import it.unibo.planning.Algorithm

trait ScenarioManager:
  val builder = new SpecialTileBuilder // todo ???
  val scenarios: List[(Int, Int) => Scenario] =
    for
      scenarioType <- EmptyScenario.apply _ :: Terrain.apply _ :: Maze.apply _ :: Specials.apply _ :: Nil
    yield
      scenarioType

  private var (_nRows, _nCols) = (Scenario.nRows, Scenario.nCols)
  private var _scenario: Scenario = scenarios head (_nRows, _nCols)
  generateScenario() // todo teniamo?
  private var _init: Position = randomPosition
  private var _goal: Position = randomPosition

  def init: Position = _init
  def goal: Position = _goal
  def init_=(pos: Position): Unit = _init = pos
  def goal_=(pos: Position): Unit = _goal = pos
  def nRows: Int = _nRows
  def nRows_=(nRows: Int): Unit = _nRows = nRows
  def nCols: Int = _nCols
  def nCols_=(nCols: Int): Unit = _nCols = nCols
  def scenariosNames: List[String] = scenarios map(supp => supp(1, 1).toString) //todo
  def scenario: Scenario = _scenario
  protected def scenario_=(newScenario: Scenario): Unit = _scenario = newScenario
  protected def resizeScenario(nRows: Int, nCols: Int): Unit =
    val constructor = _scenario.getClass.getConstructors.head
    val newParams: Array[Object] = Array(
      nRows.asInstanceOf[Object],
      nCols.asInstanceOf[Object]
    )
    scenario = constructor.newInstance(newParams*).asInstanceOf[Scenario]
  protected def generateScenario(): Unit = _scenario.generate()
  protected def randomPosition: Position = _scenario.randomFreePosition match
    case Some(pos) => pos
    case None => throw IllegalStateException("Scenario has no valid tiles")

  extension (p: (Int, Int))
    def isAvailable: Boolean = _scenario.freePositions contains Position(p)

trait AlgorithmManager:
  val algorithms: List[Algorithm] = Algorithm.values.toList
  private var _algorithm: Algorithm = algorithms.head

  def algorithmsNames: List[String] = algorithms map (_.toString)
  def algorithm: Algorithm = _algorithm
  protected def algorithm_=(newAlgorithm: Algorithm): Unit = _algorithm = newAlgorithm

trait DirectionManager:
  private var _directions: List[Direction] = allDirections

  def directions: List[Direction] = _directions
  protected def directions_=(directions: List[Direction]): Unit = _directions = directions

trait DisplayableController 
  extends ScenarioManager 
  with AlgorithmManager 
  with DirectionManager
  with AgentManager