package it.unibo.view

import it.unibo.controller.{DisplayableController, Simulation}
import it.unibo.model.Tiling
import it.unibo.model.Tiling.Position

import java.awt.Color
import java.awt.geom.{Ellipse2D, Rectangle2D}
import scala.swing.*
import scala.swing.event.{ButtonClicked, Event, SelectionChanged}

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
      case _  => YELLOW

class View(controller: DisplayableController) extends MainFrame:
  import ViewUtilities.*
  import it.unibo.ScalaPath.{gridOffset, cellSize, gridSize}
  title = "Scala Path"
  preferredSize = new Dimension(800, 600)

  private class ComboBoxWithPlaceholder[A](placeholder: String, items: Seq[A]) extends ComboBox(Seq(placeholder) ++ items):
    selection.index = 0
    listenTo(selection)
    reactions += {
      case SelectionChanged(_) =>
        if selection.index > 0 && peer.getItemAt(0) == placeholder then {
          val selected = selection.item
          peer.setModel(ComboBox.newConstantModel(items))
          selection.index = selection.index - 1
          selection.item = selected
        }
    }


  private val scenarioDropdown = new ComboBoxWithPlaceholder("Select scenario...", controller.scenariosNames)
  private val algorithmDropdown = new ComboBoxWithPlaceholder("Select algorithm", controller.algorithmsNames)



  private class DefaultDisabledButton(label: String) extends Button(label):
    enabled = false

  private val generateScenarioButton = new DefaultDisabledButton("Generate scenario")
  private val startButton = new DefaultDisabledButton("Start")
  private val resetButton = new DefaultDisabledButton("Reset")
  private val stepButton = new DefaultDisabledButton("Step")
  private val pauseResumeButton = new DefaultDisabledButton("Pause")

  def enableStartButton(): Unit = startButton.enabled = true
  def disableStartButton(): Unit = startButton.enabled = false
  def enableStepButton(): Unit = stepButton.enabled = true
  def disableStepButton(): Unit = stepButton.enabled = false
  def enableResetButton(): Unit = resetButton.enabled = true
  def disableResetButton(): Unit = resetButton.enabled = false
  def enablePauseResumeButton(): Unit = pauseResumeButton.enabled = true
  def disablePauseResumeButton(): Unit = pauseResumeButton.enabled = false
  def enableGenerateScenarioButton(): Unit = generateScenarioButton.enabled = true
  def disableGenerateScenarioButton(): Unit = generateScenarioButton.enabled = false

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
    

    private def drawPath(positions: List[Position])(using g: Graphics2D): Unit =
      positions.foreach(p => drawCircle(p.x, p.y, Color.BLUE))
    
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


  // private object ControlPanel extends FlowPanel(startButton, stepButton, resetButton, pauseResumeButton, scenarioDropdown, algorithmDropdown, generateScenarioButton)
  private object ControlPanel extends FlowPanel(startButton, stepButton, resetButton, pauseResumeButton)
  private object ScenarioSettingsPanel extends FlowPanel(scenarioDropdown, algorithmDropdown, generateScenarioButton)

  contents = new BorderPanel:
    import BorderPanel.Position
    layout(gridPanel) = Position.Center
    layout(ControlPanel) = Position.South
    layout(ScenarioSettingsPanel) = Position.North

  listenTo(startButton, stepButton, resetButton, pauseResumeButton, scenarioDropdown.selection, algorithmDropdown.selection, generateScenarioButton)
  reactions += {
    case ButtonClicked(`startButton`) => Simulation set Simulation.Running
    case ButtonClicked(`stepButton`) => Simulation set Simulation.Step
    case ButtonClicked(`resetButton`) => Simulation set Simulation.Reset
    case ButtonClicked(`generateScenarioButton`) => Simulation set Simulation.ChangeScenario(scenarioDropdown.selection.index)
    case ButtonClicked(`pauseResumeButton`) =>
      if pauseResumeButton.text == "Pause" then
        Simulation set Simulation.Paused
        pauseResumeButton.text = "Resume"
      else
        Simulation set Simulation.Running
        pauseResumeButton.text = "Pause"
    case SelectionChanged(`scenarioDropdown`) => println(s"Selected scenario: ${scenarioDropdown.selection.item}")
    case SelectionChanged(`algorithmDropdown`) => Simulation set Simulation.ChangeAlgorithm(algorithmDropdown.selection.index)
  }
    