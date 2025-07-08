package it.unibo.controller

object Simulation:
  enum State:
    case Reset
    case Running
    case Paused
    case Step
    case Empty
    case ChangeScenario(scenarioIndex: Int)
    case ChangeAlgorithm(algorithmIndex: Int)
  export State.*
  @volatile private var _current: State = Empty
  def current: State = synchronized{_current}
  def set(s: State): Unit = synchronized{_current = s}
  def exec(action: => Unit): Unit = synchronized{action}
