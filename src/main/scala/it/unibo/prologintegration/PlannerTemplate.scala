package it.unibo.prologintegration

import alice.tuprolog.Term
import it.unibo.prologintegration.Scala2Prolog.*
import it.unibo.prologintegration.Prolog2Scala.*

class PlannerTemplate {
  def runPlanner(): Unit =
    val engine: Engine = mkPrologEngineFromFile("src/main/prolog/basePlanner.pl")
    
    val goal = Term.createTerm("plan(P, 5)")
    val solutions = engine(goal)

    val firstPath: Option[List[String]] =
      solutions.headOption.map { info =>
        val listTerm = extractTerm(info, "P")
        extractListFromTerm(listTerm).toList
      }

    // Stampa
    firstPath match
      case Some(path) => println(s"Found path: ${path.mkString(" -> ")}")
      case None => println("No solution found.")
}

@main def testPlanner(): Unit =
  val planner = PlannerTemplate()
  planner.runPlanner()