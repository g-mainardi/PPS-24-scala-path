package it.unibo.prologintegration

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import it.unibo.planning.Plan.*
import it.unibo.planning.{BasePrologPlannerBuilder, Plan}


class TestPlannerBuilder extends AnyFlatSpec with Matchers {

  "PlannerBuilder" should "find a valid path with max moves" in :
    val plan: Plan = BasePrologPlannerBuilder()
      .withTheoryFrom("src/main/prolog/plannerWithoutTilesOptimized.pl")
      .withInit((0,0))
      .withGoal((2,3))
      .withMaxMoves(Some(25))
      .run
    plan shouldBe a [SucceededPlanWithoutMaxMoves]

  "PlannerBuilder" should "find a valid path without max moves" in :
    val plan: Plan = BasePrologPlannerBuilder()
      .withTheoryFrom("src/main/prolog/plannerWithoutTilesOptimized.pl")
      .withInit((0,0))
      .withGoal((2,3))
      .withMaxMoves(None)
      .run
    plan shouldBe a [SucceededPlan]

  "PlannerBuilder" should "return a configuration error (missing goal)" in :
    val plan: Plan = BasePrologPlannerBuilder()
      .withTheoryFrom("src/main/prolog/plannerWithoutTilesOptimized.pl")
      .withInit((0,0))
      .run
    plan shouldBe a [FailedPlan]

  "PlannerBuilder" should "return a configuration error (missing init)" in :
    val plan: Plan = BasePrologPlannerBuilder()
      .withTheoryFrom("src/main/prolog/plannerWithoutTilesOptimized.pl")
      .withGoal((2,3))
      .run
    plan shouldBe a [FailedPlan]
}
