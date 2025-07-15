package it.unibo.planning.prologplanner
import it.unibo.planning.prologplanner.Conversions.given_Conversion_Int_Int_Position

import alice.tuprolog.{Term, Theory}
import it.unibo.model.Agent
import it.unibo.planning.{Configuration, Plan, Planner, PrologPlanner}
import it.unibo.prologintegration.Scala2Prolog.*

class PrologBuilder(configuration: Configuration) extends BasePrologBuilder:
  def build: Agent = configuration match
      case Configuration(InitPos(initFact), Goal(goalFact), MaxMoves(goalTerm), Tiles(tileFacts), Directions(directionsFact), Theory(theoryString), _) =>
        val fullTheory = new Theory(s"$initFact\n$goalFact\n$directionsFact\n$tileFacts\n$theoryString")
        println(s"\n$fullTheory\n")
        val engine: Engine = mkPrologEngine(fullTheory)
        val plan: Plan = PrologPlanner(engine, goalTerm, configuration.maxMoves).plan
        new Agent (
          configuration.initPos,
          plan,
          configuration.environmentTiles,
        )
        
      case _ => throw new IllegalArgumentException("Invalid configuration for PrologBuilder")