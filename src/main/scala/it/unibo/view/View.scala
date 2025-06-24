package it.unibo.view

import it.unibo.controller.SimulationController
import it.unibo.model.Tiling.*

import scala.swing.{Color, Frame, SimpleSwingApplication}

object View extends SimpleSwingApplication:
  val controller: SimulationController = ???
  val cellSize = 20
  
  def tileColor(tile: Tile): Color =
    import java.awt.Color.*
    tile match
      case _: Floor    => WHITE
      case _: Grass    => GREEN
      case _: Teleport => PINK
      case _: Arrow    => YELLOW
      case _: Wall     => GRAY
      case _: Trap     => RED
      case _: Water    => CYAN
      case _: Lava     => ORANGE

  override def top: Frame = ???



    