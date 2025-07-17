package it.unibo.planner

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import it.unibo.model.fundamentals.Direction.allDirections
import it.unibo.model.planning.algorithms.Algorithm
import it.unibo.model.planning.prologplanner.PrologBuilder
import it.unibo.model.planning.algorithms.Algorithm.{BFS, DFS}
import it.unibo.model.planning.{Configuration, Plan, Planner, PlannerBuilder}

class TestPlannerBuilder extends AnyFlatSpec with Matchers with TestPlanner:
  val configuration: Configuration = Configuration(
    (1,1),
    (2,2),
    Some(5),
    TestScenarioWithPassableTiles(3, 3),
    allDirections)

  private val theoryPaths: Map[Algorithm, String] = Map(
    DFS -> "src/main/prolog/dfs.pl",
    BFS -> "src/main/prolog/bfs.pl"
  )

//  "PlannerBuilder" should "return a configuration error (missing theory)" in:
//    an [IllegalArgumentException] should be thrownBy PrologBuilder().build(configuration.copy(theoryPath = None))
//    