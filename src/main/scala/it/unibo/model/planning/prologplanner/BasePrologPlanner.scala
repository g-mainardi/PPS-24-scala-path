package it.unibo.model.planning.prologplanner

import alice.tuprolog.{SolveInfo, Term}
import it.unibo.model.fundamentals.Direction.{Cardinals, Diagonals}
import it.unibo.model.fundamentals.{Direction, Position}
import it.unibo.model.planning.{Configuration, Plan}
import it.unibo.model.planning.Plan.{FailedPlan, SucceededPlan, SucceededPlanWithMoves}
import it.unibo.utils.prologintegration.Prolog2Scala.{extractListFromTerm, extractTerm}

import scala.util.Try

trait BasePrologPlanner:
  def checkSolutions(solutions: LazyList[SolveInfo])(using configuration: Configuration): Plan = solutions match
      case solveInfo #:: _ if solveInfo.isSuccess => convertToPlan(solveInfo, configuration.maxMoves)
      case _ => FailedPlan("No valid plan found")

  private def convertToPlan(solveInfo: SolveInfo, maxMoves: Option[Int]): Plan =
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