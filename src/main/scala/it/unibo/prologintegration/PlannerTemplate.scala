package it.unibo.prologintegration

import alice.tuprolog.{Term, Theory}
import it.unibo.prologintegration.Scala2Prolog.*
import it.unibo.prologintegration.Prolog2Scala.*

import scala.io.Source

class PlannerTemplate {
  def runPlanner(path: String, maxMoves: Int, from: (Int, Int), to: (Int, Int)): Option[List[String]] =
    
    val baseTheory: String = Source.fromFile(path).mkString
    val initFact = s"init(s(${from._1}, ${from._2}))."
    val goalFact = s"goal(s(${to._1}, ${to._2}))."
    
    val fullTheory: Theory = new Theory (baseTheory + "\n" + initFact + "\n" + goalFact)
    val engine: Engine = mkPrologEngine(fullTheory)
    
    val goal = Term.createTerm("plan(P, " + maxMoves + ")")
    val solutions = engine(goal)

    // first path
    solutions.headOption.map { info =>
        val listTerm = extractTerm(info, "P")
        extractListFromTerm(listTerm).toList
      }
}

@main def testPlanner(): Unit =
  val planner = PlannerTemplate()
  val plan = planner.runPlanner("src/main/prolog/basePlanner.pl", 5, (0,0), (2,2))

  plan match
    case Some(path) => println(s"Found path: ${path.mkString(" -> ")}")
    case None => println("No solution found.")