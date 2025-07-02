package it.unibo.prologintegration

import it.unibo.model.PlannerWithoutTiles
import it.unibo.model.Tiling.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestPlannerWithoutTiles extends AnyFlatSpec with Matchers {

  "PlannerWithoutTiles" should "find a valid path" in :
    val plan = PlannerWithoutTiles((0, 0), (2, 2), Some(5)).plan
    plan.directions should not be Empty
  
  "PlannerWithoutTiles" should "not find a valid path" in:
    val plan = PlannerWithoutTiles((0, 0), (10, 10), Some(3)).plan
    plan.directions shouldBe Empty
}