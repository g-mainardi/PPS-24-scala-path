package it.unibo.model

import it.unibo.model.Tiling.{Floor, Position, Tile}

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

class Specials(nRows: Int, nCols: Int) extends Scenario(nRows, nCols):
  private val tilesPerKind = 3

  val special = new SpecialTileBuilder
  special tile "Teleport" does (_ => randomPosition.get)
  special tile "JumpDown" does (pos => Position(pos.x + 2, pos.y))
  special tile "StairsUp" does (pos => Position(pos.x - 2, pos.y))

  private val baseTiles = (for
    x <- 0 until nRows
    y <- 0 until nCols
  yield Floor(Position(x, y))).toList

  override def generate(): Unit =
    val specialPositions: Map[SpecialKind, Set[Position]] =
      SpecialTileRegistry.allKinds.map { kind =>
        kind -> randomPositions(tilesPerKind)
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
  // builder tile "Teleport" does (_ => randomPosition)
  builder tile "JumpDown" does (pos => Position(pos.x + 2, pos.y))
  builder tile "StairsUp" does (pos => Position(pos.x - 2, pos.y))