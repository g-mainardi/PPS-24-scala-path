package it.unibo

import it.unibo.controller.SimulationController
import it.unibo.model.DummyScenario
import it.unibo.view.View

import scala.swing.{Frame, SimpleSwingApplication}

object ScalaPath extends SimpleSwingApplication:
    val controller: SimulationController = ???
    val cellSize = 20
    val gridSize = 9
    val gridOffset = 50

    override def top: Frame = View(DummyScenario(), controller)
