package it.unibo.view

import it.unibo.controller.{DisplayableController, Simulation}
import it.unibo.model.Direction.Cardinals.*
import it.unibo.model.Direction.Diagonals.*
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

object ViewUtilities:
  import it.unibo.model.Tiling.*

  private val colorList: List[Color] = List(Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.LIGHT_GRAY)
  private val specialTileColors: Map[String, Color] = SpecialTileBuilder.allKinds.map(_.name).zip(colorList).toMap


  def tileColor(tile: Tile): Color =
    import java.awt.Color.*
    tile match
      case _: Floor => WHITE
      case _: Grass => GREEN
      case _: Wall => BLACK
      case _: Water => CYAN
      case _: Lava => ORANGE
      case _: Rock => GRAY
      case special: CustomSpecialTile => specialTileColors.getOrElse(special.kind.name, Color.PINK)

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

  class SelectionButton(label1: String = "", label2: String = "", onState1: => Unit = {}, onState2: => Unit = {}) extends Button(label1):
    private var state = true // true: label1, false: label2
    reactions += {
      case ButtonClicked(_) =>
        if state then
          onState1
          text = label2
          background = Color.LIGHT_GRAY
        else
          onState2
          text = label1
          background = Color.GREEN
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