package it.unibo.view

import it.unibo.controller.SimulationController
import it.unibo.model.{Scenario, Tiling}

import java.awt.geom.Rectangle2D
import scala.swing.event.ButtonClicked
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
  
class View(scene: Scenario, controller: SimulationController) extends MainFrame:
  import ViewUtilities.*
  import it.unibo.ScalaPath.{gridOffset, cellSize, gridSize}
  title = "Demo Dimensioni"
  preferredSize = new Dimension(800, 600)
      
  private val planButton = new Button("Plan")
  private val stepButton = new Button("Step")

  private val gridPanel: Panel = new Panel:
    preferredSize = new Dimension(200, 100)
    override def paintComponent(g: Graphics2D): Unit =
      super.paintComponent(g)
      given Graphics2D = g
      drawGrid(gridSize, cellSize, gridOffset)
      drawCells(cellSize, gridOffset)
    private def drawCells(size: Int, gridOffset: Int)(using g: Graphics2D): Unit =
      def makeCell(x: Int, y: Int): Rectangle2D =
        new Rectangle2D.Double(y * size + gridOffset, x * size + gridOffset, size, size)
      scene.generateScenario() foreach : t =>
        g setColor tileColor(t)
        g fill makeCell(t.x, t.y)
    private def drawGrid(size: Int, cellSize: Int, offset: Int)(using g: Graphics2D): Unit =
      List(0, size) foreach : i =>
        val gOffset = offset - 1
        val pos: Int = i * cellSize + gOffset
        g drawLine(gOffset, pos, size * cellSize + gOffset, pos)
        g drawLine(pos, gOffset, pos, size * cellSize + gOffset)

  private object ControlPanel extends FlowPanel(planButton, stepButton)

  contents = new BorderPanel:
    import BorderPanel.Position
    layout(gridPanel) = Position.Center
    layout(ControlPanel) = Position.South

  listenTo(planButton, stepButton)
  reactions += {
    case ButtonClicked(`planButton`) => println("Planning...")
    case ButtonClicked(`stepButton`) => println("Stepping...")
  }
    