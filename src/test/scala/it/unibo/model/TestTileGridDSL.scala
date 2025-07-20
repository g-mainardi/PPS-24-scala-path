package it.unibo.model

import it.unibo.model.fundamentals.{Position, Tiling}
import it.unibo.model.fundamentals.Tiling.{Floor, Grass, Lava, Rock, Trap, Wall}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import it.unibo.model.scenario.GridDSL.*
import it.unibo.model.scenario.SpecialTile

import scala.language.postfixOps

class TestTileGridDSL extends AnyFlatSpec with Matchers:
  "TileGridDSL" should "construct a sequence of tiles" in:
    val tiles = grid():
      F | F | F ||;
      G | G | G ||;
      W | W | W ||

    tiles should have length 9
    tiles.head shouldBe a[Floor]
    tiles.head.x shouldBe 0
    tiles.head.y shouldBe 0
    tiles.last shouldBe a[Wall]
    tiles.last.x shouldBe 2
    tiles.last.y shouldBe 2
  
  
  "TileGridDSL" should "work with specified columns" in :
    val tiles = grid(3):
      F | F | F
      G | G | G
      W | W | W

    tiles should have length 9
    tiles.filter(_.y == 0) should have length 3
    tiles.filter(_.y == 1) should have length 3
    tiles.filter(_.y == 2) should have length 3

  "TileGridDSL" should "create tiles at correct positions" in :
    val tiles = grid():
      F | G ||;
      W | T ||

    tiles should contain(Floor(Position(0, 0)))
    tiles should contain(Grass(Position(1, 0)))
    tiles should contain(Wall(Position(0, 1)))
    tiles should contain(Trap(Position(1, 1)))

  "TileGridDSL" should "handle different tile types" in :
    val tiles = grid():
      F | G | W | T | L | R ||

    tiles should have length 6
    tiles should contain(Floor(Position(0, 0)))
    tiles should contain(Grass(Position(1, 0)))
    tiles should contain(Wall(Position(2, 0)))
    tiles should contain(Trap(Position(3, 0)))
    tiles should contain(Lava(Position(4, 0)))
    tiles should contain(Rock(Position(5, 0)))

  "TileGridDSL" should "have two same methods of tiles constructions" in :
    val tilesFirstMethod = grid():
      F | F | F ||;
      G | G | G ||;
      W | W | W ||

    val tilesSecondMethod = grid(3):
      F | F | F
      G | G | G
      W | W | W

    tilesFirstMethod shouldEqual tilesSecondMethod

  "TileGridDSL" should "throw exception for wrong number of tiles when columns specified" in :
    an[IllegalArgumentException] should be thrownBy :
      grid(3):
        F | F
        G | G | G

  "TileGridDSL" should "create teleport tiles" in :
    val tiles = grid():
      F | TP(Position(5, 5)) ||

    tiles should have length 2
    tiles.last shouldBe a[SpecialTile]

