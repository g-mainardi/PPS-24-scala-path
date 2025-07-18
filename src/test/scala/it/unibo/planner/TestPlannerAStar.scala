package it.unibo.planner

import it.unibo.model.planning.algorithms.Algorithm.AStar
import it.unibo.model.planning.{Plan, Planner, PlannerBuilder}
import it.unibo.model.planning.Plan.{FailedPlan, SucceededPlanWithMoves}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestPlannerAStar extends AnyFlatSpec with Matchers with TestPlanner:
  "ScalaPlanner" should "find a valid path without max moves" in:
    val planner: Planner = PlannerBuilder.start
      .withInit(initPos)
      .withGoal(goalPos)
      .withMaxMoves(None)
      .withTiles(passableScenario)
      .withDirections(directions)
      .withAlgorithm(AStar)
      .build
    val plan: Plan = planner.plan
    plan shouldBe a[SucceededPlanWithMoves]

  "ScalaPlanner" should "not find a valid path" in :
    val planner: Planner = PlannerBuilder.start
      .withInit(initPos)
      .withGoal(goalPos)
      .withMaxMoves(None)
      .withTiles(blockingScenario)
      .withDirections(directions)
      .withAlgorithm(AStar)
      .build
    planner.plan shouldBe a[FailedPlan]