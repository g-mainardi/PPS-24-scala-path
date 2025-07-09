package it.unibo.planning.prologplanner

import alice.tuprolog.{SolveInfo, Term, Theory}
import it.unibo.model.Direction
import it.unibo.model.Direction.{Cardinals, Diagonals}
import it.unibo.model.Tiling.*
import it.unibo.planning.Plan.{FailedPlan, SucceededPlan, SucceededPlanWithMoves}
import it.unibo.planning.prologplanner.MoveFactsGenerator.generateMoveRules
import it.unibo.planning.{Plan}
import it.unibo.prologintegration.Prolog2Scala.*
import it.unibo.prologintegration.Scala2Prolog.*

import scala.io.Source
import scala.util.Try

trait BasePrologBuilder:
  protected object InitPos:
    def unapply(o: Option[(Int, Int)]): Option[String] = o map ((ix, iy) => s"init(s($ix, $iy)).")

  protected object Goal:
    def unapply(o: Option[(Int, Int)]): Option[String] = o map ((gx, gy) => s"goal(s($gx, $gy)).")

  protected object MaxMoves:
    def unapply(o: Option[Int]): Option[String] = o match
      case Some(maxMoves) => Some(s"maxmoves($maxMoves).")
      case None => Some("maxmoves(100).")

  protected object Theory:
    def unapply(o: Option[String]): Option[String] = o map (theoryPath => Source.fromFile(theoryPath).mkString)

  protected object Tiles:
    def unapply(o: Option[List[Tile]]): Option[String] = o map (tiles => tiles.collect {
      case s: Special => s"passable(s(${s.x}, ${s.y})).\nspecial(s(${s.x}, ${s.y}), s(${s.newPos.x}, ${s.newPos.y}))."
      case p: Passage => s"passable(s(${p.x}, ${p.y}))."
      // case o: Obstacle => "" // s"blocked(${o.x}, ${o.y})."
    }.mkString("\n"))

  protected object Directions:
    def unapply(o: Option[List[Direction]]): Option[String] = o map { directions =>
      generateMoveRules(directions)
    }
