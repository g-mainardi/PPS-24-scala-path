package it.unibo.model.scenario

import it.unibo.model.fundamentals.Tiling.*
import it.unibo.model.fundamentals.{Position, Tile}

import scala.language.{implicitConversions, postfixOps}
import it.unibo.model.scenario.{Scenario, SpecialTile, SpecialTileBuilder, SpecialTileRegistry, Specials}

/**
 * A builder for creating a grid of tiles with optional constraints on the number of columns.
 * It allows adding tiles in a row-wise manner and ensures that all rows have the same length.
 *
 * @param expectedColumns Optional expected number of columns in the grid.
 */
class GridBuilder(expectedColumns: Option[Int]):
  private var tiles: Seq[Tile] = Seq.empty
  private var currentRowIndex: Int = 0
  private var currentColumnIndex: Int = 0
  private var maxColumnIndex: Option[Int] = None

  /**
   * Adds a tile to the grid at the current position.
   * If the current row is full, it starts a new row.
   *
   * @param f A function that takes the current position and returns a Tile.
   */
  def add(f: Position => Tile): Unit =
    tiles = tiles :+ f(Position(currentColumnIndex, currentRowIndex))
    expectedColumns match
      case Some(columns) if currentColumnIndex == (columns -1) => newRow()
      case _ => currentColumnIndex += 1

  /**
   * Starts a new row in the grid.
   */
  def newRow(): Unit =
    if maxColumnIndex.isEmpty then
      maxColumnIndex = Some(currentColumnIndex)
    if currentColumnIndex != maxColumnIndex.get then
      throw new IllegalArgumentException(s"Rows must be of the same length: expected ${maxColumnIndex.get}, found ${currentColumnIndex} at row ${currentRowIndex}")
    currentRowIndex += 1
    currentColumnIndex = 0

  /**
   * Builds the grid and returns the sequence of tiles.
   * @return A sequence of tiles representing the grid.
   */
  def build(): Seq[Tile] =
    expectedColumns match
      case Some(columns) if tiles.length % columns != 0 => throw new IllegalArgumentException(s"Rows must be of the same length: expected ${columns}}")
      case _ => ()
    tiles

/**
 * A DSL for constructing a grid of tiles using a builder pattern.
 * It allows defining the grid layout in a concise manner.
 */
object GridDSL:

  /**
   * Creates a grid with an optional specified number of columns.
   * usage:
   *  {{{
   * grid(3):
   *  F | F | F
   *  G | G | G
   *  W | W | W
   *  }}}
   * or
   * {{{
   * grid():
   *  F | F | F ||;
   *  G | G | G ||;
   *  W | W | W ||;
   * }}}
   * @param columns Optional number of columns in the grid.
   * @param body The body of the grid definition.
   * @return A sequence of tiles representing the grid.
   */
  def grid(columns: Int = -1)(body: GridBuilder ?=> Unit): Seq[Tile] =
    given builder: GridBuilder = new GridBuilder(if columns!= -1 then Some(columns) else None)
    body
    builder.build()

  /**
   * Adds a Floor tile to the grid.
   * @param b the GridBuilder instance to which the tile will be added.
   * @return the GridBuilder instance for method chaining.
   */
  def F(using b: GridBuilder): GridBuilder =
    b.add(Floor.apply)
    b

  /**
   * Adds a Grass tile to the grid.
   * @param b the GridBuilder instance to which the tile will be added.
   * @return the GridBuilder instance for method chaining.
   */
  def G(using b: GridBuilder): GridBuilder =
    b.add(Grass.apply)
    b

  /**
   * Adds a Wall tile to the grid.
   * @param b the GridBuilder instance to which the tile will be added.
   * @return the GridBuilder instance for method chaining.
   */
  def W(using b: GridBuilder): GridBuilder =
    b.add(Wall.apply)
    b

  /**
   * Adds a Trap tile to the grid.
   * @param b the GridBuilder instance to which the tile will be added.
   * @return the GridBuilder instance for method chaining.
   */
  def T(using b: GridBuilder): GridBuilder =
    b.add(Trap.apply)
    b

  /**
   * Adds a Lava tile to the grid.
   * @param b the GridBuilder instance to which the tile will be added.
   * @return the GridBuilder instance for method chaining.
   */
  def L(using b: GridBuilder): GridBuilder =
    b.add(Lava.apply)
    b

  /**
   * Adds a Rock tile to the grid.
   * @param b the GridBuilder instance to which the tile will be added.
   * @return the GridBuilder instance for method chaining.
   */
  def R(using b: GridBuilder): GridBuilder =
    b.add(Rock.apply)
    b

  /**
   * Adds a Teleport tile to the grid that teleports to a specified position.
   * @param to the position to which the teleport tile will teleport.
   * @param b the GridBuilder instance to which the tile will be added.
   * @return the GridBuilder instance for method chaining.
   */
  def TP(to: Position)(using b: GridBuilder): GridBuilder =
    b.add(pos => {
      val special = new SpecialTileBuilder
      special tile "TestTeleport" does (_ => to)
      val kind = SpecialTileRegistry.allKinds.find(_.name == "TestTeleport").get
      given Scenario.Dimensions = Scenario.Dimensions(to.x + 1, to.y + 1)
      SpecialTile(pos, kind)
    })
    b

  extension (b: GridBuilder)
    /**
     * Adds a new row to the grid.
     * This method can be used to separate rows in the grid definition.
     * @param next the next GridBuilder instance (not used, just for chaining).
     * @return the GridBuilder instance for method chaining.
     */
    def ||(next: GridBuilder): GridBuilder =
      b.newRow()
      b

    /**
     * Adds a new row to the grid.
     * This method can be used to separate rows in the grid definition.
     * @return the GridBuilder instance for method chaining.
     */
    def || : GridBuilder =
      b.newRow()
      b

    /**
     * Combines two GridBuilders into one.
     * This allows chaining multiple grid definitions together.
     * @param next the next GridBuilder instance to combine with.
     * @return the current GridBuilder instance for method chaining.
     */
    def |(next: GridBuilder): GridBuilder = b