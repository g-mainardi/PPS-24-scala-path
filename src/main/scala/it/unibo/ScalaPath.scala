package it.unibo

import it.unibo.controller.{SimulationController, SimulationControllerImpl}
import it.unibo.model.DummyScenario
import it.unibo.view.View

import scala.swing.{Frame, SimpleSwingApplication}

object ScalaPath extends SimpleSwingApplication:
    // val controller: SimulationController = SimulationControllerImpl()

    val view: View = View(SimulationControllerImpl)

    SimulationControllerImpl.attachView(view)
    SimulationControllerImpl.initSimulation()

    val cellSize = 20
    val gridSize = 9
    val gridOffset = 50

    override def top: Frame = view