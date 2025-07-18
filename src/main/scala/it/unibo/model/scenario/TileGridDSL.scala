package it.unibo.model.scenario

import scala.language.postfixOps
import it.unibo.model.fundamentals.Tiling.*
import it.unibo.model.fundamentals.{Position, Tile}
import scala.language.implicitConversions

// --- DSL symbols ---
object TileSymbol:
  sealed trait Symbol
  case object W extends Symbol
  case object T extends Symbol
  case object F extends Symbol
  case object G extends Symbol
  case object L extends Symbol
  case object R extends Symbol
  case class TP(to: Position) extends Symbol



import TileSymbol.*
class GridBuilder:
  private var currentRow: List[Symbol] = List.empty
  private var rows: List[List[Symbol]] = List.empty

  def add(sym: Symbol): Unit =
    currentRow = currentRow :+ sym

  def newRow(): Unit =
    if rows.nonEmpty && currentRow.length != rows.head.length then
      throw new IllegalArgumentException(s"Rows must be of the same length: expected ${rows.head.length}, found ${currentRow.length}")
    rows = rows :+ currentRow
    currentRow = List.empty

  def build(): List[Tile] =
    val mapping: Map[Symbol, Position => Tile] = Map(
      W -> Wall.apply,
      T -> Trap.apply,
      F -> Floor.apply,
      G -> Grass.apply,
      L -> Lava.apply,
      R -> Rock.apply
    )
    rows.zipWithIndex.flatMap { case (line, y) =>
      line.zipWithIndex.map { case (sym, x) =>
        sym match
          case TP(to) => Teleport(Position(x, y), to)
          case _ => mapping.getOrElse(sym, throw new IllegalArgumentException(s"Unknown symbol: $sym"))(Position(x, y))
      }
    }


object GridDSL:
  def grid(body: GridBuilder ?=> Unit): List[Tile] =
    given builder: GridBuilder = new GridBuilder()
    body
    builder.build()

  extension (sym: Symbol)(using b: GridBuilder)
    infix def |(next: Symbol) : GridBuilder =
      b.add(sym)
      b.add(next)
      b

    def || : GridBuilder =
      b.newRow()
      b

  extension (b: GridBuilder)
    infix def |(next: Symbol): GridBuilder =
      b.add(next)
      b

    def || : GridBuilder =
      b.newRow()
      b

import GridDSL.*
import TileSymbol.*

@main def testDSL(): Unit =
  val tiles = grid:
    (F | F | F). ||
    (G | G | G). ||
    (W | W | W). ||

  tiles.foreach(t => println(s"${t.getClass.getSimpleName} at (${t.x}, ${t.y})"))