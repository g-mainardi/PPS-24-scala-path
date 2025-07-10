package it.unibo.planner

import it.unibo.planning.{Algorithm, Configuration, Plan, Planner, PlannerBuilder}
import it.unibo.planning.prologplanner.PrologBuilder
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import it.unibo.model.Direction.allDirections
import it.unibo.planning.Algorithm.{BFS, DFS}

class TestPlannerBuilder extends AnyFlatSpec with Matchers with TestPlanner:
  val configuration: Configuration = Configuration(
    (1,1),
    (2,2),
    Some(5),
    passableTiles,
    allDirections)

  private val theoryPaths: Map[Algorithm, String] = Map(
    DFS -> "src/main/prolog/dfs.pl",
    BFS -> "src/main/prolog/bfs.pl"
  )

  "PlannerBuilder" should "return a configuration error (missing theory)" in :
    an [IllegalArgumentException] should be thrownBy PrologBuilder().build(configuration.copy(theoryPath = None))