package it.unibo.model

import it.unibo.model.Scenario.{nCols, nRows}
import it.unibo.model.Tiling.{Floor, Position, Tile}

case class SpecialKind(name: String, computeNewPos: Position => Position)

case class SpecialTile(pos: Position, kind: SpecialKind) extends Tiling.Special:
  override val newPos: Position =
    val computed = kind.computeNewPos(pos)
    Position(computed.x % nRows, computed.y % nCols)

object SpecialTileRegistry:
  var registry: Map[String, SpecialKind] = Map()
  def allKinds: Iterable[SpecialKind] = registry.values
  def clear(): Unit = registry = Map.empty

class SpecialTileBuilder:
  private var name: String = ""
  
  def tile(name: String): SpecialTileBuilder =
    this.name = name
    this

  def does(compute: Position => Position): Unit =
    SpecialTileRegistry.registry += name -> SpecialKind(name, compute)

class Specials(nRows: Int, nCols: Int) extends Scenario(nRows, nCols):
  private val tilesPerKind = 4

  override def generate(): Unit =
    val baseTiles = (for
      x <- 0 until nRows
      y <- 0 until nCols
    yield Floor(Position(x, y))).toList

    val specialPositions: Map[SpecialKind, Set[Position]] =
      SpecialTileRegistry.allKinds.map { kind =>
        kind -> Scenario.randomPositions(tilesPerKind)
      }.toMap

    _tiles = baseTiles.map:
      case Tile(pos) =>
        specialPositions.collectFirst {
          case (kind, positions) if positions.contains(pos) =>
            SpecialTile(pos, kind)
        }.getOrElse(Floor(pos))
      case other => other

@main def defineSpecialTiles(): Unit =
  val builder = new SpecialTileBuilder
  builder tile "Teleport" does (_ => Scenario.randomPosition)
  builder tile "JumpDown" does (pos => Position(pos.x + 2, pos.y))
  builder tile "StairsUp" does (pos => Position(pos.x - 2, pos.y))