package it.unibo.view

import it.unibo.controller.{DisplayableController, Simulation}
import it.unibo.model.{CustomSpecialTile, Direction, SpecialTile, SpecialTileBuilder, Tiling}
import it.unibo.model.Tiling.Position

import java.awt.Color
import java.awt.geom.{Ellipse2D, Rectangle2D}
import scala.swing.*
import scala.swing.event.{ButtonClicked, Event, SelectionChanged}
import java.awt.Image
import java.awt.image.BufferedImage
import javax.swing.ImageIcon
import javax.imageio.ImageIO
import it.unibo.model.Direction.Cardinals.*
import it.unibo.model.Direction.Diagonals.*


class View(controller: DisplayableController) extends MainFrame:
  import ViewUtilities.*
  import it.unibo.ScalaPath.{gridOffset, cellSize, gridSize}
  title = "Scala Path"
  preferredSize = new Dimension(800, 600)
  private val algorithmDropdown = new ComboBoxWithPlaceholder("Select algorithm", controller.algorithmsNames, Simulation set Simulation.ChangeAlgorithm(_))
  private val scenarioDropdown = new ComboBoxWithPlaceholder("Select scenario...", controller.scenariosNames, Simulation set Simulation.ChangeScenario(_))

  class DirectionGrid(onDirectionSelected: Option[it.unibo.model.Direction] => Unit) extends GridPanel(3, 3):
    private val directions: Vector[Option[Direction]] = Vector(
      Some(LeftUp), Some(Up), Some(RightUp),
      Some(Left), None, Some(Right),
      Some(LeftDown), Some(Down), Some(RightDown)
    )
    private val buttonSize = new Dimension(40, 40)
    contents ++= directions.zipWithIndex.map { case (dirOpt, idx) =>
      val btn = new Button() {
        preferredSize = buttonSize
        minimumSize = buttonSize
        maximumSize = buttonSize
        enabled = dirOpt.isDefined
        if enabled then
          icon = getArrowIconFromDirection(dirOpt.get)
      }
      listenTo(btn)
      reactions += {
        case ButtonClicked(`btn`) => onDirectionSelected(dirOpt)
      }
      btn
    }

  private val refreshScenarioButton = new Button(){
    icon = scaledIcon("/icons/refreshIcon.png", 14, 14)
    borderPainted = false
    contentAreaFilled = false
    focusPainted = false
  }
  private val startButton = new DefaultDisabledButton("Start")
  private val resetButton = new DefaultDisabledButton("Reset")
  private val stepButton = new DefaultDisabledButton("Step")
  private val searchWithLabel = new Label("Search with: ")


  private val directionGrid = new FlowPanel {
    contents += new DirectionGrid(_ => println("hello"))
    preferredSize = new Dimension(40*3, 40*3) // adatta alle tue esigenze
    maximumSize = preferredSize
    minimumSize = preferredSize
  }
  private val pauseResumeButton = new TwoStateButton(
    "Pause",
    "Resume",
    Simulation set Simulation.Paused(fromUser = true),
    Simulation set Simulation.Running
  ){enabled = false}

  def enableStartButton(): Unit = startButton.enabled = true
  def disableStartButton(): Unit = startButton.enabled = false
  def enableStepButton(): Unit = stepButton.enabled = true
  def disableStepButton(): Unit = stepButton.enabled = false
  def enableResetButton(): Unit = resetButton.enabled = true
  def disableResetButton(): Unit = resetButton.enabled = false
  def enablePauseResumeButton(): Unit = pauseResumeButton.enabled = true
  def disablePauseResumeButton(): Unit = pauseResumeButton.enabled = false
  def enableGenerateScenarioButton(): Unit = refreshScenarioButton.enabled = true
  def disableGenerateScenarioButton(): Unit = refreshScenarioButton.enabled = false

  def showInfoMessage(message: String, title: String): Unit =
    showPopupMessage(message, title, Dialog.Message.Info)

  def showErrorMessage(message: String, title: String): Unit =
    showPopupMessage(message, title, Dialog.Message.Error)

  private val gridPanel: Panel = new Panel:
    preferredSize = new Dimension(200, 100)
    override def paintComponent(g: Graphics2D): Unit =
      super.paintComponent(g)
      given Graphics2D = g
      drawCells(cellSize, gridOffset)
      drawGrid(gridSize, cellSize, gridOffset)
      drawCircle(controller.scenario.goalPosition.x, controller.scenario.goalPosition.y, Color.RED)
      drawCircle(controller.scenario.agent.x, controller.scenario.agent.y, Color.BLUE)
      drawPath(controller.path)
    

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
      //println(controller.scenario.tiles)
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


  // private object ControlPanel extends FlowPanel(startButton, stepButton, resetButton, pauseResumeButton, scenarioDropdown, algorithmDropdown, refreshScenarioButton)
  private object ControlPanel extends FlowPanel(startButton, stepButton, resetButton, pauseResumeButton)
  private object ScenarioSettingsPanel extends FlowPanel(searchWithLabel, scenarioDropdown, refreshScenarioButton, algorithmDropdown)

  contents = new BorderPanel:
    import BorderPanel.Position
    layout(gridPanel) = Position.Center
    layout(ControlPanel) = Position.South
    layout(ScenarioSettingsPanel) = Position.North
    layout(directionGrid) = Position.East

  listenTo(startButton, stepButton, resetButton, pauseResumeButton, scenarioDropdown.selection, algorithmDropdown.selection, refreshScenarioButton)
  reactions += {
    case ButtonClicked(`startButton`) => Simulation set Simulation.Running
    case ButtonClicked(`stepButton`) => Simulation set Simulation.Step
    case ButtonClicked(`resetButton`) => Simulation set Simulation.Empty
    case ButtonClicked(`refreshScenarioButton`) => Simulation set Simulation.ChangeScenario(scenarioDropdown.selection.index)
  }
    