package it.unibo.prologintegration

import alice.tuprolog.Term
import it.unibo.prologintegration.Scala2Prolog.*
import it.unibo.prologintegration.Prolog2Scala.*

class PlannerTemplate {
  def runPlanner(path: String, maxMoves: Int): Option[List[String]] =
    val engine: Engine = mkPrologEngineFromFile(path)
    
    val goal = Term.createTerm("plan(P, " + maxMoves + ")")
    val solutions = engine(goal)

    val firstPath: Option[List[String]] =
      solutions.headOption.map { info =>
        val listTerm = extractTerm(info, "P")
        extractListFromTerm(listTerm).toList
      }

    firstPath
}

@main def testPlanner(): Unit =
  val planner = PlannerTemplate()
  val plan = planner.runPlanner("src/main/prolog/basePlanner.pl", 5)

  plan match
    case Some(path) => println(s"Found path: ${path.mkString(" -> ")}")
    case None => println("No solution found.")