package it.unibo.planner

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import it.unibo.model.planning.algorithms.Algorithm
import it.unibo.model.planning.algorithms.Algorithm.*
import it.unibo.model.planning.{PlannerBuilder, PrologPlanner, ScalaPlanner}

class TestPlannerBuilder extends AnyFlatSpec with Matchers with TestPlanner:
  "PlannerBuilder" should "build a PrologPlanner for BFS" in {
    val planner = PlannerBuilder.start
      .withInit(initPos)
      .withGoal(goalPos)
      .withMaxMoves(maxMoves)
      .withTiles(passableScenario)
      .withDirections(directions)
      .withAlgorithm(BFS)
      .build

    planner shouldBe a [PrologPlanner]
  }

  it should "build a ScalaPlanner for AStar" in {
    val planner = PlannerBuilder.start
      .withInit(initPos)
      .withGoal(goalPos)
      .withMaxMoves(None)
      .withTiles(passableScenario)
      .withDirections(directions)
      .withAlgorithm(AStar)
      .build

    planner shouldBe a[ScalaPlanner]
  }

  it should "build a PrologPlanner for DFS" in {
    val planner = PlannerBuilder.start
      .withInit(initPos)
      .withGoal(goalPos)
      .withMaxMoves(None)
      .withTiles(passableScenario)
      .withDirections(directions)
      .withAlgorithm(DFS)
      .build

    planner shouldBe a [PrologPlanner]
  }
