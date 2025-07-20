package it.unibo.view

import it.unibo.controller.{DisplayableController, Simulation}
import it.unibo.controller.Simulation.{ExecutionState, UICommand}
import it.unibo.view.ViewUtilities.{ComboBoxWithPlaceholder, DefaultDisabledButton, IntegerTextField, TwoStateButton, scaledIcon}

import scala.swing.event.{ButtonClicked, EditDone, ValueChanged}
import scala.swing.{Button, ButtonGroup, Component, Dimension, FlowPanel, GridPanel, Label, Orientation, RadioButton, Slider}

import it.unibo.controller.Simulation
import it.unibo.controller.Simulation.UICommand
import it.unibo.model.fundamentals.Direction
import it.unibo.model.fundamentals.Direction.Diagonals.*
import it.unibo.model.fundamentals.Direction.Cardinals.*
import it.unibo.view.ViewUtilities.{SelectionButton, getArrowIconFromDirection}

object ViewPanels:

  class ControlPanel() extends FlowPanel:
    val startStopButton: TwoStateButton = new TwoStateButton(
      "Start",
      "Stop",
      Simulation set ExecutionState.Running,
      Simulation set ExecutionState.Paused(fromUser = true)
    ) {
      enabled = false
    }
    val stepButton = new DefaultDisabledButton("Step")
    val remainingSteps = new Label()
    val resetButton = new DefaultDisabledButton("Reset")

    val speedSlider: Slider = new Slider {
      min = 1
      max = 31
      value = 10
      majorTickSpacing = 10
      minorTickSpacing = 1
      paintTicks = true
      paintLabels = true
      orientation = Orientation.Horizontal
      preferredSize = new Dimension(200, 40)
      labels = (min to max by majorTickSpacing).map(i => i -> new Label(f"${i / 10.0}%.1fx")).toMap
      tooltip = "Animation speed (0.1x - 3.1x)"
    }

    contents ++= Seq(
      startStopButton, stepButton,
      new Label("Remaining steps: "), remainingSteps,
      resetButton,
      new Label("Animation speed: "), speedSlider
    )
    listenTo(stepButton, resetButton, speedSlider)
    reactions += {
      case ButtonClicked(`stepButton`) => Simulation set ExecutionState.Step
      case ButtonClicked(`resetButton`) => Simulation set ExecutionState.Empty
      case ValueChanged(`speedSlider`) =>
        val speed = speedSlider.value / 10.0
        Simulation set UICommand.SetAnimationSpeed(1 / speed)
    }


  class ScenarioSettingsPanel(controller: DisplayableController) extends FlowPanel:
    val colsInput: IntegerTextField = new IntegerTextField() {
      columns = 2
      text = controller.scenario.nCols.toString
    }
    val rowsInput: IntegerTextField = new IntegerTextField() {
      columns = 2
      text = controller.scenario.nRows.toString
    }
    val algorithmDropdown: ComboBoxWithPlaceholder[String] = new ComboBoxWithPlaceholder(
      "Select algorithm",
      controller.algorithmsNames,
      Simulation set UICommand.ChangeAlgorithm(_)
    ) { enabled = false }
    val scenarioDropdown = new ComboBoxWithPlaceholder(
      "Select scenario...",
      controller.scenariosNames,
      Simulation set UICommand.ChangeScenario(_)
    )
    val refreshScenarioButton: Button = new Button() {
      icon = scaledIcon("/icons/refreshIcon.png", 14, 14)
      borderPainted = false
      contentAreaFilled = false
      focusPainted = false
      enabled = false
    }
    val moveStartRadio = new RadioButton("Start")
    val moveGoalRadio = new RadioButton("Goal") { selected = true }
    val moveGroup = new ButtonGroup(moveStartRadio, moveGoalRadio)
    val movePanel = new FlowPanel(moveStartRadio, moveGoalRadio)

    contents ++= Seq(
      new Label("Dimensions: "), colsInput, new Label("x"), rowsInput,
      new Label("Search with: "), scenarioDropdown, refreshScenarioButton,
      new Label("Change: "), movePanel, algorithmDropdown
    )
    listenTo(refreshScenarioButton, colsInput, rowsInput)
    reactions += {
      case ButtonClicked(`refreshScenarioButton`) => Simulation set UICommand.ChangeScenario(scenarioDropdown.selection.index)
      case EditDone(`colsInput`) => onDimensionChange()
      case EditDone(`rowsInput`) => onDimensionChange()
    }

    private def onDimensionChange(): Unit =
      val maybeRows = rowsInput.text.toIntOption
      val maybeCols = colsInput.text.toIntOption

      (maybeRows, maybeCols) match
        case (Some(rows), Some(cols)) if rows > 0 && cols > 0 =>
          Simulation set UICommand.SetScenarioSize(rows, cols)
        case _ => None

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
      }.getOrElse(() => {})

      val onSelection: () => Unit = dirOpt.map { d =>
        () =>
          selectedDirections = selectedDirections.filterNot(_ == d)
          Simulation set UICommand.DirectionsChoice(selectedDirections)
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