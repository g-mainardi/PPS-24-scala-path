package it.unibo.model.scenario

import it.unibo.model.fundamentals.Tiling.{Floor, Grass, Wall}
import it.unibo.model.fundamentals.{Position, Tile}

import scala.annotation.targetName
import scala.collection.immutable.BitSet.empty
import scala.language.postfixOps


object SimpleGridDSL:

  case class Builder(rows: Vector[String] = Vector.empty):
    private val mapping: Map[Char, Position => Tile] = Map(
      'F' -> (Floor.apply),
      'G' -> (Grass.apply),
      'W' -> (Wall.apply)
    )

    def row(s: String): Builder =
      if rows.nonEmpty && s.length != rows.head.length then
        throw new IllegalArgumentException(s"Tutte le righe devono avere la stessa lunghezza; attesa ${rows.head.length}, trovata ${s.length}")
      else this.copy(rows = rows :+ s)

    def rows(lines: String*): Builder =
      lines.foldLeft(this)((b, line) => b.row(line))

    def build(): List[Tile] =
      rows.zipWithIndex.toList.flatMap { case (line, y) =>
        line.zipWithIndex.map { case (ch, x) =>
          mapping.getOrElse(ch, throw new IllegalArgumentException(s"Carattere non mappato: '$ch'"))
            .apply(Position(x, y))
        }
      }

  object Builder:
    def start: Builder = Builder()


import it.unibo.model.scenario.SimpleGridDSL.Builder
import it.unibo.model.scenario.SimpleGridDSL.*
import it.unibo.model.scenario.*

@main def testDSL(): Unit =
  val tiles = Builder.start.rows(
      "FFF",
      "GGG",
      "WWW"
    ).build()
  tiles.foreach(t => println(s"${t.getClass.getSimpleName} at (${t.x}, ${t.y})"))