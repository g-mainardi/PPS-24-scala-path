package it.unibo.view

import it.unibo.controller.SimulationController
import it.unibo.model.{Scenario, Tiling}

import java.awt.Color
import java.awt.geom.{Ellipse2D, Rectangle2D}
import scala.swing.event.{ButtonClicked, SelectionChanged}

import scala.swing.*

object ViewUtilities:
  import Tiling.*
  def tileColor(tile: Tile): Color =
    import java.awt.Color.*
    tile match
      case _: Floor => WHITE
      case _: Grass => GREEN
      case _: Teleport => PINK
      case _: Arrow => YELLOW
      case _: Wall => BLACK
      case _: Trap => RED
      case _: Water => CYAN
      case _: Lava => ORANGE
      case _: Rock => GRAY
  
class View(controller: SimulationController) extends MainFrame:
  import ViewUtilities.*
  import it.unibo.ScalaPath.{gridOffset, cellSize, gridSize}
  title = "Scala Path"
  preferredSize = new Dimension(800, 600)
      
  private val startButton = new Button("Start")
  private val resetButton = new Button("Reset")
  private val stepButton = new Button("Step")
  private val pauseResumeButton = new Button("Pause")
  private val generateScenarioButton = new Button("Generate scenario")
  private val scenarios = Seq("Maze", "Map", "Trap")
  private val scenarioDropdown = new ComboBox(scenarios)


  private val gridPanel: Panel = new Panel:
    preferredSize = new Dimension(200, 100)
    override def paintComponent(g: Graphics2D): Unit =
      super.paintComponent(g)
      given Graphics2D = g
      drawGrid(gridSize, cellSize, gridOffset)
      drawCells(cellSize, gridOffset)
      drawAgent(gridOffset)

    private def drawAgent(gridOffset: Int)(using g: Graphics2D): Unit =
      val agent = controller.scenario.agent
      g setColor Color.BLUE
      val agentRect = new Ellipse2D.Double(
        agent.y * cellSize + gridOffset,
        agent.x * cellSize + gridOffset,
        cellSize, cellSize
      )
      g fill agentRect

    private def drawCells(size: Int, gridOffset: Int)(using g: Graphics2D): Unit =
      def makeCell(x: Int, y: Int): Rectangle2D =
        new Rectangle2D.Double(y * size + gridOffset, x * size + gridOffset, size, size)
      print(controller.scenario.tiles)
      controller.scenario.tiles foreach : t =>
        g setColor tileColor(t)
        g fill makeCell(t.x, t.y)

    private def drawGrid(size: Int, cellSize: Int, offset: Int)(using g: Graphics2D): Unit =
      List(0, size) foreach : i =>
        val gOffset = offset - 1
        val pos: Int = i * cellSize + gOffset
        g drawLine(gOffset, pos, size * cellSize + gOffset, pos)
        g drawLine(pos, gOffset, pos, size * cellSize + gOffset)


  private object ControlPanel extends FlowPanel(startButton, stepButton, resetButton, pauseResumeButton, scenarioDropdown, generateScenarioButton)

  contents = new BorderPanel:
    import BorderPanel.Position
    layout(gridPanel) = Position.Center
    layout(ControlPanel) = Position.North

  listenTo(startButton, stepButton, resetButton, pauseResumeButton, scenarioDropdown, generateScenarioButton)
  reactions += {
    case ButtonClicked(`startButton`) => controller.startSimulation()
    case ButtonClicked(`stepButton`) => controller.simulationStep()
    case ButtonClicked(`resetButton`) => controller.resetSimulation()
    case ButtonClicked(`generateScenarioButton`) => controller.generateScenario()
    case ButtonClicked(`pauseResumeButton`) =>
      if pauseResumeButton.text == "Pause" then
        controller.pauseSimulation()
        pauseResumeButton.text = "Resume"
      else
        controller.resumeSimulation()
        pauseResumeButton.text = "Pause"
    case SelectionChanged(`scenarioDropdown`) =>
      println(s"Selected scenario: ${scenarioDropdown.selection.item}")
      controller.changeScenario(null)
  }
    