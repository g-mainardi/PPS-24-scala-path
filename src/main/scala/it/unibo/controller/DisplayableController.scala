package it.unibo.controller

import it.unibo.model.fundamentals.Position

import scala.collection.immutable.ListMap

object DisplayableController:


  /**
   * Manages the simulation scenario, including:
   * <li> current map type (e.g., 
   * [[it.unibo.model.scenario.Maze]], 
   * [[it.unibo.model.scenario.Terrain]], 
   * [[it.unibo.model.scenario.Specials]])
   * <li> map dimensions
   * <li> scenario generation and resizing
   * <li> init/goal positions
   */
  trait ScenarioManager:
    import it.unibo.model.scenario.{EmptyScenario, Maze, Scenario, Specials, Terrain}
    private val scenarioMap: ListMap[String, (Int, Int) => Scenario] = ListMap(
      "Terrain" -> Terrain.apply,
      "Maze" -> Maze.apply,
      "Specials" -> Specials.apply
    )

    private var (_nRows, _nCols) = (Scenario.nRows, Scenario.nCols)
    private var _scenario: Scenario = EmptyScenario(_nRows, _nCols)
    generateScenario()

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
    def scenarios: Seq[(Int, Int) => Scenario] = scenarioMap.values.toList
    def scenariosNames: Seq[String] = scenarioMap.keys.toList
    def scenario: Scenario = _scenario
    protected def scenario_=(newScenario: Scenario): Unit = _scenario = newScenario
    protected def resizeScenario(nRows: Int, nCols: Int): Unit =
      _nRows = nRows
      _nCols = nCols
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

    extension (p: Position)
      def isAvailable: Boolean = _scenario.freePositions contains p

  /**
   * Manages the planning algorithm selection.
   * Provides access to the list of available algorithms and the currently selected one.
   */
  trait AlgorithmManager:
    import it.unibo.model.planning.algorithms.Algorithm
    val algorithms: Seq[Algorithm] = Algorithm.values.toList
    private var _algorithm: Algorithm = algorithms.head

    def algorithmsNames: Seq[String] = algorithms map (_.toString)
    def algorithm: Algorithm = _algorithm
    protected def algorithm_=(newAlgorithm: Algorithm): Unit = _algorithm = newAlgorithm


  /**
   * Manages the set of allowed movement directions.
   * Allows the controller to restrict or change which directions the agent can use.
   */
  trait DirectionManager:
    import it.unibo.model.fundamentals.Direction
    import Direction.allDirections
    private var _directions: Seq[Direction] = allDirections

    def directions: Seq[Direction] = _directions
    protected def directions_=(directions: Seq[Direction]): Unit = _directions = directions

import DisplayableController.*

/**
 * A high-level controller that manages all displayable aspects of the simulation:
 * the scenario, planning algorithm, available directions, and the agent itself.
 *
 * This controller aggregates:
 * <li> scenario configuration and generation logic
 * <li> algorithm selection
 * <li> movement direction settings
 * <li> agent management (via [[AgentManager]])</li> 
 *
 * It acts as the main interface between the simulation logic and the UI layer.
 */
trait DisplayableController 
  extends ScenarioManager 
  with AlgorithmManager 
  with DirectionManager
  with AgentManager