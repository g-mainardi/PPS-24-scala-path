package it.unibo.prologintegration

import it.unibo.model.{PlannerWithoutTiles, SucceededPlan}
import it.unibo.model.Tiling.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestPlannerWithoutTiles extends AnyFlatSpec with Matchers {

  "PlannerWithoutTiles" should "find a valid path" in :
    val plan = PlannerWithoutTiles((0, 0), (2, 2), Some(5)).plan
    plan match
      case SucceededPlan(directions, _) =>
        directions should not be empty
      case _ =>
        fail("Expected a SucceededPlan")

  "PlannerWithoutTiles" should "find a path > 0" in :
    val plan = PlannerWithoutTiles((0, 0), (2, 2), Some(5)).plan
    plan match
      case SucceededPlan(directions, moves) =>
        moves should be > 0
      case _ =>
        fail("Moves is not positive")
  
  "PlannerWithoutTiles" should "not find a valid path" in:
    val plan = PlannerWithoutTiles((0, 0), (10, 10), Some(3)).plan
    plan match
      case SucceededPlan(directions, moves) =>
        directions shouldBe empty
      case _ =>
        fail("Expected a SucceededPlan")
}