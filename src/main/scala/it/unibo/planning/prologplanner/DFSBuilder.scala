package it.unibo.planning.prologplanner

import alice.tuprolog.{SolveInfo, Term, Theory}
import it.unibo.model.Direction
import it.unibo.model.Direction.{Cardinals, Diagonals}
import it.unibo.model.Tiling.*
import it.unibo.planning.{Configuration, Plan, Planner, PrologBuilder, PrologPlanner}
import it.unibo.planning.Plan.{FailedPlan, SucceededPlan, SucceededPlanWithMoves}
import it.unibo.planning.prologplanner.MoveFactsGenerator.generateMoveRules
import it.unibo.prologintegration.Prolog2Scala.*
import it.unibo.prologintegration.Scala2Prolog.*

import scala.io.Source

class DFSBuilder extends BasePrologPlannerBuilder {
  val theoryString: String = Source.fromFile("src/main/prolog/plannerWithTiles.pl").mkString

  private object IncompletePlannerConfig:
    def unapply(config: (Option[(Int, Int)], Option[(Int, Int)], Option[List[Tile]], Option[List[Direction]])): Option[String] =
      val labels = List("init position", "goal", "environmental tiles", "possible directions")
      val options = config.toList
      labels.zip(options).collectFirst {
        case (label, None) => s"missing $label"
      }

  // (initPos, goalPos, maxMoves, environmentTiles, directions) match
  override def build(configuration: Configuration): Planner = configuration match
    // case IncompletePlannerConfig(reason) => FailedPlan(s"Planner not fully configured, $reason")
    case (InitPos(initFact), Goal(goalFact), Tiles(tileFacts), Directions(directionsFact)) =>
      val fullTheory = new Theory(s"$initFact\n$goalFact\n$directionsFact\n$tileFacts\n$theoryString")
      println(s"\n$fullTheory\n")
      val engine: Engine = mkPrologEngine(fullTheory)
      val goal = configuration.maxMoves match
        case None => Term.createTerm(s"plan(P, M)")
        case Some(moves) => Term.createTerm(s"plan(P, $moves)")
      PrologPlanner(engine, goal, configuration.maxMoves)
}

object DFSPrologPlannerBuilder:
  def apply(): DFSBuilder = new DFSBuilder()