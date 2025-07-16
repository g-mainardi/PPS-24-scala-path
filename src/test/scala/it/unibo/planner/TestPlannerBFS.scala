package it.unibo.planner

import it.unibo.model.Direction.allDirections
import it.unibo.planning.Algorithm.*
import it.unibo.planning.Plan.*
import it.unibo.planning.{Plan, Planner, PlannerBuilder}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestPlannerBFS extends AnyFlatSpec with Matchers with TestPlanner:
  ??? // todo: separate planner from agent
//  "BFS Planner" should "find a valid path with max moves" in :
//    val planner: Planner = PlannerBuilder.start
//      .withInit((0, 0))
//      .withGoal((2, 2))
//      .withMaxMoves(Some(5))
//      .withTiles(TestScenarioWithPassableTiles(3, 3))
//      .withDirections(allDirections)
//      .withAlgorithm(BFS)
//      .build
//    planner.plan shouldBe a [SucceededPlan]
//
//  "BFS Planner" should "find a valid path without max moves" in :
//    val planner: Planner = PlannerBuilder.start
//      .withInit((0, 0))
//      .withGoal((2, 2))
//      .withMaxMoves(None)
//      .withTiles(TestScenarioWithPassableTiles(3, 3))
//      .withDirections(allDirections)
//      .withAlgorithm(BFS)
//      .build
//    planner.plan shouldBe a [SucceededPlanWithMoves]
//
//  "BFS Planner" should "not find a valid path" in :
//    val planner: Planner = PlannerBuilder.start
//      .withInit((0, 0))
//      .withGoal((2, 2))
//      .withMaxMoves(None)
//      .withTiles(TestScenarioWithPassableTiles(3, 3))
//      .withDirections(allDirections)
//      .withAlgorithm(BFS)
//      .build
//    planner.plan shouldBe a [FailedPlan]