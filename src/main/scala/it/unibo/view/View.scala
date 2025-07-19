package it.unibo.view

import it.unibo.controller.{DisplayableController, ScalaPathController, Simulation}
import Simulation.*

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


class View(controller: DisplayableController, gridOffset: Int, cellSize: Int)
  extends ControllableView(controller):
  import ViewUtilities.*
  import ViewPanels.*

  title = "Scala Path"
  preferredSize = new Dimension(800, 600)


  private val directionGrid = new FlowPanel {
    contents += new Label("Possible directions")
    contents += new DirectionGrid()
    preferredSize = new Dimension(40 * 3, 40 * 3)
    maximumSize = preferredSize
    minimumSize = preferredSize
  }

  private val gridPanel: Panel = new Panel:
    preferredSize = new Dimension(200, 100)
    listenTo(mouse.clicks)
    reactions += {
      case e: event.MouseClicked =>
        val x = (e.point.x - gridOffset) / cellSize
        val y = (e.point.y - gridOffset) / cellSize
        if x >= 0 && y >= 0 && x < controller.scenario.nCols && y < controller.scenario.nRows then
          if scenarioSettingsPanel.moveGoalRadio.selected then {
            Simulation set UICommand.SetPosition(Simulation.SettablePosition.Goal(x, y))
          } else if scenarioSettingsPanel.moveStartRadio.selected then
            Simulation set UICommand.SetPosition(Simulation.SettablePosition.Init(x, y))
    }
    override def paintComponent(g: Graphics2D): Unit =
      super.paintComponent(g)
      given Graphics2D = g
      var tiles = controller.scenario.tiles
      drawCells(cellSize, gridOffset, tiles)
      drawGrid(cellSize, gridOffset, tiles)
      drawCircle(controller.goal.x, controller.goal.y, Color.RED, cellSize, gridOffset)
      drawCircle(controller.init.x, controller.init.y, Color.BLUE, cellSize, gridOffset)
      controller.agent foreach: agent =>
        drawCircle(agent.x, agent.y, Color.YELLOW, cellSize, gridOffset)
        drawPath(agent.path, cellSize, gridOffset)
        controlPanel.remainingSteps.text = agent.remainingSteps.toString
  

  contents = new BorderPanel:
    import BorderPanel.Position
    layout(gridPanel) = Position.Center
    layout(controlPanel) = Position.South
    layout(scenarioSettingsPanel) = Position.North
    layout(directionGrid) = Position.West