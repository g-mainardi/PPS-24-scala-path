package it.unibo.controller

import it.unibo.model.fundamentals.{Direction, Position}

object Simulation:
  trait State

  enum ExecutionState extends State:
    case Running
    case Paused(fromUser: Boolean = false)
    case Step
    case Empty

  enum UICommand extends State:
    case ChangeScenario(scenarioIndex: Int)
    case ChangeAlgorithm(algorithmIndex: Int)
    case DirectionsChoice(directions: List[Direction])
    case SetPosition(toSet: SettablePosition)
    case SetAnimationSpeed(speed: Double)
    case SetScenarioSize(nRows: Int, nCols: Int)

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
    def unapply(t: Transition): Boolean = t equals(Step, Step)
    
  object Resume:
    def unapply(t: Transition): Boolean = -?-> { case (Empty | Paused(_), Running) => true }(t)

  object Pause:
    def unapply(t: Transition): Boolean = -?-> { case (Running, Paused(true)) => true }(t)

  object FirstScenario:
    def unapply(t: Transition): Boolean = -?-> { case (Empty, ChangeScenario(_)) => true }(t)

  object Reset:
    def unapply(t: Transition): Boolean = -?-> { case (Running | Paused(_), Empty) => true }(t)

  object ChangeSpeed:
    def unapply(t: Transition): Option[(State, Double)] = 
      ~=~> { case (prev: State, SetAnimationSpeed(s)) => Some(prev, s)}(t)

