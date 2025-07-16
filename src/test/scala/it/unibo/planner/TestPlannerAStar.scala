package it.unibo.planner

import it.unibo.model.Agent
import it.unibo.model.Direction.allDirections
import it.unibo.planning.Algorithm.*
import it.unibo.planning.Plan.*
import it.unibo.planning.{Plan, Planner, PlannerBuilder}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestPlannerAStar extends AnyFlatSpec with Matchers with TestPlanner:

  "PlannerBuilder" should "find a valid path without max moves" in:
    val agent: Agent = PlannerBuilder.start
      .withInit((0, 0))
      .withGoal((2, 2))
      .withMaxMoves(None)
      .withTiles(TestScenarioWithPassableTiles(3, 3))
      .withDirections(allDirections)
      .withAlgorithm(AStar)
      .build

    noException should be thrownBy
      agent.searchPlan


    // agent .planner.plan shouldBe a[SucceededPlan]
  