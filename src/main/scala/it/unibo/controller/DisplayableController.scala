package it.unibo.controller

import it.unibo.model.fundamentals.Position

object DisplayableController:
  trait ScenarioManager:
    import it.unibo.model.scenario.{EmptyScenario, Maze, Scenario, Specials, Terrain}
    val scenarios: Seq[(Int, Int) => Scenario] =
      for
        scenarioType <- Terrain.apply :: Maze.apply :: Specials.apply :: Nil
      yield
        scenarioType

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
    def scenariosNames: Seq[String] = scenarios map(supp => supp(1, 1).toString) //todo
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

    extension (p: Position)
      def isAvailable: Boolean = _scenario.freePositions contains p

  trait AlgorithmManager:
    import it.unibo.model.planning.algorithms.Algorithm
    val algorithms: Seq[Algorithm] = Algorithm.values.toList
    private var _algorithm: Algorithm = algorithms.head

    def algorithmsNames: Seq[String] = algorithms map (_.toString)
    def algorithm: Algorithm = _algorithm
    protected def algorithm_=(newAlgorithm: Algorithm): Unit = _algorithm = newAlgorithm

  trait DirectionManager:
    import it.unibo.model.fundamentals.Direction
    import Direction.allDirections
    private var _directions: Seq[Direction] = allDirections

    def directions: Seq[Direction] = _directions
    protected def directions_=(directions: Seq[Direction]): Unit = _directions = directions

import DisplayableController.*
trait DisplayableController 
  extends ScenarioManager 
  with AlgorithmManager 
  with DirectionManager
  with AgentManager