package it.unibo.model.planning.prologplanner

import alice.tuprolog.{SolveInfo, Term}
import it.unibo.model.fundamentals.Direction.{Cardinals, Diagonals}
import it.unibo.model.fundamentals.{Direction, Position}
import it.unibo.model.planning.{Configuration, Plan}
import it.unibo.model.planning.Plan.{FailedPlan, SucceededPlan, SucceededPlanWithMoves}
import it.unibo.utils.prologintegration.Prolog2Scala.{extractListFromTerm, extractTerm}

import scala.util.Try

/**
 * Trait containing shared logic for Prolog-based planners.
 * It provides utilities to process the results of a Prolog query
 * and convert them into Scala-based Plan objects.
 */
trait BasePrologPlanner:
  /**
   * Converts the first successful Prolog solution into a Plan.
   * Returns a failed plan if no solution is found.
   *
   * @param solutions     lazy stream of Prolog SolveInfo results
   * @param configuration the implicit planning configuration
   * @return a succeeded or failed Plan
   */
  def checkSolutions(solutions: LazyList[SolveInfo])(using configuration: Configuration): Plan = solutions match
      case solveInfo #:: _ if solveInfo.isSuccess => convertToPlan(solveInfo, configuration.maxMoves)
      case _ => FailedPlan("No valid plan found")


  /**
   * Internal method to extract a list of directions (as a plan) from a SolveInfo.
   *
   * @param solveInfo the Prolog solution containing the plan
   * @param maxMoves  optional constraint on maximum allowed moves
   * @return a succeeded Plan with or without move count
   */
  private def convertToPlan(solveInfo: SolveInfo, maxMoves: Option[Int]): Plan =
    import Conversions.given
    val listTerm: Term = extractTerm(solveInfo, "P")
    val directions: Seq[Direction] = extractListFromTerm(listTerm).toList map (s => s: Direction)
    maxMoves match
      case None =>
        val movesTerm: Term = extractTerm(solveInfo, "M")
        SucceededPlanWithMoves(directions, movesTerm.toString.toInt)
      case _ => SucceededPlan(directions)

/**
 * Converts a string (from Prolog) into a Direction (Cardinal or Diagonal).
 */
object Conversions:
  given Conversion[String, Direction] with
    def apply(s: String): Direction =
      Try(Cardinals valueOf s.capitalize) getOrElse (Diagonals valueOf s.capitalize)

  given Conversion[(Int, Int), Position] = Position(_, _)
  given Conversion[Position, (Int, Int)] = p => (p.x, p.y)