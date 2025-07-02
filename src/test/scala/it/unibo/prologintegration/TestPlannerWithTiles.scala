package it.unibo.prologintegration

import it.unibo.model.PlannerWithMaxMoves
import it.unibo.model.Tiling.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestPlannerWithTiles extends AnyFlatSpec with Matchers {
  val passableTiles: List[Tile] = List(
    Floor(Position(0, 0)),
    Floor(Position(0, 1)),
    Floor(Position(1, 0)),
    Floor(Position(1, 1)),
    Floor(Position(1, 2)),
    Floor(Position(2, 1)),
    Floor(Position(2, 2)))

  val blockingTiles: List[Tile] = List(
    Wall(Position(0, 0)),
    Wall(Position(0, 1)),
    Wall(Position(1, 0)),
    Wall(Position(1, 1)),
    Wall(Position(1, 2)),
    Wall(Position(2, 1)),
    Wall(Position(2, 2)))

  "PlannerWithTiles" should "find a valid path" in :
    val pathOpt = PlannerWithMaxMoves((0, 0), (2, 2), 5, passableTiles).plan
    pathOpt should not be None

  "PlannerWithTiles" should "not find a valid path" in :
    val pathOpt = PlannerWithMaxMoves((0, 0), (10, 10), 3, blockingTiles).plan
    pathOpt shouldBe None
}
