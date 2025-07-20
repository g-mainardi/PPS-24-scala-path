package it.unibo.controller

import it.unibo.model.agent.Agent
import it.unibo.model.exceptions.AgentNotBuiltException
import it.unibo.model.fundamentals.Direction
import it.unibo.model.planning.Planner

/**
 * Trait responsible for managing the lifecycle and behavior of an [[it.unibo.model.Agent]],
 * including planning, resetting, stepping, and handling planning outcomes.
 *
 * Subclasses must implement the abstract methods to start the planning process
 * and handle the results (success or failure).
 */

trait AgentManager:
  private var _agent: Option[Agent] = None

  def agent: Option[Agent] = _agent
  def agent_=(agent: Agent): Unit = _agent = Some(agent)

  protected def startSearch(): Unit
  protected def handleNoPlan(errorMessage: String): Unit
  protected def handleValidPlan(nMoves: Option[Int] = None): Unit
  protected def assembleAgent(): Unit

  /**
   * Checks whether the agent has completed its plan.
   *
   * @return true if the plan is over or no agent is present
   */
  protected def planOver: Boolean = _agent match
    case Some(a) => a.planOver
    case None    => true

  protected def resetAgent(): Unit = _agent foreach: agent =>
    agent.resetPosition()
    agent.resetPlan()
    agent.resetPath()
  
  protected def stepAgent(): Unit = _agent foreach (_.step())
  
  protected def dropAgent(): Unit = _agent = None

  /**
   * Initiates planning for the current agent:
   * <li> starts the search
   * <li> handles the result in case of success or failure
   *
   * @throws AgentNotBuiltException if no agent is available
   */
  protected def searchPlan(): Unit =
    startSearch()
    _agent match 
      case Some(agent) =>
        try handleValidPlan(agent.searchPlan)
        catch
          case e: Exception => handleNoPlan(e.getMessage)
          //case PlanNotFoundException(error) => handleNoPlan(error)
      case None => throw AgentNotBuiltException
