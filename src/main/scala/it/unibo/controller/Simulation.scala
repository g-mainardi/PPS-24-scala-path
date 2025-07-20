package it.unibo.controller

import it.unibo.model.fundamentals.{Direction, Position}

/**
 * Object that manages the global simulation state, used to coordinate execution and UI commands.
 *
 * It defines:
 * <li> [[State]]: a base type for all simulation-related states
 * <li> [[ExecutionState]]: current simulation activity (running, paused, etc.)
 * <li> [[UICommand]]: commands from the UI that affect the simulation
 * <li> [[SettablePosition]]: sealed hierarchy for agent goal and init positions
 * <li> Custom extractors to simplify transition handling </li> 
 *
 * The simulation state is stored in a synchronized, volatile variable [[_current]].
 * Transitions between states can be matched via pattern extractors like [[Resume]], [[Reset]], etc.
 */
object Simulation:
  sealed trait State

  /**
   * Represents the runtime status of the simulation.
   */
  enum ExecutionState extends State:
    case Running
    case Paused(fromUser: Boolean = false)
    case Step
    case Empty

  /**
   * Represents commands issued from the UI that modify the simulation.
   */
  enum UICommand extends State:
    case ChangeScenario(scenarioIndex: Int)
    case ChangeAlgorithm(algorithmIndex: Int)
    case DirectionsChoice(directions: Seq[Direction])
    case SetPosition(toSet: SettablePosition)
    case SetAnimationSpeed(speed: Double)
    case SetScenarioSize(nRows: Int, nCols: Int)

  /**
   * Represents a UI-settable position (Init or Goal).
   *
   * @param pos the position as a tuple (x, y)
   */
  enum SettablePosition(private val pos: (Int, Int)):
    case Init(private val pos: (Int, Int)) extends SettablePosition(pos)
    case Goal(private val pos: (Int, Int)) extends SettablePosition(pos)

    def position: Position = Position(pos)

  import ExecutionState.*
  import UICommand.*

  @volatile private var _current: State = Empty

  def current: State = synchronized{_current}

  def set(s: State): Unit = synchronized{_current = s}

  def exec(action: => Unit): Unit = synchronized{action}

  private[controller] type Transition = (State, State)
  
  import it.unibo.utils.PartialFunctionExtension.{~=~>, -?->}
  
  object FirstStep:
    def unapply(t: Transition): Boolean = t equals (Empty, Step)

  object ContinueStep:
    def unapply(t: Transition): Boolean = t equals (Step, Step)
    
  object Resume:
    def unapply(t: Transition): Boolean = -?-> { case (Empty | Paused(_), Running) => true }(t)

  object Pause:
    def unapply(t: Transition): Boolean = -?-> { case (Running, Paused(true)) => true }(t)

  object ConfigurationChanged:
    opaque type ConfigurationChange = ChangeScenario | SetPosition | SetScenarioSize | DirectionsChoice
    def unapply(t: Transition): Boolean = -?-> { case (_, _: ConfigurationChange ) => true }(t)

  object Reset:
    def unapply(t: Transition): Boolean = -?-> { case (Running | Paused(_), Empty) => true }(t)

  object ChangeSpeed:
    def unapply(t: Transition): Option[(State, Double)] = 
      ~=~> { case (prev: State, SetAnimationSpeed(s)) => Some(prev, s)}(t)

