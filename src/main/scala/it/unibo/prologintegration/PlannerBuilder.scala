package it.unibo.prologintegration

import alice.tuprolog.{SolveInfo, Term, Theory}
import it.unibo.model.Tiling.*
import it.unibo.prologintegration.Scala2Prolog.*
import it.unibo.prologintegration.Prolog2Scala.*

import scala.util.Try
import scala.io.Source
import it.unibo.model.{Cardinals, Diagonals, Direction, FailedPlan, Plan, SucceededPlan}

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
  private var maxMoves: Int = 0

  def withTheoryFrom(path: String): PlannerBuilder =
    this.theoryStr += Source.fromFile(path).mkString
    this

  def withInit(initPos: (Int, Int)): PlannerBuilder =
    this.initPos = Some(initPos)
    this

  def withGoal(goal: (Int, Int)): PlannerBuilder =
    this.goalPos = Some(goal)
    this

  def withMaxMoves(maxMoves: Int): PlannerBuilder =
    this.maxMoves = maxMoves
    this

  def withTiles(tiles: List[Tile]): PlannerBuilder =
    val tileFacts = tiles.map {
      case p: Passage => s"passable(${p.x}, ${p.y})."
      case o: Obstacle => s"blocked(${o.x}, ${o.y})."
    }.mkString("\n")
    this.theoryStr += s"\n$tileFacts\n"
    this

  private object InitPos:
    def unapply(o: Option[(Int, Int)]): Option[String] = o map ((ix, iy) => s"init(s($ix, $iy)).")
    
  private object Goal:
    def unapply(o: Option[(Int, Int)]): Option[String] = o map ((ix, iy) => s"goal(s($ix, $iy)).")
  
  def run: Plan = (initPos, goalPos) match
    case (None, _) | (_, None) =>
      println("Planner not fully configured (missing init or goal)")
      FailedPlan()
    case (InitPos(initFact), Goal(goalFact)) =>
      val fullTheory = new Theory(s"$initFact\n$goalFact\n$theoryStr")
      val engine: Engine = mkPrologEngine(fullTheory)

      val goal = Term.createTerm(s"plan(P, M)")
      val solutions: LazyList[SolveInfo] = engine(goal)
      convertToPlan(solutions)
  
  private def convertToPlan(solutions: LazyList[SolveInfo]): Plan =
    import Conversions.given
    solutions.headOption.get match
      case solveInfo if !solveInfo.isSuccess => FailedPlan()
      case solveInfo if solveInfo.isSuccess =>
        val listTerm: Term = extractTerm(solveInfo, "P")
        val movesTerm: Term = extractTerm(solveInfo, "M")
        val directions: List[String] = extractListFromTerm(listTerm).toList
        SucceededPlan(directions.map(s => s: Direction), movesTerm.toString.toInt)

object PlannerBuilder:
  def apply(): PlannerBuilder = new PlannerBuilder()

@main def testPlannerBuilder(): Unit =
  import Conversions.given
  val pathOpt = PlannerBuilder()
    .withTheoryFrom("src/main/prolog/plannerWithMaxMoves.pl")
    .withInit(0, 0)
    .withGoal(2, 2)
    .withTiles(List(
      Floor(Position(0, 0)),
      Grass(Position(0, 1)),
      Floor(Position(1, 1)),
      Floor(Position(2, 2))
    ))
    .run

//  pathOpt match
//    case Some(path) => println(s"Found path: ${path.mkString(" -> ")}")
//    case None => println("No solution found.")
