package it.unibo.view

import it.unibo.controller.{DisplayableController, Simulation}
import it.unibo.model.{SpecialTile, Direction, SpecialKind, SpecialTileBuilder, Tiling}
import it.unibo.model.Tiling.Position

import java.awt.Color
import java.awt.geom.{Ellipse2D, Rectangle2D}
import scala.swing.*
import scala.swing.event.{ButtonClicked, Event, MouseEvent, SelectionChanged}
import java.awt.Image
import java.awt.image.BufferedImage
import javax.swing.ImageIcon
import javax.imageio.ImageIO
import it.unibo.model.Direction.Cardinals.*
import it.unibo.model.Direction.Diagonals.*
import java.awt.event.MouseAdapter


class View(controller: DisplayableController) extends MainFrame:
  import ViewUtilities.*
  import it.unibo.ScalaPath.{gridOffset, cellSize, gridSize}
  title = "Scala Path"
  preferredSize = new Dimension(800, 600)
  private val algorithmDropdown = new ComboBoxWithPlaceholder("Select algorithm", controller.algorithmsNames, Simulation set Simulation.ChangeAlgorithm(_))
  private val scenarioDropdown = new ComboBoxWithPlaceholder("Select scenario...", controller.scenariosNames, Simulation set Simulation.ChangeScenario(_))

  class DirectionGrid() extends GridPanel(3, 3):
    private val directions: List[Option[Direction]] = List(
      Some(LeftUp), Some(Up), Some(RightUp),
      Some(Left), None, Some(Right),
      Some(LeftDown), Some(Down), Some(RightDown)
    )
    var selectedDirections: List[Direction] = Direction.allDirections
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
  private val searchWithLabel = new Label("Search with: ")


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


  def enableStepButton(): Unit = stepButton.enabled = true
  def disableStepButton(): Unit = stepButton.enabled = false
  def enableResetButton(): Unit = resetButton.enabled = true
  def disableResetButton(): Unit = resetButton.enabled = false
  def enableStartStopButton(): Unit = startStopButton.enabled = true
  def disableStartStopButton(): Unit = startStopButton.enabled = false
  def enableGenerateScenarioButton(): Unit = refreshScenarioButton.enabled = true
  def disableGenerateScenarioButton(): Unit = refreshScenarioButton.enabled = false
  def resetAlgorithmDropdown(): Unit = algorithmDropdown.reset()


  def showInfoMessage(message: String, title: String): Unit =
    showPopupMessage(message, title, Dialog.Message.Info)

  def showErrorMessage(message: String, title: String): Unit =
    showPopupMessage(message, title, Dialog.Message.Error)

  private val moveGoalRadio = new RadioButton("Change goal") { selected = true }
  private val moveStartRadio = new RadioButton("Change start")
  private val moveGroup = new ButtonGroup(moveGoalRadio, moveStartRadio)

  private val movePanel = new FlowPanel(moveGoalRadio, moveStartRadio)

  private val gridPanel: Panel = new Panel:
    preferredSize = new Dimension(200, 100)
    listenTo(mouse.clicks)
    reactions += {
      case e: event.MouseClicked =>
        val x = (e.point.x - gridOffset) / cellSize
        val y = (e.point.y - gridOffset) / cellSize
        if x >= 0 && y >= 0 && x < gridSize && y < gridSize then
          if moveGoalRadio.selected then {
            Simulation set Simulation.SetPosition(Simulation.SettablePosition.Goal(x, y))
          } else if moveStartRadio.selected then
            Simulation set Simulation.SetPosition(Simulation.SettablePosition.Init(x, y))
    }
    override def paintComponent(g: Graphics2D): Unit =
      super.paintComponent(g)
      given Graphics2D = g
      drawCells(cellSize, gridOffset)
      drawGrid(gridSize, cellSize, gridOffset)
      drawCircle(controller.goal.x, controller.goal.y, Color.RED)
      drawCircle(controller.init.x, controller.init.y, Color.BLUE)
      controller.agent foreach: agent =>
        drawCircle(agent.x, agent.y, Color.YELLOW)
        drawPath(agent.path)

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

    private def drawGrid(size: Int, cellSize: Int, offset: Int)(using g: Graphics2D): Unit =
      for t <- controller.scenario.tiles do
        val rect = new Rectangle2D.Double(
          t.x * cellSize + offset,
          t.y * cellSize + offset,
          cellSize, cellSize
        )
        g setColor Color(147, 153, 149)
        g draw rect


  // private object ControlPanel extends FlowPanel(startButton, stepButton, resetButton, startStopButton, scenarioDropdown, algorithmDropdown, refreshScenarioButton)
  private object ControlPanel extends FlowPanel(startStopButton, stepButton, resetButton)
  private object ScenarioSettingsPanel extends FlowPanel(searchWithLabel, scenarioDropdown, refreshScenarioButton, movePanel, algorithmDropdown)

  contents = new BorderPanel:
    import BorderPanel.Position
    layout(gridPanel) = Position.Center
    layout(ControlPanel) = Position.South
    layout(ScenarioSettingsPanel) = Position.North
    layout(directionGrid) = Position.West

  listenTo(stepButton, resetButton, scenarioDropdown.selection, algorithmDropdown.selection, refreshScenarioButton)
  reactions += {
    case ButtonClicked(`stepButton`) => Simulation set Simulation.Step
    case ButtonClicked(`resetButton`) => Simulation set Simulation.Empty
    case ButtonClicked(`refreshScenarioButton`) => Simulation set Simulation.ChangeScenario(scenarioDropdown.selection.index)
  }
    