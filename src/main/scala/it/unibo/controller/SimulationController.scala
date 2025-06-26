package it.unibo.controller

import it.unibo.model.{DummyPlanner, DummyScenario, Planner, Scenario}
import it.unibo.model.Tiling.Tile
import it.unibo.view.View

trait SimulationController:
  def startSimulation(): Unit
  def pauseSimulation(): Unit
  def resumeSimulation(): Unit
  def resetSimulation(): Unit
  def resetScenario(): Unit
  def simulationStep(): Unit
  def generateScenario(): Unit
  def initSimulation(): Unit = scenario.generateScenario()
  def attachView(view: View): Unit
  def changeScenario(scenario: Scenario): Unit
  var scenario: Scenario = DummyScenario()
  protected var view: Option[View] = None
  protected var planner: Planner = DummyPlanner()

object SimulationControllerImpl extends SimulationController:
  override def attachView(view: View): Unit =
    this.view = Some(view)

  override def changeScenario(scenario: Scenario): Unit =
    this.scenario = scenario

  override def startSimulation(): Unit =
    planner.plan

  override def pauseSimulation(): Unit = ()

  override def resumeSimulation(): Unit = ()
  override def generateScenario(): Unit = ()

  override def resetSimulation(): Unit =
    scenario.resetAgent()
  override def simulationStep(): Unit = ()
  override def resetScenario(): Unit =
    scenario.generateScenario()
    view foreach(_.repaint())
    resetSimulation()

