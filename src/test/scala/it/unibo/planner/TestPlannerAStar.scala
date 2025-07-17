package it.unibo.planner

import it.unibo.model.fundamentals.Direction.allDirections
import it.unibo.model.agent.Agent
import it.unibo.model.planning.algorithms.Algorithm.AStar
import it.unibo.model.scenario.Scenario
import it.unibo.model.planning.{Plan, Planner, PlannerBuilder}
import it.unibo.model.planning.Plan.{SucceededPlan, FailedPlan, SucceededPlanWithMoves}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestPlannerAStar extends AnyFlatSpec with Matchers with TestPlanner:
  val passableScenario: Scenario = new TestScenarioWithPassableTiles(3, 3)
  val blockingScenario: Scenario = new TestScenarioWithBlockingTiles(3, 3)
  passableScenario.generate()
  blockingScenario.generate()

  "ScalaPlanner" should "find a valid path without max moves" in:
    val planner: Planner = PlannerBuilder.start
      .withInit((0, 0))
      .withGoal((2, 2))
      .withMaxMoves(None)
      .withTiles(passableScenario)
      .withDirections(allDirections)
      .withAlgorithm(AStar)
      .build
    val plan: Plan = planner.plan
    plan shouldBe a[SucceededPlanWithMoves]

  "ScalaPlanner" should "not find a valid path" in :
    val planner: Planner = PlannerBuilder.start
      .withInit((0, 0))
      .withGoal((2, 2))
      .withMaxMoves(None)
      .withTiles(blockingScenario)
      .withDirections(allDirections)
      .withAlgorithm(AStar)
      .build
    planner.plan shouldBe a[FailedPlan]