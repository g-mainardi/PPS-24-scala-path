package it.unibo.planning.prologplanner

import alice.tuprolog.{Term, Theory}
import it.unibo.planning.{Configuration, Planner, PrologPlanner}
import it.unibo.prologintegration.Scala2Prolog.*

class PrologBuilder(configuration: Configuration) extends BasePrologBuilder:
  def build: Planner = configuration match
      case Configuration(InitPos(initFact), Goal(goalFact), MaxMoves(goalTerm), Tiles(tileFacts), Directions(directionsFact), Theory(theoryString)) =>
        val fullTheory = new Theory(s"$initFact\n$goalFact\n$directionsFact\n$tileFacts\n$theoryString")
        println(s"\n$fullTheory\n")
        val engine: Engine = mkPrologEngine(fullTheory)
        PrologPlanner(engine, goalTerm, configuration.maxMoves)
      case _ => throw new IllegalArgumentException("Invalid configuration for PrologBuilder")