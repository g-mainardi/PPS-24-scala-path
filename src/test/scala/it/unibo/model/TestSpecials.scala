package it.unibo.model

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import it.unibo.model.Tiling.Position

class TestSpecials extends AnyFlatSpec with Matchers:
  val builder = new SpecialTileBuilder

  behavior of "SpecialTileBuilder and SpecialTile"
  it should "register special kinds correctly in the registry" in {
    SpecialTileRegistry.clear()
    builder tile "JumpRight" does (pos => Position(pos.x, pos.y + 2))
    val kinds = SpecialTileRegistry.allKinds.toList
    kinds.map(_.name) should contain only "JumpRight"
  }

  it should "compute new position and wrap correctly inside bounds" in {
    SpecialTileRegistry.clear()
    builder tile "JumpFar" does (pos => Position(pos.x + 20, pos.y + 20))
    val kind = SpecialTileRegistry.allKinds.find(_.name == "JumpFar").get
    val tile = SpecialTile(Position(5, 5), kind)
    val newPos = tile.newPos
    newPos.x should be >= 0
    newPos.x should be < Scenario.nRows
    newPos.y should be >= 0
    newPos.y should be < Scenario.nCols
  }

  it should "generate a scenario with special tiles at random positions" in {
    SpecialTileRegistry.clear()
    builder tile "JumpDown" does (pos => Position(pos.x + 1, pos.y))
    builder tile "JumpUp" does (pos => Position(pos.x - 1, pos.y))
    val scenario = new Specials
    scenario.generate()
    val specialTiles = scenario.tiles.collect { case s: SpecialTile => s }
    specialTiles.size should be > 0
    all(specialTiles.map(_.kind.name)) should (be("JumpDown") or be("JumpUp"))
  }