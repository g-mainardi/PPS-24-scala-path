package it.unibo.view

import it.unibo.controller.Simulation
import it.unibo.controller.Simulation.UICommand
import it.unibo.model.fundamentals.Direction
import it.unibo.model.fundamentals.Direction.Diagonals.*
import it.unibo.model.fundamentals.Direction.Cardinals.*
import it.unibo.view.ViewUtilities.{SelectionButton, getArrowIconFromDirection}

import scala.swing.{Dimension, GridPanel}

object ViewComponents:
  class DirectionGrid() extends GridPanel(3, 3):
    private val directions: List[Option[Direction]] = List(
      Some(LeftUp), Some(Up), Some(RightUp),
      Some(Left), None, Some(Right),
      Some(LeftDown), Some(Down), Some(RightDown)
    )
    private var selectedDirections: Seq[Direction] = Direction.allDirections
    private val buttonSize = new Dimension(40, 40)
    contents ++= directions.map { dirOpt =>
      val onDeselection: () => Unit = dirOpt.map { d =>
        () =>
          selectedDirections = selectedDirections :+ d
          Simulation set UICommand.DirectionsChoice(selectedDirections)
          println("called onDeselection, selectedDirections: " + selectedDirections)
      }.getOrElse(() => {})

      val onSelection: () => Unit = dirOpt.map { d =>
        () =>
          selectedDirections = selectedDirections.filterNot(_ == d)
          Simulation set UICommand.DirectionsChoice(selectedDirections)
          println("called onSelection, selectedDirections: " + selectedDirections)
      }.getOrElse(() => {})

      val btn = new SelectionButton(
        onState1 = onSelection,
        onState2 = onDeselection
      ) {
        preferredSize = buttonSize
        minimumSize = buttonSize
        maximumSize = buttonSize
        enabled = dirOpt.isDefined
        if enabled then
          icon = getArrowIconFromDirection(dirOpt.get)
        selected = false
      }
      btn
    }