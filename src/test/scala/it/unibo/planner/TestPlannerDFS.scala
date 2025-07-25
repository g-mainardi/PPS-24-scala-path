package it.unibo.planner

import it.unibo.model.fundamentals.Position
import it.unibo.model.planning.{Plan, Planner, PlannerBuilder}
import it.unibo.model.planning.Plan.{FailedPlan, SucceededPlan, SucceededPlanWithMoves}
import it.unibo.model.planning.algorithms.Algorithm.DFS
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestPlannerDFS extends AnyFlatSpec with Matchers with TestPlanner:
  "DFS Planner" should "find a valid path with max moves" in :
    val planner: Planner = PlannerBuilder.start
      .withInit(initPos)
      .withGoal(goalPos)
      .withMaxMoves(maxMoves)
      .withTiles(passableScenario)
      .withDirections(directions)
      .withAlgorithm(DFS)
      .build
    planner.plan shouldBe a [SucceededPlan]

  "PlannerBuilder" should "find a valid path without max moves" in :
    val planner: Planner = PlannerBuilder.start
      .withInit(initPos)
      .withGoal(goalPos)
      .withMaxMoves(None)
      .withTiles(passableScenario)
      .withDirections(directions)
      .withAlgorithm(DFS)
      .build
    planner.plan shouldBe a [SucceededPlanWithMoves]

  "PlannerBuilder" should "find a valid path with teleport" in :
    val planner: Planner = PlannerBuilder.start
      .withInit(Position(0, 0))
      .withGoal(Position(8, 8))
      .withMaxMoves(None)
      .withTiles(scenarioWithClosedWalls)
      .withDirections(directions)
      .withAlgorithm(DFS)
      .build
    val plan: Plan = planner.plan
    plan shouldBe a [SucceededPlanWithMoves]

  "DFS Planner" should "find a valid path with specials" in :
    val planner: Planner = PlannerBuilder.start
      .withInit(initPos)
      .withGoal(goalPos)
      .withMaxMoves(None)
      .withTiles(specialsScenario)
      .withDirections(directions)
      .withAlgorithm(DFS)
      .build
    planner.plan shouldBe a [SucceededPlanWithMoves]


  "PlannerBuilder" should "not find a valid path" in :
    val planner: Planner = PlannerBuilder.start
      .withInit(initPos)
      .withGoal(goalPos)
      .withMaxMoves(None)
      .withTiles(blockingScenario)
      .withDirections(directions)
      .withAlgorithm(DFS)
      .build
    planner.plan shouldBe a [FailedPlan]
