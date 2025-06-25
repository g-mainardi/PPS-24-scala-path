package it.unibo.controller

import it.unibo.model.{DummyPlanner, DummyScenario, Planner, Scenario}
import it.unibo.model.Tiling.Tile
import it.unibo.view.View

trait SimulationController:
  def startSimulation(): Unit
  def stopSimulation(): Unit
  def resetScenario(): Unit
  def resetPath():Unit
  def initSimulation(): Unit = scenario.generateScenario()
  def attachView(view: View): Unit
  def changeScenario(scenario: Scenario): Unit
  var scenario: Scenario = DummyScenario()
  protected var view: Option[View] = None
  protected var planner: Planner = DummyPlanner()

class SimulationControllerImpl extends SimulationController:
  def attachView(view: View): Unit =
    this.view = Some(view)

  def changeScenario(scenario: Scenario): Unit =
    this.scenario = scenario

  def startSimulation(): Unit =
    planner.plan()

  override def stopSimulation(): Unit = ()

  override def resetScenario(): Unit =
    scenario.generateScenario()
    view foreach(_.repaint())
    resetPath()

  override def resetPath(): Unit = ()