package it.unibo.planning.prologplanner

import alice.tuprolog.{SolveInfo, Term, Theory}
import it.unibo.model.Direction
import it.unibo.model.Direction.{Cardinals, Diagonals}
import it.unibo.model.Tiling.*
import it.unibo.planning.Plan.{FailedPlan, SucceededPlan, SucceededPlanWithMoves}
import it.unibo.planning.prologplanner.MoveFactsGenerator.generateMoveRules
import it.unibo.planning.{Plan, PrologBuilder}
import it.unibo.prologintegration.Prolog2Scala.*
import it.unibo.prologintegration.Scala2Prolog.*

import scala.io.Source
import scala.util.Try

object Conversions:
  given Conversion[String, Direction] with
    def apply(s: String): Direction =
      Try(Cardinals valueOf s.capitalize) getOrElse (Diagonals valueOf s.capitalize)

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
      case s: Special => s"passable(${s.x}, ${s.y}).\nspecial(s(${s.x}, ${s.y}), s(${s.newPos.x}, ${s.newPos.y}))."
      case p: Passage => s"passable(${p.x}, ${p.y})."
      case o: Obstacle => s"blocked(${o.x}, ${o.y})."
    }.mkString("\n"))

  private object Directions:
    def unapply(o: Option[List[Direction]]): Option[String] = o map { directions =>
      generateMoveRules(directions)
    }

  object IncompletePlannerConfig:
    def unapply(config: (Option[(Int, Int)], Option[(Int, Int)], Option[String], Option[List[Tile]], Option[List[Direction]])): Option[String] =
      val labels = List("init position", "goal", "theory", "environmental tiles", "possible directions")
      val options = config.toList
      labels.zip(options).collectFirst {
        case (label, None) => s"missing $label"
      }

  override def run: Plan = (initPos, goalPos, theoryPath, environmentTiles, directions) match
    case IncompletePlannerConfig(reason) => FailedPlan(s"Planner not fully configured, $reason")
    case (InitPos(initFact), Goal(goalFact), Theory(theoryString), Tiles(tileFacts), Directions(directionsFact)) =>
      val fullTheory = new Theory(s"$initFact\n$goalFact\n$directionsFact\n$tileFacts\n$theoryString")
      println(s"\n$fullTheory\n")
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
        SucceededPlanWithMoves(directions, movesTerm.toString.toInt)
      case _ => SucceededPlan(directions)

object BasePrologPlannerBuilder:
  def apply(): BasePrologPlannerBuilder = new BasePrologPlannerBuilder()
