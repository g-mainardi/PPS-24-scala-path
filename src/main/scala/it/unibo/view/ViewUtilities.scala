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

  class ComboBoxWithPlaceholder[A](placeholder: String, items: Seq[A], onSelect: Int => Unit) extends ComboBox(Seq(placeholder) ++ items):
    selection.index = 0
    listenTo(selection)
    reactions += {
      case SelectionChanged(_) =>
        if selection.index > 0 && peer.getItemAt(0) == placeholder then
          val selected = selection.item
          selection.index = selection.index - 1
          peer.setModel(ComboBox.newConstantModel(items))
          selection.item = selected
        onSelect(selection.index)
    }
    def reset(): Unit =
      peer.setModel(ComboBox.newConstantModel(Seq(placeholder) ++ items))
      selection.index = 0

  class DefaultDisabledButton(label: String) extends Button(label):
    enabled = false

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
    def reset(): Unit =
      state = true
      text = label1

  class IntegerTextField() extends TextField:
    listenTo(keys)
    reactions += {
      case e: KeyTyped =>
        val c = e.char
        if (!c.isDigit && c != '-' && c != '\b') then
          e.consume()
    }

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

  def showPopupMessage(message: String, title: String, messageType: Dialog.Message.Value): Unit =
    Dialog.showMessage(
      message = message,
      title = title,
      messageType = messageType
    )

//  def showLoadingDialog(message: String, title: String): Unit =
//    Dialog.showMessage(
//      message = message,
//      title = title,
//      modal = true
//    )

  def showLoadingDialog(message: String = "Loading..."): Dialog =
    val dialog = new Dialog():
      modal = true
      peer.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
      contents = new BorderPanel:
        layout(new Label(message)) = BorderPanel.Position.Center
      preferredSize = new Dimension(200, 80)
    dialog.open()
    dialog

  def closeLoadingDialog(dialog: Dialog): Unit =
    dialog.close()