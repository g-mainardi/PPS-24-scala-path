package it.unibo.controller

import it.unibo.model.Scenario
import it.unibo.view.View

trait SimulationController:
  def startSimulation(): Unit
  def stopSimulation(): Unit
  def resetScenario(): Unit
  def resetPath():Unit
  val scenario: Scenario
  
  View.top.visible = true
