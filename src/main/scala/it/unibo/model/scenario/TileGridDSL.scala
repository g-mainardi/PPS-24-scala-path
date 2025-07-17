package it.unibo.model.scenario

import it.unibo.model.fundamentals.Tiling.{Floor, Grass, Wall}
import it.unibo.model.fundamentals.{Position, Tile}

import scala.language.postfixOps


object TileSymbol:
  sealed trait Symbol
  case object F extends Symbol
  case object G extends Symbol
  case object W extends Symbol

import TileSymbol.*
class GridBuilder:
  private var currentRow: List[Symbol] = List.empty
  private var rows: List[List[Symbol]] = List.empty

  def |(sym: Symbol): GridBuilder =
    currentRow = currentRow :+ sym
    this

  def || : GridBuilder =
    if rows.nonEmpty && currentRow.length != rows.head.length then
      throw new IllegalArgumentException(s"Rows must be of the same length ${rows.head.length}, found ${currentRow.length}")
    rows = rows :+ currentRow
    currentRow = List.empty
    this

  def build(): List[Tile] =
    val mapping: Map[Symbol, Position => Tile] = Map(
      F -> (Floor.apply),
      G -> (Grass.apply),
      W -> (Wall.apply)
    )
    rows.zipWithIndex.toList.flatMap { case (line, y) =>
      line.zipWithIndex.map { case (sym, x) =>
        mapping.getOrElse(sym, throw new IllegalArgumentException(s"Non existing symbol: '$sym'"))
          .apply(Position(x, y))
      }
    }

object GridDSL:
  def start: GridBuilder = new GridBuilder



import GridDSL.*
import TileSymbol.*

@main def testDSL(): Unit =
  val tiles = start
    .|(F).|(F).|(F).||
    .|(G).|(G).|(G).||
    .|(W).|(W).|(W).||
    .build()

  tiles.foreach(t => println(s"${t.getClass.getSimpleName} at (${t.x}, ${t.y})"))