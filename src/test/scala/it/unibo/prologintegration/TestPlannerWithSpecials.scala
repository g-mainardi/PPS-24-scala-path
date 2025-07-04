package it.unibo.prologintegration

import it.unibo.model.Tiling.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import it.unibo.planning.Plan.*
import it.unibo.planning.{Plan, PlannerWithSpecials, PlannerWithTiles}
import it.unibo.model.Direction.allDirections

class TestPlannerWithSpecials extends AnyFlatSpec with Matchers with TestPlanner {
  "PlannerWithSpecials" should "find a valid path" in :
    val plan: Plan = PlannerWithSpecials((0, 0), (2, 2), passableTiles).plan
    plan shouldBe a[SucceededPlanWithMoves]

  "PlannerWithSpecials" should "find a valid path with max moves" in :
    val plan = PlannerWithSpecials((0, 0), (2, 2), passableTiles, Some(7)).plan
    plan shouldBe a[SucceededPlan]

  "PlannerWithSpecials" should "find a valid path with directions" in :
    val plan = PlannerWithSpecials((0, 0), (2, 2), passableTiles, Some(7), allDirections).plan
    plan shouldBe a[SucceededPlan]

  "PlannerWithSpecials" should "not find a valid path" in :
    val plan: Plan = PlannerWithSpecials((0, 0), (10, 10), blockingTiles).plan
    plan shouldBe a[FailedPlan]

  "PlannerWithSpecials" should "not find a valid path with max moves" in :
    val plan = PlannerWithSpecials((0, 0), (10, 10), blockingTiles, Some(3)).plan
    plan shouldBe a[FailedPlan]
}
