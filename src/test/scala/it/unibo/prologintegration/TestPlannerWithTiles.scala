package it.unibo.prologintegration

import it.unibo.model.Tiling.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import it.unibo.planning.Plan.*
import it.unibo.planning.{Plan, PlannerWithTiles, PlannerWithoutTiles}


class TestPlannerWithTiles extends AnyFlatSpec with Matchers with TestPlanner {
  "PlannerWithTiles" should "find a valid path" in :
    val plan: Plan = PlannerWithTiles((0, 0), (2, 2), passableTiles).plan
    plan shouldBe a[SucceededPlanWithMaxMoves]

  "PlannerWithTiles" should "not find a valid path" in :
    val plan: Plan = PlannerWithTiles((0, 0), (10, 10), blockingTiles).plan
    plan shouldBe a[FailedPlan]

  "PlannerWithTiles" should "find a valid path with max moves" in :
    val plan = PlannerWithTiles((0, 0), (2, 2), passableTiles, Some(5)).plan
    plan shouldBe a[SucceededPlan]

  "PlannerWithTiles" should "not find a valid path with max moves" in :
    val plan = PlannerWithTiles((0, 0), (10, 10), blockingTiles, Some(3)).plan
    plan shouldBe a[FailedPlan]
}
