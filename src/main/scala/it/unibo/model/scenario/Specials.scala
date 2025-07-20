package it.unibo.model.scenario

import it.unibo.model.fundamentals.Tiling.Floor
import it.unibo.model.*
import it.unibo.model.fundamentals.{Position, Tile, Tiling}
import Scenario.Dimensions

/**
 * A programmatically defined kind of special tile.
 * @param name          the name of the tile kind (e.g., "Teleport", "JumpDown")
 * @param computeNewPos a function that computes the new position when the tile is activated
 */
case class SpecialKind(name: String, computeNewPos: Position => Position)

/**
 * A tile with custom movement logic defined by its kind.
 * The resulting position is wrapped to stay within the scenario bounds.
 *
 * @param pos  the current position of the tile
 * @param kind the behavior-defining kind of this special tile
 */
case class SpecialTile(pos: Position, kind: SpecialKind)(using bound: Dimensions) extends Tiling.Special:
  override val newPos: Position =
    val computed = kind.computeNewPos(pos)
    Position(computed.x % bound.nRows, computed.y % bound.nCols)

/**
 * Global registry for all declared special tile kinds.
 */
object SpecialTileRegistry:
  var registry: Map[String, SpecialKind] = Map()
  def allKinds: Iterable[SpecialKind] = registry.values
  def clear(): Unit = registry = Map.empty

/**
 * DSL-style builder for defining new special tile kinds.
 *
 * Usage: builder tile "Teleport" does (pos => ...)
 */
class SpecialTileBuilder:
  private var name: String = ""

  def tile(name: String): SpecialTileBuilder =
    this.name = name
    this

  def does(compute: Position => Position): Unit =
    SpecialTileRegistry.registry += name -> SpecialKind(name, compute)

/**
 * A scenario that includes random special tiles with predefined behaviors.
 *
 * @param nRows number of rows in the scenario
 * @param nCols number of columns in the scenario
 */
class Specials(nRows: Int, nCols: Int) extends EmptyScenario(nRows, nCols):
  private val special = new SpecialTileBuilder
  special tile "Teleport" does (_ => randomFreePosition.get)
  special tile "JumpDown" does (pos => Position(pos.x, pos.y - 2))
  special tile "StairsUp" does (pos => Position(pos.x, pos.y + 2))
  //private val tilesPerKind = math.min (nRows, nCols) / SpecialTileRegistry.allKinds.size
  private val tilesPerKind = 3

  override def generate(): Unit =
    super.generate()
    val specialPositions: Map[SpecialKind, Set[Position]] =
      SpecialTileRegistry.allKinds.map { kind =>
        kind -> randomFreePositions(tilesPerKind).toSet
      }.toMap

    _tiles = _tiles.map:
      case Tile(pos) =>
        specialPositions.collectFirst {
          case (kind, positions) if positions.contains(pos) =>
            SpecialTile(pos, kind)
        }.getOrElse(Floor(pos))
      case other => other