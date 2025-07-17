package it.unibo.model.scenario

import it.unibo.model.fundamentals.Tiling.Floor
import it.unibo.model.*
import it.unibo.model.fundamentals.{Position, Tile, Tiling}

case class SpecialKind(name: String, computeNewPos: Position => Position)

case class SpecialTile(pos: Position, kind: SpecialKind)(using bound: ScenarioDimensions) extends Tiling.Special:
  override val newPos: Position =
    val computed = kind.computeNewPos(pos)
    Position(computed.x % bound.nRows, computed.y % bound.nCols)

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

class Specials(nRows: Int, nCols: Int) extends EmptyScenario(nRows, nCols):
  private val tilesPerKind = 3
  private val special = new SpecialTileBuilder

  special tile "Teleport" does (_ => randomFreePosition.get)
  special tile "JumpDown" does (pos => Position(pos.x + 2, pos.y))
  special tile "StairsUp" does (pos => Position(pos.x - 2, pos.y))
//
//  _tiles = (for
//    x <- 0 until nCols
//    y <- 0 until nRows
//  yield Floor(Position(x, y))).toList

  override def generate(): Unit =
    super.generate()
    val specialPositions: Map[SpecialKind, Set[Position]] =
      SpecialTileRegistry.allKinds.map { kind =>
        kind -> randomFreePositions(tilesPerKind)
      }.toMap

    _tiles = _tiles.map:
      case Tile(pos) =>
        specialPositions.collectFirst {
          case (kind, positions) if positions.contains(pos) =>
            SpecialTile(pos, kind)
        }.getOrElse(Floor(pos))
      case other => other

@main def defineSpecialTiles(): Unit =
  val builder = new SpecialTileBuilder
  // builder tile "Teleport" does (_ => randomPosition)
  builder tile "JumpDown" does (pos => Position(pos.x + 2, pos.y))
  builder tile "StairsUp" does (pos => Position(pos.x - 2, pos.y))