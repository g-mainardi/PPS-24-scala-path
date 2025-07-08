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

trait BasePrologPlannerBuilder extends PrologBuilder:
  protected object InitPos:
    def unapply(o: Option[(Int, Int)]): Option[String] = o map ((ix, iy) => s"init(s($ix, $iy)).")

  protected object Goal:
    def unapply(o: Option[(Int, Int)]): Option[String] = o map ((gx, gy) => s"goal(s($gx, $gy)).")

  protected object Theory:
    def unapply(o: Option[String]): Option[String] = o map (theoryPath => Source.fromFile(theoryPath).mkString)

  protected object Tiles:
    def unapply(o: Option[List[Tile]]): Option[String] = o map (tiles => tiles.map {
      case s: Special => s"passable(${s.x}, ${s.y}).\nspecial(s(${s.x}, ${s.y}), s(${s.newPos.x}, ${s.newPos.y}))."
      case p: Passage => s"passable(${p.x}, ${p.y})."
      case o: Obstacle => s"blocked(${o.x}, ${o.y})."
    }.mkString("\n"))

  protected object Directions:
    def unapply(o: Option[List[Direction]]): Option[String] = o map { directions =>
      generateMoveRules(directions)
    }

class BasePrologPlannerConversions:
  def checkSolutions(solutions: LazyList[SolveInfo], maxMoves: Option[Int]): Plan = solutions match
    case solveInfo #:: _ if solveInfo.isSuccess => convertToPlan(solveInfo, maxMoves)
    case _ => FailedPlan("No valid plan found")

  def convertToPlan(solveInfo: SolveInfo, maxMoves: Option[Int]): Plan =
    import Conversions.given
    val listTerm: Term = extractTerm(solveInfo, "P")
    val directions: List[Direction] = extractListFromTerm(listTerm).toList map (s => s: Direction)
    maxMoves match
      case None =>
        val movesTerm: Term = extractTerm(solveInfo, "M")
        SucceededPlanWithMoves(directions, movesTerm.toString.toInt)
      case _ => SucceededPlan(directions)

    object Conversions:
      given Conversion[String, Direction] with
        def apply(s: String): Direction =
          Try(Cardinals valueOf s.capitalize) getOrElse (Diagonals valueOf s.capitalize)

      given Conversion[(Int, Int), Position] = Position(_, _)
      given Conversion[Position, (Int, Int)] = p => (p.x, p.y)
