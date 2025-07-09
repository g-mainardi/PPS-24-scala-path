package it.unibo.planning.prologplanner

import it.unibo.model.Tiling.Tile
import alice.tuprolog.{SolveInfo, Term, Theory}
import it.unibo.model.Direction
import it.unibo.model.Direction.{Cardinals, Diagonals}
import it.unibo.model.Tiling.*
import it.unibo.planning.Plan.{FailedPlan, SucceededPlan, SucceededPlanWithMoves}
import it.unibo.planning.prologplanner.MoveFactsGenerator.generateMoveRules
import it.unibo.planning.{Configuration, Plan, Planner, PrologPlanner}
import it.unibo.prologintegration.Prolog2Scala.*
import it.unibo.prologintegration.Scala2Prolog.*

import scala.io.Source
import scala.util.Using

class PrologBFSBuilder extends BasePrologBuilder {
  val theoryString: String = Using.resource(Source.fromFile("src/main/prolog/bfs.pl"))(_.mkString)

  private object IncompletePlannerConfig:
    def unapply(config: Configuration): Option[String] =
      val labels = List("init position", "goal", "max moves", "environmental tiles", "possible directions")
      val options = List(config.initPos, config.goalPos, config.maxMoves, config.environmentTiles, config.directions)
      labels.zip(options).collectFirst {
        case (label, None) => s"missing $label"
      }

  // (initPos, goalPos, maxMoves, environmentTiles, directions)
  def build(configuration: Configuration): Planner = configuration match
    case IncompletePlannerConfig(reason) => throw new IllegalArgumentException(s"Planner not fully configured, $reason")
    case Configuration(InitPos(initFact), Goal(goalFact), MaxMoves(maxMovesFact), Tiles(tileFacts), Directions(directionsFact)) =>
      val fullTheory = new Theory(s"$initFact\n$goalFact\n$directionsFact\n$tileFacts\n$maxMovesFact\n$theoryString")
      println(s"\n$fullTheory\n")
      val engine: Engine = mkPrologEngine(fullTheory)
      val goal = Term.createTerm(s"plan(P)")
      PrologPlanner(engine, goal, configuration._3)
}

object PrologBFSBuilder:
  def apply(): PrologBFSBuilder = new PrologBFSBuilder()