package it.unibo.prologintegration

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import it.unibo.planning.Plan.*
import it.unibo.planning.{Plan, Planner, PlannerBuilder}
import it.unibo.planning.prologplanner.{BasePrologBuilder, PrologDFSBuilder}
import it.unibo.model.Direction.allDirections
import it.unibo.planning.Algorithm.*

class TestPlannerBuilder extends AnyFlatSpec with Matchers with TestPlanner {

  "PlannerBuilder" should "find a valid path with max moves" in :
    val planner: Planner = PlannerBuilder()
      .withInit((0,0))
      .withGoal((2,2))
      .withMaxMoves(Some(5))
      .withTiles(passableTiles)
      .withDirections(allDirections)
      .withAlgorithm(DFS)
      .build
    planner.plan shouldBe a [SucceededPlan]

  "PlannerBuilder" should "find a valid path without max moves" in :
    val planner: Planner = PlannerBuilder()
      .withInit((0,0))
      .withGoal((2,2))
      .withTiles(passableTiles)
      .withDirections(allDirections)
      .withAlgorithm(DFS)
      .build
    planner.plan shouldBe a [SucceededPlanWithMoves]

  "PlannerBuilder" should "not find a valid path" in :
    val planner: Planner = PlannerBuilder.start
      .withInit((0,0))
      .withGoal((2,2))
      .withMaxMoves(None)
      .withTiles(blockingTiles)
      .withDirections(allDirections)
      .withAlgorithm(DFS)
      .build
    planner.plan shouldBe a [FailedPlan]

  "PlannerBuilder" should "return a configuration error (missing goal)" in :
    val builder: PlannerBuilder = PlannerBuilder()
      .withInit((0,0))
      .withTiles(blockingTiles)
      .withDirections(allDirections)
      .withAlgorithm(DFS)
    an [IllegalArgumentException] should be thrownBy builder.build
    
    
//  "PlannerBuilder" should "return a configuration error (missing init)" in :
//    val plan: Plan = PrologDFSBuilder()
//      .withTheoryFrom("src/main/prolog/plannerWithTiles.pl")
//      .withGoal((2,3))
//      .withTiles(passableTiles)
//      .withDirections(allDirections)
//      .run
//    plan shouldBe a [FailedPlan]
//
//  "PlannerBuilder" should "return a configuration error (missing tiles)" in :
//    val plan: Plan = PrologDFSBuilder()
//      .withTheoryFrom("src/main/prolog/plannerWithTiles.pl")
//      .withInit((0,0))
//      .withGoal((2,3))
//      .withDirections(allDirections)
//      .run
//    plan shouldBe a [FailedPlan]
//
//  "PlannerBuilder" should "return a configuration error (missing directions)" in :
//    val plan: Plan = PrologDFSBuilder()
//      .withTheoryFrom("src/main/prolog/plannerWithTiles.pl")
//      .withInit((0, 0))
//      .withGoal((2, 3))
//      .withTiles(passableTiles)
//      .run
//    plan shouldBe a [FailedPlan]
}
