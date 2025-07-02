package it.unibo.prologintegration

import it.unibo.model.{FailedPlan, Plan}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestPlannerBuilder extends AnyFlatSpec with Matchers {

  "PlannerBuilder" should "find a valid path with max moves" in :
    val plan: Plan = PlannerBuilder()
      .withTheoryFrom("src/main/prolog/plannerWithoutTiles.pl")
      .withInit((0,0))
      .withGoal((2,3))
      .withMaxMoves(Some(5))
      .run
    plan.directions should not be Empty

  "PlannerBuilder" should "find a valid path without max moves" in :
    val plan: Plan = PlannerBuilder()
      .withTheoryFrom("src/main/prolog/plannerWithoutTiles.pl")
      .withInit((0,0))
      .withGoal((2,3))
      .withMaxMoves(None)
      .run
    plan.directions should not be Empty

  "PlannerBuilder" should "return a configuration error (missing goal)" in :
    val plan: Plan = PlannerBuilder()
      .withTheoryFrom("src/main/prolog/plannerWithoutTiles.pl")
      .withInit((0,0))
      .run
    plan shouldBe FailedPlan()
  
  "PlannerBuilder" should "return a configuration error (missing init)" in :
    val plan: Plan = PlannerBuilder()
      .withTheoryFrom("src/main/prolog/plannerWithoutTiles.pl")
      .withGoal((2,3))
      .run
    plan shouldBe FailedPlan()
}
