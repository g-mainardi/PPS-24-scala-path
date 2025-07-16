package it.unibo

import it.unibo.controller.ScalaPathController
import it.unibo.view.View

import scala.swing.{Frame, SimpleSwingApplication}

object ScalaPath extends SimpleSwingApplication:
  val view: View = View(ScalaPathController, gridOffset = 50, cellSize = 20)
  ScalaPathController attachView view

  override def top: Frame = view
  override def main(args: Array[String]): Unit =
    super.main(args)
    ScalaPathController.start()
