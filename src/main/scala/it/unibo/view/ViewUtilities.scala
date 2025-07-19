package it.unibo.view

import it.unibo.ScalaPath.view.pack
import it.unibo.controller.{DisplayableController, Simulation}
import it.unibo.model.fundamentals.Direction.Cardinals.*
import it.unibo.model.fundamentals.Direction.Diagonals.*
import it.unibo.model.fundamentals.{Position, Tile}
import it.unibo.model.fundamentals.{Direction, Tiling}
import it.unibo.model.scenario.{SpecialKind, SpecialTile, SpecialTileBuilder, SpecialTileRegistry}
import java.awt.Dimension
import java.awt.Color
import java.awt.geom.{Ellipse2D, Rectangle2D}
import scala.swing.{Alignment, *}
import scala.swing.event.{ButtonClicked, Event, KeyEvent, KeyTyped, SelectionChanged}
import java.awt.Image
import java.awt.image.BufferedImage
import javax.swing.{ImageIcon, WindowConstants}
import javax.imageio.ImageIO
import scala.swing.Dialog.Result
import scala.swing.Swing.onEDT
import scala.swing.*
import javax.swing.WindowConstants
import scala.swing.Point

object ViewUtilities:
  import Tiling.*

  private val colorList: List[Color] = List(Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.LIGHT_GRAY)
  private val specialTileColors: Map[String, Color] = SpecialTileRegistry.allKinds.map(_.name).zip(colorList).toMap
  /**
   * Returns a color for the given tile.
   * @param tile the tile to get the color for
   * @return the color associated with the tile type
   */
  def tileColor(tile: Tile): Color =
    import java.awt.Color.*
    tile match
      case _: Floor => WHITE
      case _: Grass => GREEN
      case _: Wall => BLACK
      case _: Water => CYAN
      case _: Lava => ORANGE
      case _: Rock => GRAY
      case special: SpecialTile => specialTileColors.getOrElse(special.kind.name, Color.PINK)


  /**
   * Retrieves an arrow icon based on the specified direction.
   * @param direction the direction for which to get the arrow icon
   * @param diameter the diameter of the icon
   * @return an ImageIcon representing the arrow in the specified direction
   */
  def getArrowIconFromDirection(direction: Direction, diameter: Int = 14): ImageIcon =
    val path = "/icons/arrow.png"
    direction match
      case Up => scaledIcon(path, diameter, diameter)
      case RightUp => scaledIcon(path, diameter, diameter, 45)
      case Right => scaledIcon(path, diameter, diameter, 90)
      case RightDown => scaledIcon(path, diameter, diameter, 135)
      case Down => scaledIcon(path, diameter, diameter, 180)
      case LeftDown => scaledIcon(path, diameter, diameter, 225)
      case Left => scaledIcon(path, diameter, diameter, 270)
      case LeftUp => scaledIcon(path, diameter, diameter, 315)


  /**
   * Extends the ComboBox class to include a placeholder item.
   * @param placeholder a placeholder string to display when no item is selected
   * @param items a sequence of items to populate the ComboBox
   * @param onSelect a callback function that is called when an item is selected
   * @tparam A the type of items in the ComboBox
   */
  class ComboBoxWithPlaceholder[A](placeholder: String, items: Seq[A], onSelect: Int => Unit) extends ComboBox(Seq(placeholder) ++ items):
    selection.index = 0
    listenTo(selection)
    reactions += {
      case SelectionChanged(_) =>
        if selection.index == 0 && peer.getItemAt(0) == placeholder then
          ()
        else
          if selection.index > 0 && peer.getItemAt(0) == placeholder then
            val selected = selection.item
            selection.index -= 1
            peer.setModel(ComboBox.newConstantModel(items))
            selection.item = selected
          onSelect(selection.index)
    }

    /**
     * Resets the ComboBox to its initial state with the placeholder selected.
     */
    def reset(): Unit =
      deafTo(selection)
      peer.setModel(ComboBox.newConstantModel(Seq(placeholder) ++ items))
      selection.index = 0
      listenTo(selection)

  /**
   * A button that is initially disabled.
   * @param label the text to display on the button
   */
  class DefaultDisabledButton(label: String) extends Button(label):
    enabled = false


  /** A button that toggles between two states with different labels and actions.
   * @param label1 the text for the first state
   * @param label2 the text for the second state
   * @param onState1 action to perform when the button is in the first state
   * @param onState2 action to perform when the button is in the second state
   */
  class TwoStateButton(label1: String = "", label2: String = "", onState1: => Unit = {}, onState2: => Unit = {}) extends Button(label1):
    private var state = true // true: label1, false: label2
    reactions += {
      case ButtonClicked(_) =>
        if state then
          onState1
          text = label2
        else
          onState2
          text = label1
        state = !state
    }
    /**
     * Resets the button to its initial state with label1.
     */
    def reset(): Unit =
      state = true
      text = label1


  /** A text field that only allows integer input.
   * It listens to key events and consumes any non-digit input.
   */
  class IntegerTextField() extends TextField:
    listenTo(keys)
    reactions += {
      case e: KeyTyped =>
        val c = e.char
        if (!c.isDigit && c != '-' && c != '\b') then
          e.consume()
    }


  /**
   * A button that toggles between two states with different labels and actions.
   * This button is used for selection purposes, when the button is pressed it also changes its color.
   * @param label1 the text for the first state
   * @param label2 the text for the second state
   * @param onState1 action to perform when the button is in the first state
   * @param onState2 action to perform when the button is in the second state
   */
  class SelectionButton(label1: String = "", label2: String = "", onState1:() => Unit = () => {}, onState2: () => Unit = () => {}) extends Button(label1):
    private var state = true // true: label1, false: label2
    reactions += {
      case ButtonClicked(_) =>
        if state then
          onState1()
          text = label2
          background = Color.LIGHT_GRAY
        else
          onState2()
          text = label1
          background = null
        state = !state
    }


  /**
   * Creates a scaled icon from a resource path, with optional rotation.
   * @param path the resource path to the icon image
   * @param width the desired width of the icon
   * @param height the desired height of the icon
   * @param rotationDegrees the degrees to rotate the icon (default is 0)
   * @return a scaled and optionally rotated ImageIcon
   */
  def scaledIcon(path: String, width: Int, height: Int, rotationDegrees: Double = 0): ImageIcon =
    val url = getClass.getResource(path)
    val original = ImageIO.read(url)
    val scaled = original.getScaledInstance(width, height, Image.SCALE_SMOOTH)
    val buffered = new BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB)
    val g2d = buffered.createGraphics()
    val theta = Math.toRadians(rotationDegrees)
    g2d.rotate(theta, width / 2.0, height / 2.0)
    g2d.drawImage(scaled, 0, 0, null)
    g2d.dispose()
    new ImageIcon(buffered)


  /**
   * Shows a popup message dialog with the specified message, title, and type.
   * @param message the message to display in the dialog
   * @param title the title of the dialog
   * @param messageType the type of message (e.g., Dialog.Message.Info, Dialog.Message.Warning, etc.)
   */
  def showPopupMessage(message: String, title: String, messageType: Dialog.Message.Value): Unit =
    Dialog.showMessage(
      message = message,
      title = title,
      messageType = messageType
    )


  /**
   * Shows a loading dialog with a specified message.
   * The dialog is modal and prevents interaction with other windows until closed.
   * @param message the message to display in the loading dialog
   * @return the Dialog instance for further manipulation (e.g., closing it)
   */
  def showLoadingDialog(message: String = "Loading..."): Dialog =
    val dialog = new Dialog():
      modal = true
      peer.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
      contents = new BorderPanel:
        layout(new Label(message)) = BorderPanel.Position.Center
      preferredSize = new Dimension(300, 200)
    onEDT:
      dialog.open()
    dialog


  /**
   * Closes the specified loading dialog.
   * @param dialog the Dialog instance to close
   */
  def closeLoadingDialog(dialog: Dialog): Unit =
    onEDT:
      dialog.close()


  /**
   * Draws a path on the graphics context using a sequence of positions and directions.
   * @param positions a sequence of tuples containing positions and their corresponding directions
   * @param scale the scale factor to apply to the positions
   * @param offset an optional offset to apply to the positions (default is 0)
   * @param g the Graphics2D context to draw on
   */
  def drawPath(positions: Seq[(Position, Direction)],  scale: Int, offset: Int = 0)(using g: Graphics2D): Unit =
    positions.foreach((p, d) =>
      val px = p.x * scale + offset
      val py = p.y * scale + offset
      g.drawImage(getArrowIconFromDirection(d, scale).getImage, px, py, null))

  /**
   * Draws a circle at the specified position with a given color and diameter.
   * @param x the x-coordinate of the circle's center
   * @param y the y-coordinate of the circle's center
   * @param color the color of the circle
   * @param diameter the diameter of the circle
   * @param offset an optional offset to apply to the circle's position (default is 0)
   * @param g the Graphics2D context to draw on
   */
  def drawCircle(x: Int, y: Int, color: Color, diameter: Int, offset: Int = 0)(using g: Graphics2D): Unit =
    g setColor color
    val entity = new Ellipse2D.Double(x * diameter + offset,
      y * diameter + offset,
      diameter, diameter
    )
    g fill entity


  /**
   * Draws a grid of cells with a specified cell size and offset.
   * @param cellSize the size of each cell in the grid
   * @param offset the offset to apply to the grid position
   * @param tiles the sequence of tiles to draw
   * @param g the Graphics2D context to draw on
   */
  def drawCells(cellSize: Int, offset: Int = 0, tiles: Seq[Tile])(using g: Graphics2D): Unit =
    def makeCell(x: Int, y: Int): Rectangle2D =
      new Rectangle2D.Double(x * cellSize + offset, y * cellSize + offset, cellSize, cellSize)

    tiles foreach: t =>
      g setColor tileColor(t)
      g fill makeCell(t.x, t.y)


  /**
   * Draws a gray grid to separate the tiles visually.
   * @param cellSize the size of each cell in the grid
   * @param offset the offset to apply to the grid position
   * @param tiles the sequence of tiles to draw the grid for
   * @param g the Graphics2D context to draw on
   */
  def drawGrid(cellSize: Int, offset: Int = 0, tiles: Seq[Tile])(using g: Graphics2D): Unit =
    for t <- tiles do
      val rect = new Rectangle2D.Double(
        t.x * cellSize + offset,
        t.y * cellSize + offset,
        cellSize, cellSize
      )
      g setColor Color(147, 153, 149)
      g draw rect