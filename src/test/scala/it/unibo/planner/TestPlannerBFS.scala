package it.unibo.planner

import it.unibo.model.fundamentals.Direction.allDirections
import it.unibo.model.scenario.Scenario
import it.unibo.model.fundamentals.Position
import it.unibo.model.planning.{Plan, Planner, PlannerBuilder}
import it.unibo.model.planning.Plan.{FailedPlan, SucceededPlan, SucceededPlanWithMoves}
import it.unibo.model.planning.algorithms.Algorithm.BFS
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestPlannerBFS extends AnyFlatSpec with Matchers with TestPlanner:
  "BFS Planner" should "find a valid path with max moves" in :
    val planner: Planner = PlannerBuilder.start
      .withInit(initPos)
      .withGoal(goalPos)
      .withMaxMoves(maxMoves)
      .withTiles(passableScenario)
      .withDirections(directions)
      .withAlgorithm(BFS)
      .build
    planner.plan shouldBe a [SucceededPlan]

  "BFS Planner" should "find a valid path without max moves" in :
    val planner: Planner = PlannerBuilder.start
      .withInit(initPos)
      .withGoal(goalPos)
      .withMaxMoves(None)
      .withTiles(passableScenario)
      .withDirections(directions)
      .withAlgorithm(BFS)
      .build
    planner.plan shouldBe a [SucceededPlanWithMoves]

  "BFS Planner" should "not find a valid path" in :
    val planner: Planner = PlannerBuilder.start
      .withInit(initPos)
      .withGoal(goalPos)
      .withMaxMoves(maxMoves)
      .withTiles(blockingScenario)
      .withDirections(directions)
      .withAlgorithm(BFS)
      .build
    planner.plan shouldBe a [FailedPlan]