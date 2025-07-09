package it.unibo.model

import it.unibo.model.Scenario.{nCols, nRows}
import it.unibo.model.Tiling.{Floor, Position, TilePos}

case class SpecialTile(name: String, computeNewPos: Position => Position)

case class CustomSpecialTile(pos: Position, kind: SpecialTile) extends Tiling.Special:
  override val newPos: Position = kind.computeNewPos(pos)

object SpecialTileBuilder:
  private var registry: Map[String, SpecialTile] = Map()
  def define(name: String)(compute: Position => Position): Unit =
    registry += name -> SpecialTile(name, compute)

  def allKinds: Iterable[SpecialTile] = registry.values
  def clear(): Unit = registry = Map.empty

class ScenarioWithSpecials extends Scenario:
  val tilesPerKind = 4

  override def generate(): Unit =
    val baseTiles = (for
      x <- 0 until nRows
      y <- 0 until nCols
    yield Floor(Position(x, y))).toList

    val specialPositions: Map[SpecialTile, Set[Position]] =
      SpecialTileBuilder.allKinds.map { kind =>
        kind -> Scenario.randomPositions(tilesPerKind)
      }.toMap

    _tiles = baseTiles.map:
      case TilePos(pos) =>
        specialPositions.collectFirst {
          case (kind, positions) if positions.contains(pos) =>
            CustomSpecialTile(pos, kind)
        }.getOrElse(Floor(pos))

@main def defineSpecialTiles(): Unit =
  import SpecialTileBuilder.*
  define("Teleport")(_ => Scenario.randomPosition)
  define("JumpDown")(pos => Position(pos.x + 2, pos.y))
  define("StairsUp")(pos => Position(pos.x - 2, pos.y))

