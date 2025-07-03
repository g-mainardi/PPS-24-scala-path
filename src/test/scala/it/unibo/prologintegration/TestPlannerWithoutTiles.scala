package it.unibo.prologintegration

import it.unibo.model.Tiling.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import it.unibo.planning.Plan.*
import it.unibo.planning.{Plan, PlannerWithoutTiles}
import org.scalatest.concurrent.TimeLimits.failAfter
import org.scalatest.time.SpanSugar._


class TestPlannerWithoutTiles extends AnyFlatSpec with Matchers {

  "PlannerWithoutTiles" should "find a valid path" in :
    val plan = PlannerWithoutTiles((0, 0), (2, 2), Some(25)).plan
    plan match
      case SucceededPlan(directions) =>
        directions should not be empty
      case _ =>
        fail("Expected a SucceededPlan")

  "PlannerWithoutTiles" should "find a path > 0" in :
    val plan = PlannerWithoutTiles((0, 0), (2, 2)).plan
    plan match
      case SucceededPlanWithMaxMoves(directions, moves) =>
        println(moves)
        moves should be > 0
      case _ =>
        fail("Moves is not positive ")

//  "PlannerWithoutTiles" should "not find a valid path" in {
//    failAfter(2.seconds) {
//      val plan = PlannerWithoutTiles((0, 0), (10, 10), Some(5)).plan
//      plan match
//        case SucceededPlan(directions, moves) =>
//          directions shouldBe empty
//        case _ =>
//          fail("Expected a SucceededPlan")
//    }
//  }
}