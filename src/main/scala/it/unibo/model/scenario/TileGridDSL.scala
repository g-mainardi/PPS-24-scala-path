package it.unibo.model.scenario

import it.unibo.model.fundamentals.Tiling.*
import it.unibo.model.fundamentals.{Position, Tile}

import scala.language.{implicitConversions, postfixOps}
import it.unibo.model.scenario.{Scenario, SpecialTile, SpecialTileBuilder, SpecialTileRegistry, Specials}

class GridBuilder(expectedColumns: Option[Int]):
  var tiles: Seq[Tile] = Seq.empty
  private var currentRowIndex: Int = 0
  private var currentColumnIndex: Int = 0
  private var maxColumnIndex: Option[Int] = None

  def add(f: Position => Tile): Unit =
    tiles = tiles :+ f(Position(currentColumnIndex, currentRowIndex))
    expectedColumns match
      case Some(columns) if currentColumnIndex == (columns -1) => newRow()
      case _ => currentColumnIndex += 1

  def newRow(): Unit =
    if maxColumnIndex.isEmpty then
      maxColumnIndex = Some(currentColumnIndex)
    if currentColumnIndex != maxColumnIndex.get then
      throw new IllegalArgumentException(s"Rows must be of the same length: expected ${maxColumnIndex.get}, found ${currentColumnIndex} at row ${currentRowIndex}")
    currentRowIndex += 1
    currentColumnIndex = 0

  def build(): Seq[Tile] =
    expectedColumns match
      case Some(columns) if tiles.length % columns != 0 => throw new IllegalArgumentException(s"Rows must be of the same length: expected ${columns}}")
      case _ => ()
    tiles


object GridDSL:

  def grid(columns: Int = -1)(body: GridBuilder ?=> Unit): Seq[Tile] =
    given builder: GridBuilder = new GridBuilder(if columns!= -1 then Some(columns) else None)
    body
    builder.build()

  def F(using b: GridBuilder): GridBuilder =
    b.add(Floor.apply)
    b

  def G(using b: GridBuilder): GridBuilder =
    b.add(Grass.apply)
    b

  def W(using b: GridBuilder): GridBuilder =
    b.add(Wall.apply)
    b

  def T(using b: GridBuilder): GridBuilder =
    b.add(Trap.apply)
    b

  def L(using b: GridBuilder): GridBuilder =
    b.add(Lava.apply)
    b

  def R(using b: GridBuilder): GridBuilder =
    b.add(Rock.apply)
    b

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
    def ||(next: GridBuilder): GridBuilder =
      b.newRow()
      b
    def || : GridBuilder =
      b.newRow()
      b

    def |(next: GridBuilder): GridBuilder = b