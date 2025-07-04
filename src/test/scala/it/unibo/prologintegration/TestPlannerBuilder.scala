package it.unibo.prologintegration

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import it.unibo.planning.Plan.*
import it.unibo.planning.{BasePrologPlannerBuilder, Plan}


class TestPlannerBuilder extends AnyFlatSpec with Matchers with TestPlanner {

  "PlannerBuilder" should "find a valid path with max moves" in :
    val plan: Plan = BasePrologPlannerBuilder()
      .withTheoryFrom("src/main/prolog/plannerWithTiles.pl")
      .withInit((0,0))
      .withGoal((2,2))
      .withTiles(passableTiles)
      .withMaxMoves(Some(5))
      .run
    plan shouldBe a [SucceededPlan]

  "PlannerBuilder" should "find a valid path without max moves" in :
    val plan: Plan = BasePrologPlannerBuilder()
      .withTheoryFrom("src/main/prolog/plannerWithTiles.pl")
      .withInit((0,0))
      .withGoal((2,2))
      .withTiles(passableTiles)
      .run
    plan shouldBe a [SucceededPlanWithMoves]

  "PlannerBuilder" should "return a configuration error (missing goal)" in :
    val plan: Plan = BasePrologPlannerBuilder()
      .withTheoryFrom("src/main/prolog/plannerWithTiles.pl")
      .withInit((0,0))
      .withTiles(passableTiles)
      .run
    plan shouldBe a [FailedPlan]

  "PlannerBuilder" should "return a configuration error (missing init)" in :
    val plan: Plan = BasePrologPlannerBuilder()
      .withTheoryFrom("src/main/prolog/plannerWithTiles.pl")
      .withGoal((2,3))
      .withTiles(passableTiles)
      .run
    plan shouldBe a [FailedPlan]

  "PlannerBuilder" should "return a configuration error (missing tiles)" in :
    val plan: Plan = BasePrologPlannerBuilder()
      .withTheoryFrom("src/main/prolog/plannerWithTiles.pl")
      .withInit((0,0))
      .withGoal((2,3))
      .run
    plan shouldBe a [FailedPlan]
}
