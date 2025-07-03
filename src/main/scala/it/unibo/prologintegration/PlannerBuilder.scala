package it.unibo.prologintegration

import alice.tuprolog.{SolveInfo, Term, Theory}
import it.unibo.model.Tiling.*
import it.unibo.prologintegration.Scala2Prolog.*
import it.unibo.prologintegration.Prolog2Scala.*

import scala.util.Try
import scala.io.Source
import it.unibo.model.{Cardinals, Diagonals, Direction, Plan}
import it.unibo.model.Plan.*

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

  def withMaxMoves(maxMoves: Option[Int]): PlannerBuilder =
    maxMoves match
      case Some(moves) => this.maxMoves = Math.abs(moves)
      case _ => this.maxMoves = 0
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
    case (None, _) | (_, None) => FailedPlan("Planner not fully configured (missing init or goal)")
    case (InitPos(initFact), Goal(goalFact)) =>
      val fullTheory = new Theory(s"$initFact\n$goalFact\n$theoryStr")
      val engine: Engine = mkPrologEngine(fullTheory)
      val goal = maxMoves match
        case 0 => Term.createTerm(s"plan(P, M)")
        case _ => Term.createTerm(s"plan(P, $maxMoves)")

      checkSolutions(engine(goal))

  private def checkSolutions(solutions: LazyList[SolveInfo]): Plan = solutions match
    case solveInfo #:: _ if solveInfo.isSuccess => convertToPlan(solveInfo)
    case _ => FailedPlan("No valid plan found")

  private def convertToPlan(solveInfo: SolveInfo): Plan =
    import Conversions.given
    val listTerm: Term = extractTerm(solveInfo, "P")
    val directions: List[Direction] = extractListFromTerm(listTerm).toList map(s => s: Direction)
    maxMoves match
      case 0 =>
        val movesTerm: Term = extractTerm(solveInfo, "M")
        SucceededPlan(directions, movesTerm.toString.toInt)
      case _ => SucceededPlanWithoutMaxMoves(directions)

object PlannerBuilder:
  def apply(): PlannerBuilder = new PlannerBuilder()
