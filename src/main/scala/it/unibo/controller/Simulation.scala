package it.unibo.controller

import it.unibo.model.fundamentals.{Direction, Position}

object Simulation:
  enum State:
    case Running
    case Paused(fromUser: Boolean = false)
    case Step
    case Empty
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
  
  export State.*

  @volatile private var _current: State = Empty

  def current: State = synchronized{_current}

  def set(s: State): Unit = synchronized{_current = s}

  def exec(action: => Unit): Unit = synchronized{action}
