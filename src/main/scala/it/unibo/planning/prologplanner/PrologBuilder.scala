package it.unibo.planning.prologplanner

import alice.tuprolog.{Term, Theory}
import it.unibo.model.Direction
import it.unibo.model.Tiling.*
import it.unibo.planning.{Configuration, Planner, PrologPlanner}
import it.unibo.prologintegration.Scala2Prolog.*

class PrologBuilder extends BasePrologBuilder {

  private object IncompletePlannerConfig:
    def unapply(config: Configuration): Option[String] =
      val labels = List("init position", "goal", "environmental tiles", "possible directions", "theory path")
      val options = List(config.initPos, config.goalPos, config.environmentTiles, config.directions, config.theoryPath)
      labels.zip(options).collectFirst {
        case (label, None) => s"missing $label"
      }
  
  // (initPos, goalPos, maxMoves, environmentTiles, directions)
  def build(configuration: Configuration): Planner = configuration match
    case IncompletePlannerConfig(reason) => throw new IllegalArgumentException(s"Planner not fully configured, $reason")
    case Configuration(InitPos(initFact), Goal(goalFact), MaxMoves(maxMovesFact), Tiles(tileFacts), Directions(directionsFact), Theory(theoryString)) =>
      val fullTheory = new Theory(s"$initFact\n$goalFact\n$directionsFact\n$tileFacts\n$theoryString")
      println(s"\n$fullTheory\n")
      val engine: Engine = mkPrologEngine(fullTheory)
      val maxMoves = configuration._3
      val goal = maxMoves match
        case None => Term.createTerm(s"plan(P, M)")
        case Some(moves) => Term.createTerm(s"plan(P, $moves)")
      PrologPlanner(engine, goal, maxMoves)
}

object PrologBuilder:
  def apply(): PrologBuilder = new PrologBuilder()