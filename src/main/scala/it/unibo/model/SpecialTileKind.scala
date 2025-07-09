package it.unibo.model

import it.unibo.model.Scenario.{nCols, nRows}
import it.unibo.model.Tiling.{Floor, Position, TilePos}

case class SpecialTileKind(name: String, computeNewPos: Position => Position)

case class CustomSpecialTile(pos: Position, kind: SpecialTileKind) extends Tiling.Special:
  override val newPos: Position = kind.computeNewPos(pos)

object SpecialTileDSL:
  private var registry: Map[String, SpecialTileKind] = Map()
  def define(name: String)(compute: Position => Position): Unit =
    registry += name -> SpecialTileKind(name, compute)

  def allKinds: Iterable[SpecialTileKind] = registry.values
  def clear(): Unit = registry = Map.empty

class DSLDrivenScenario extends Scenario:
  val tilesPerKind = 4

  override def generate(): Unit =
    val baseTiles = (for
      x <- 0 until nRows
      y <- 0 until nCols
    yield Floor(Position(x, y))).toList

    val specialPositions: Map[SpecialTileKind, Set[Position]] =
      SpecialTileDSL.allKinds.map { kind =>
        kind -> Scenario.randomPositions(tilesPerKind)
      }.toMap

    _tiles = baseTiles.map:
      case TilePos(pos) =>
        specialPositions.collectFirst {
          case (kind, positions) if positions.contains(pos) =>
            CustomSpecialTile(pos, kind)
        }.getOrElse(Floor(pos))

@main def defineSpecialTiles(): Unit =
  import SpecialTileDSL.*
  define("Teleport")(_ => Scenario.randomPosition)
  define("JumpDown")(pos => Position(pos.x + 2, pos.y))
  define("StairsUp")(pos => Position(pos.x - 2, pos.y))

