package it.unibo.controller

import it.unibo.model.Direction

object Simulation:
  enum State:
    case Running
    case Paused(fromUser: Boolean = false)
    case Step
    case Empty
    case ChangeScenario(scenarioIndex: Int)
    case ChangeAlgorithm(algorithmIndex: Int)
    case DirectionsChoice(directions: List[Direction])
  export State.*
  @volatile private var _current: State = Empty
  def current: State = synchronized{_current}
  def set(s: State): Unit = synchronized{_current = s}
  def exec(action: => Unit): Unit = synchronized{action}
