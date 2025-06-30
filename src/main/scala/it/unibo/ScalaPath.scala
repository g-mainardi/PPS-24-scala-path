package it.unibo

import it.unibo.controller.SimulationControllerImpl
import it.unibo.view.View

import scala.swing.{Frame, SimpleSwingApplication}

object ScalaPath extends SimpleSwingApplication:
  val view: View = View(SimulationControllerImpl)

  SimulationControllerImpl attachView view

  val cellSize = 20
  val gridSize = 10
  val gridOffset = 50

  override def top: Frame = view

  override def main(args: Array[String]): Unit =
    super.main(args)
    SimulationControllerImpl.initSimulation()
