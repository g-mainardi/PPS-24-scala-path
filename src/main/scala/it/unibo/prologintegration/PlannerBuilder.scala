package it.unibo.prologintegration

import alice.tuprolog.{Term, Theory}
import it.unibo.model.Tiling.*
import it.unibo.prologintegration.Scala2Prolog.*
import it.unibo.prologintegration.Prolog2Scala.*

import scala.util.Try
import scala.io.Source
import it.unibo.model.{Cardinals, Diagonals, Direction}

object Conversions:
  given Conversion[String, Direction] with
    def apply(s: String): Direction =
      Try(Cardinals.valueOf(s.capitalize)).getOrElse(Diagonals.valueOf(s.capitalize))

  given Conversion[(Int, Int), Position] = Position(_, _)
  given Conversion[Position, (Int, Int)] = p => (p.x, p.y)

class PlannerBuilder:
  private var theoryStr: String = ""
  private var initPos: Option[(Int, Int)] = None
  private var goalPos: Option[(Int, Int)] = None
  private var maxMoves: Option[Int] = None

  def withTheoryFrom(path: String): PlannerBuilder =
    this.theoryStr += Source.fromFile(path).mkString
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

  def withTiles(tiles: List[Tile]): PlannerBuilder =
    if tiles.isEmpty then
      println("No tiles provided, skipping tile facts generation.")
      return this

    var tileFacts =  tiles.map {
      case p: Passage => s"passable(${p.x}, ${p.y})."
      case o: Obstacle => s"blocked(${o.x}, ${o.y})."
    }.mkString("\n")
    tileFacts = tileFacts + "\n"

    // println(s"$tileFacts\n")
    this.theoryStr += s"\n$tileFacts"
    this

  def run: Option[List[Direction]] =
    (initPos, goalPos, maxMoves) match
      case (Some((ix, iy)), Some((gx, gy)), Some(moves)) =>
        val initFact = s"init(s($ix, $iy))."
        val goalFact = s"goal(s($gx, $gy))."

        println(s"$initFact\n$goalFact\n$theoryStr\n")

        val fullTheory = new Theory(s"$initFact\n$goalFact\n$theoryStr")

        val engine: Engine = mkPrologEngine(fullTheory)
        val goal = Term.createTerm(s"plan(P, $moves)")
        val solutions = engine(goal)

        val directions: Option[List[String]] = solutions.headOption.map { info =>
          val listTerm = extractTerm(info, "P")
          println("Term list: " + listTerm)
          extractListFromTerm(listTerm).toList
        }

        println("Directions list: " + directions)

        import Conversions.given
        directions.map(_.map(s => s: Direction))

      case _ =>
        println("Planner not fully configured (missing init, goal, or maxMoves)")
        None

object PlannerBuilder:
  def apply(): PlannerBuilder = new PlannerBuilder()

@main def testPlannerBuilder(): Unit =
  import Conversions.given
  val pathOpt = PlannerBuilder()
    .withTheoryFrom("src/main/prolog/plannerWithMaxMoves.pl")
    .withInit(0, 0)
    .withGoal(2, 2)
    .withMaxMoves(5)
    .withTiles(List(
      Floor(Position(0, 0)),
      Grass(Position(0, 1)),
      Floor(Position(1, 1)),
     Floor(Position(2, 2))
    ))
    .run

  pathOpt match
    case Some(path) => println(s"Found path: ${path.mkString(" -> ")}")
    case None => println("No solution found.")
