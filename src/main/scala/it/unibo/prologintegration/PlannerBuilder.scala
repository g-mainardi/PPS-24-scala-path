package it.unibo.prologintegration

import alice.tuprolog.{Term, Theory}
import it.unibo.prologintegration.Scala2Prolog.*
import it.unibo.prologintegration.Prolog2Scala.*

import scala.util.Try
import scala.io.Source
import it.unibo.model.{Direction, Cardinals, Diagonals}

object Conversions:
  given Conversion[String, Direction] with
    def apply(s: String): Direction =
      Try(Cardinals.valueOf(s.capitalize)).getOrElse(Diagonals.valueOf(s.capitalize))

class PlannerBuilder private():
  private var theoryStr: String = ""
  private var initPos: Option[(Int, Int)] = None
  private var goalPos: Option[(Int, Int)] = None
  private var maxMoves: Option[Int] = None

  def withTheoryFrom(path: String): PlannerBuilder =
    this.theoryStr = Source.fromFile(path).mkString
    this

  def withInit(initPos: (Int, Int)): PlannerBuilder =
    this.initPos = Some(initPos)
    this

  def withGoal(goal: (Int, Int)): PlannerBuilder =
    this.goalPos = Some(goal)
    this

  def withMaxMoves(m: Int): PlannerBuilder =
    this.maxMoves = Some(m)
    this

  def run(): Option[List[Direction]] =
    (initPos, goalPos, maxMoves) match
      case (Some((ix, iy)), Some((gx, gy)), Some(moves)) =>
        val initFact = s"init(s($ix, $iy))."
        val goalFact = s"goal(s($gx, $gy))."
        val fullTheory = new Theory(s"$theoryStr\n$initFact\n$goalFact")

        val engine: Engine = mkPrologEngine(fullTheory)
        val goal = Term.createTerm(s"plan(P, $moves)")
        val solutions = engine(goal)

        val directions: Option[List[String]] = solutions.headOption.map { info =>
          val listTerm = extractTerm(info, "P")
          extractListFromTerm(listTerm).toList
        }

        import Conversions.given
        directions.map(_.map(s => s: Direction))

      case _ =>
        println("Planner not fully configured (missing init, goal, or maxMoves)")
        None

object PlannerBuilder:
  def apply(): PlannerBuilder = new PlannerBuilder()

@main def testPlannerBuilder(): Unit =
  val pathOpt = PlannerBuilder()
    .withTheoryFrom("src/main/prolog/basePlanner.pl")
    .withInit(0, 0)
    .withGoal(2, 2)
    .withMaxMoves(5)
    .run()

  pathOpt match
    case Some(path) => println(s"Found path: ${path.mkString(" -> ")}")
    case None => println("No solution found.")
