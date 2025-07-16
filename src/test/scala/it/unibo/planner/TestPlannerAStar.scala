package it.unibo.planner

import it.unibo.model.{Agent, Scenario}
import it.unibo.model.Direction.allDirections
import it.unibo.planning.Algorithm.*
import it.unibo.planning.Plan.*
import it.unibo.planning.{Plan, Planner, PlannerBuilder}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestPlannerAStar extends AnyFlatSpec with Matchers with TestPlanner:

  "PlannerBuilder" should "find a valid path without max moves" in:
    var scenario: Scenario = new TestScenarioWithPassableTiles(3, 3)
    scenario.generate()
    val planner: Planner = PlannerBuilder.start
      .withInit((0, 0))
      .withGoal((2, 2))
      .withMaxMoves(None)
      .withTiles(scenario)
      .withDirections(allDirections)
      .withAlgorithm(AStar)
      .build
    val plan: Plan = planner.plan
    plan shouldBe a[SucceededPlanWithMoves]