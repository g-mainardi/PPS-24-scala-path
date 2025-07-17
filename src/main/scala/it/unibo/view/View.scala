package it.unibo.view

import it.unibo.controller.{DisplayableController, ScalaPathController, Simulation}

import java.awt.Color
import java.awt.geom.{Ellipse2D, Rectangle2D}
import scala.swing.*
import scala.swing.event.{ButtonClicked, Event, MouseEvent, SelectionChanged, ValueChanged}
import java.awt.Image
import java.awt.image.BufferedImage
import javax.swing.ImageIcon
import javax.imageio.ImageIO
import it.unibo.model.fundamentals.Direction.Cardinals.*
import it.unibo.model.fundamentals.Direction.Diagonals.*
import it.unibo.model.fundamentals.{Direction, Position, Tiling}
import it.unibo.model.scenario.{SpecialKind, SpecialTile, SpecialTileBuilder}

import java.awt.event.MouseAdapter


class View(controller: DisplayableController, gridOffset: Int, cellSize: Int) extends MainFrame:
  import ViewUtilities.*
  title = "Scala Path"
  preferredSize = new Dimension(800, 600)
  private val algorithmDropdown = new ComboBoxWithPlaceholder("Select algorithm", controller.algorithmsNames, Simulation set Simulation.ChangeAlgorithm(_)){enabled = false}
  private val scenarioDropdown = new ComboBoxWithPlaceholder("Select scenario...", controller.scenariosNames, Simulation set Simulation.ChangeScenario(_))

  private class DirectionGrid() extends GridPanel(3, 3):
    private val directions: List[Option[Direction]] = List(
      Some(LeftUp), Some(Up), Some(RightUp),
      Some(Left), None, Some(Right),
      Some(LeftDown), Some(Down), Some(RightDown)
    )
    private var selectedDirections: List[Direction] = Direction.allDirections
    private val buttonSize = new Dimension(40, 40)
    contents ++= directions.map { dirOpt =>
      val onDeselection: () => Unit = dirOpt.map { d => () =>
        selectedDirections = selectedDirections :+ d
        Simulation set Simulation.DirectionsChoice(selectedDirections)
        println("called onDeselection, selectedDirections: " + selectedDirections)
      }.getOrElse(() => {})

      val onSelection: () => Unit = dirOpt.map { d => () =>
        selectedDirections = selectedDirections.filterNot(_ == d)
        Simulation set Simulation.DirectionsChoice(selectedDirections)
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

  private val refreshScenarioButton = new Button(){
    icon = scaledIcon("/icons/refreshIcon.png", 14, 14)
    borderPainted = false
    contentAreaFilled = false
    focusPainted = false
  }
  private val resetButton = new DefaultDisabledButton("Reset")
  private val stepButton = new DefaultDisabledButton("Step")
  private val remainingSteps = new Label()
  private val colsInput = new IntegerTextField() {columns = 2}
  private val rowsInput = new IntegerTextField() {columns = 2}
  private var loadingDialog: Option[Dialog] = None

  private val directionGrid = new FlowPanel {
    contents += new DirectionGrid()
    preferredSize = new Dimension(40*3, 40*3)
    maximumSize = preferredSize
    minimumSize = preferredSize
  }
  private val startStopButton = new TwoStateButton(
    "Start",
    "Stop",
    Simulation set Simulation.Running,
    Simulation set Simulation.Paused(fromUser = true)
  ){enabled = false}

  private val speedSlider = new Slider {
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

  def enableStepButton(): Unit = stepButton.enabled = true
  def disableStepButton(): Unit = stepButton.enabled = false
  def enableResetButton(): Unit = resetButton.enabled = true
  def disableResetButton(): Unit = resetButton.enabled = false
  def enableStartStopButton(): Unit = startStopButton.enabled = true
  def disableStartStopButton(): Unit = startStopButton.enabled = false
  def enableGenerateScenarioButton(): Unit = refreshScenarioButton.enabled = true
  def disableGenerateScenarioButton(): Unit = refreshScenarioButton.enabled = false
  def enableAlgorithmDropdown(): Unit = algorithmDropdown.enabled = true
  def resetAlgorithmDropdown(): Unit = algorithmDropdown.reset()
  def resetStartStopButton(): Unit = startStopButton.reset()

  def showInfoMessage(message: String, title: String): Unit =
    showPopupMessage(message, title, Dialog.Message.Info)

  def showErrorMessage(message: String, title: String): Unit =
    showPopupMessage(message, title, Dialog.Message.Error)

  def showLoadingDialog(message: String): Unit =
    loadingDialog = Some(ViewUtilities.showLoadingDialog(this, message))

  def closeLoadingDialog(): Unit
    ViewUtilities.closeLoadingDialog(loadingDialog.get)

  private val moveStartRadio = new RadioButton("Start")
  private val moveGoalRadio = new RadioButton("Goal") { selected = true }

  private val moveGroup = new ButtonGroup(moveStartRadio, moveGoalRadio)

  private val movePanel = new FlowPanel(moveStartRadio, moveGoalRadio)

  private val gridPanel: Panel = new Panel:
    preferredSize = new Dimension(200, 100)
    listenTo(mouse.clicks)
    reactions += {
      case e: event.MouseClicked =>
        val x = (e.point.x - gridOffset) / cellSize
        val y = (e.point.y - gridOffset) / cellSize
        if x >= 0 && y >= 0 && x < controller.scenario.nCols && y < controller.scenario.nRows then
          if moveGoalRadio.selected then {
            Simulation set Simulation.SetPosition(Simulation.SettablePosition.Goal(x, y))
          } else if moveStartRadio.selected then
            Simulation set Simulation.SetPosition(Simulation.SettablePosition.Init(x, y))
    }
    override def paintComponent(g: Graphics2D): Unit =
      super.paintComponent(g)
      given Graphics2D = g
      drawCells(cellSize, gridOffset)
      drawGrid(cellSize, gridOffset)
      drawCircle(controller.goal.x, controller.goal.y, Color.RED)
      drawCircle(controller.init.x, controller.init.y, Color.BLUE)
      controller.agent foreach: agent =>
        drawCircle(agent.x, agent.y, Color.YELLOW)
        drawPath(agent.path)
        remainingSteps.text = agent.remainingSteps.toString

    private def drawPath(positions: List[(Position, Direction)])(using g: Graphics2D): Unit =
      positions.foreach((p, d) =>
        val px = p.x * cellSize + gridOffset
        val py = p.y * cellSize + gridOffset
        g.drawImage(getArrowIconFromDirection(d, cellSize).getImage, px, py, null))
    
    private def drawCircle(x: Int, y: Int, color: Color)(using g: Graphics2D): Unit =
      g setColor color
      val entity = new Ellipse2D.Double(x * cellSize + gridOffset,
        y * cellSize + gridOffset,
        cellSize, cellSize
      )
      g fill entity

    private def drawCells(size: Int, gridOffset: Int)(using g: Graphics2D): Unit =
      def makeCell(x: Int, y: Int): Rectangle2D =
        new Rectangle2D.Double(x * size + gridOffset, y * size + gridOffset, size, size)
      controller.scenario.tiles foreach : t =>
        g setColor tileColor(t)
        g fill makeCell(t.x, t.y)

    private def drawGrid(cellSize: Int, offset: Int)(using g: Graphics2D): Unit =
      for t <- controller.scenario.tiles do
        val rect = new Rectangle2D.Double(
          t.x * cellSize + offset,
          t.y * cellSize + offset,
          cellSize, cellSize
        )
        g setColor Color(147, 153, 149)
        g draw rect


  // private object ControlPanel extends FlowPanel(startButton, stepButton, resetButton, startStopButton, scenarioDropdown, algorithmDropdown, refreshScenarioButton)
  private object ControlPanel extends FlowPanel(startStopButton, stepButton,
    new Label("Remaining steps: "), remainingSteps, resetButton,
    new Label("Animation speed: "), speedSlider)
  private object ScenarioSettingsPanel extends FlowPanel(new Label("Dimensions: "), colsInput, new Label("x"), rowsInput,
    new Label("Search with: "), scenarioDropdown, refreshScenarioButton,
    new Label("Change: "), movePanel, algorithmDropdown)

  contents = new BorderPanel:
    import BorderPanel.Position
    layout(gridPanel) = Position.Center
    layout(ControlPanel) = Position.South
    layout(ScenarioSettingsPanel) = Position.North
    layout(directionGrid) = Position.West

  listenTo(stepButton, resetButton, scenarioDropdown.selection, algorithmDropdown.selection, refreshScenarioButton, speedSlider, colsInput, rowsInput)
  reactions += {
    case ButtonClicked(`stepButton`) => Simulation set Simulation.Step
    case ButtonClicked(`resetButton`) => Simulation set Simulation.Empty
    case ButtonClicked(`refreshScenarioButton`) => Simulation set Simulation.ChangeScenario(scenarioDropdown.selection.index)
    case ValueChanged(`speedSlider`) =>
      val speed = speedSlider.value / 10.0
      Simulation set Simulation.SetAnimationSpeed(1 / speed)
    case event.EditDone(`colsInput`) => onDimensionChange()
    case event.EditDone(`rowsInput`) => onDimensionChange()
  }

  private def onDimensionChange(): Unit =
    val maybeRows = rowsInput.text.toIntOption
    val maybeCols = colsInput.text.toIntOption

    (maybeRows, maybeCols) match
      case (Some(rows), Some(cols)) if rows > 0 && cols > 0 =>
        Simulation set Simulation.SetScenarioSize(rows, cols)
        println(s"passing ($rows $cols)")
      case _ => None
