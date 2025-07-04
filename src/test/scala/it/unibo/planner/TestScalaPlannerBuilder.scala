package it.unibo.planner;

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import it.unibo.planning.Plan.*
import it.unibo.planning.{AStar, Plan}
import it.unibo.planning.scalaplanner.BaseScalaPlannerBuilder
import it.unibo.prologintegration.TestPlanner



class TestScalaPlannerBuilder extends AnyFlatSpec with Matchers with TestPlanner:

  "PlannerBuilder" should "find a valid path without max moves" in:
    val plan: Plan = BaseScalaPlannerBuilder()
      .withAlgorithm(new AStar())
      .withInit((0, 0))
      .withGoal((2, 2))
      .withTiles(passableTiles)
      .run
    plan shouldBe a[SucceededPlanWithMoves]

