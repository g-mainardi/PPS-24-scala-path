package it.unibo.planning

import alice.tuprolog.{SolveInfo, Term, Theory}
import Plan.*
import it.unibo.model.Tiling.*
import it.unibo.model.{Cardinals, Diagonals, Direction}
import it.unibo.prologintegration.Prolog2Scala.*
import it.unibo.prologintegration.Scala2Prolog.*

import scala.io.Source
import scala.util.Try

object Conversions:
  given Conversion[String, Direction] with
    def apply(s: String): Direction =
      Try(Cardinals.valueOf(s.capitalize)).getOrElse(Diagonals.valueOf(s.capitalize))

  given Conversion[(Int, Int), Position] = Position(_, _)

  given Conversion[Position, (Int, Int)] = p => (p.x, p.y)

class BasePrologPlannerBuilder extends PrologBuilder:
  private object InitPos:
    def unapply(o: Option[(Int, Int)]): Option[String] = o map ((ix, iy) => s"init(s($ix, $iy)).")
  private object Goal:
    def unapply(o: Option[(Int, Int)]): Option[String] = o map ((gx, gy) => s"goal(s($gx, $gy)).")
  private object Theory:
    def unapply(o: Option[String]): Option[String] = o map (theoryPath => Source.fromFile(theoryPath).mkString)
  private object Tiles:
    def unapply(o: Option[List[Tile]]): Option[String] = o map (tiles => tiles.map {
      case p: Passage => s"passable(${p.x}, ${p.y})."
      case o: Obstacle => s"blocked(${o.x}, ${o.y})."
    }.mkString("\n"))

  override def run: Plan = (initPos, goalPos, theoryPath, environmentTiles) match
    case (None, _, _, _) => FailedPlan("Planner not fully configured, missing init position")
    case (_, None, _, _) => FailedPlan("Planner not fully configured, missing goal")
    case (_, _, None, _) => FailedPlan("Planner not fully configured, missing theory")
    case (_, _, _, None) => FailedPlan("Planner not fully configured, missing environmental tiles")
    case (InitPos(initFact), Goal(goalFact), Theory(theoryString), Tiles(tileFacts)) =>
      val fullTheory = new Theory(s"$initFact\n$goalFact\n$tileFacts\n$theoryString")
      val engine: Engine = mkPrologEngine(fullTheory)
      val goal = maxMoves match
        case None => Term.createTerm(s"plan(P, M)")
        case Some(moves) => Term.createTerm(s"plan(P, $moves)")
      checkSolutions(engine(goal))

  private def checkSolutions(solutions: LazyList[SolveInfo]): Plan = solutions match
    case solveInfo #:: _ if solveInfo.isSuccess => convertToPlan(solveInfo)
    case _ => FailedPlan("No valid plan found")

  private def convertToPlan(solveInfo: SolveInfo): Plan =
    import Conversions.given
    val listTerm: Term = extractTerm(solveInfo, "P")
    val directions: List[Direction] = extractListFromTerm(listTerm).toList map (s => s: Direction)
    maxMoves match
      case None =>
        val movesTerm: Term = extractTerm(solveInfo, "M")
        SucceededPlanWithMaxMoves(directions, movesTerm.toString.toInt)
      case _ => SucceededPlan(directions)

object BasePrologPlannerBuilder:
  def apply(): BasePrologPlannerBuilder = new BasePrologPlannerBuilder()
