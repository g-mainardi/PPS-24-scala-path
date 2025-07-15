package it.unibo.controller

import it.unibo.model.{Agent, AgentNotBuiltException, Direction}
import it.unibo.planning.Planner

trait AgentManager:
  private var _agent: Option[Agent] = None

  def agent: Option[Agent] = _agent
  def agent_=(agent: Agent): Unit = _agent = Some(agent)

  protected def handleNoPlan(errorMessage: String): Unit
  protected def handleValidPlan(nMoves: Option[Int] = None): Unit
  protected def assembleAgent(): Unit

  protected def planOver: Boolean = _agent match
    case Some(a) => a.planOver
    case None    => true

  protected def resetAgent(): Unit = _agent foreach: agent =>
    agent.resetPosition()
    agent.resetPlan()
    agent.resetPath()
  
  protected def stepAgent(): Unit = _agent foreach (_.step())

  protected def searchPlan(): Unit =
    assembleAgent()
    println("Agent assembled! Now searching a plan...") //todo change with loading screen
    _agent match 
      case Some(agent) =>
        try handleValidPlan(agent.searchPlan)
        catch
          case e: Exception => handleNoPlan(e.getMessage)
          //case PlanNotFoundException(error) => handleNoPlan(error)
      case None => throw AgentNotBuiltException
