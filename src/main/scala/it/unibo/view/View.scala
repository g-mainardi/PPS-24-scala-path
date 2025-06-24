package it.unibo.view

import it.unibo.controller.SimulationController
import it.unibo.model.*

import scala.swing.SimpleSwingApplication
import java.awt.Color

object View extends SimpleSwingApplication:
  val controller: SimulationController = ???
  
  def tileColor(tile: Tile): Color = tile match
    case _:Floor => Color.WHITE
    case _: Grass => Color.GREEN
    case _: Teleport => Color.PINK
    case _: Arrow => Color.YELLOW
    case _: Wall => Color.GRAY
    case _: Trap => Color.RED
    case _: Water => Color.CYAN
    case _: Lava => Color.ORANGE



    