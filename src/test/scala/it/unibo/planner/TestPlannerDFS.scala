package it.unibo.planner

import it.unibo.model.fundamentals.Direction.allDirections
import it.unibo.model.scenario.Scenario
import it.unibo.model.planning.{Plan, Planner, PlannerBuilder}
import it.unibo.model.planning.Plan.{FailedPlan, SucceededPlan, SucceededPlanWithMoves}
import it.unibo.model.planning.algorithms.Algorithm.DFS
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestPlannerDFS extends AnyFlatSpec with Matchers with TestPlanner:
  val passableScenario: Scenario = new TestScenarioWithPassableTiles(3, 3)
  val blockingScenario: Scenario = new TestScenarioWithBlockingTiles(3, 3)
  passableScenario.generate()
  blockingScenario.generate()

  "DFS Planner" should "find a valid path with max moves" in :
    val planner: Planner = PlannerBuilder.start
      .withInit((0,0))
      .withGoal((2,2))
      .withMaxMoves(Some(5))
      .withTiles(passableScenario)
      .withDirections(allDirections)
      .withAlgorithm(DFS)
      .build
    planner.plan shouldBe a [SucceededPlan]

  "PlannerBuilder" should "find a valid path without max moves" in :
    val planner: Planner = PlannerBuilder.start
      .withInit((0,0))
      .withGoal((2,2))
      .withMaxMoves(None)
      .withTiles(passableScenario)
      .withDirections(allDirections)
      .withAlgorithm(DFS)
      .build
    planner.plan shouldBe a [SucceededPlanWithMoves]

  "PlannerBuilder" should "not find a valid path" in :
    val planner: Planner = PlannerBuilder.start
      .withInit((0,0))
      .withGoal((2,2))
      .withMaxMoves(None)
      .withTiles(blockingScenario)
      .withDirections(allDirections)
      .withAlgorithm(DFS)
      .build
    planner.plan shouldBe a [FailedPlan]
