package it.unibo.planning.prologplanner

import it.unibo.model.Tiling.Tile
import alice.tuprolog.{SolveInfo, Term, Theory}
import it.unibo.model.Direction
import it.unibo.model.Direction.{Cardinals, Diagonals}
import it.unibo.model.Tiling.*
import it.unibo.planning.Plan.{FailedPlan, SucceededPlan, SucceededPlanWithMoves}
import it.unibo.planning.prologplanner.MoveFactsGenerator.generateMoveRules
import it.unibo.planning.{Plan, PrologBuilder}
import it.unibo.prologintegration.Prolog2Scala.*
import it.unibo.prologintegration.Scala2Prolog.*

class BFSBuilder extends BasePrologPlannerBuilder {
  private object IncompletePlannerConfig:
    def unapply(config: (Option[(Int, Int)], Option[(Int, Int)], Option[String], Option[Int], Option[List[Tile]], Option[List[Direction]])): Option[String] =
      val labels = List("init position", "goal", "theory", "max moves", "environmental tiles", "possible directions")
      val options = config.toList
      labels.zip(options).collectFirst {
        case (label, None) => s"missing $label"
      }

  private object MaxMoves:
    def unapply(o: Option[Int]): Option[String] = o map (maxMoves => s"maxmoves($maxMoves)).")

  override def run: Plan = (initPos, goalPos, theoryPath, maxMoves, environmentTiles, directions) match
    case IncompletePlannerConfig(reason) => FailedPlan(s"Planner not fully configured, $reason")
    case (InitPos(initFact), Goal(goalFact), Theory(theoryString), MaxMoves(maxMovesFact), Tiles(tileFacts), Directions(directionsFact)) =>
      val fullTheory = new Theory(s"$initFact\n$goalFact\n$directionsFact\n$tileFacts\n$maxMovesFact\n$theoryString")
      println(s"\n$fullTheory\n")
      val engine: Engine = mkPrologEngine(fullTheory)
      val goal = Term.createTerm(s"plan(P)")
      checkSolutions(engine(goal))
}

object BFSPrologPlannerBuilder:
  def apply(): BFSBuilder = new BFSBuilder()