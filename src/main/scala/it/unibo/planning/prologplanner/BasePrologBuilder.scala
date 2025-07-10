package it.unibo.planning.prologplanner

import alice.tuprolog.Term
import it.unibo.model.Direction
import it.unibo.model.Tiling.*
import it.unibo.planning.prologplanner.MoveFactsGenerator.generateMoveRules

import scala.io.Source
import scala.util.Using

trait BasePrologBuilder:
  protected object InitPos:
    def unapply(init: (Int, Int)): Option[String] = Some(s"init(s(${init._1}, ${init._1})).")

  protected object Goal:
    def unapply(goal: (Int, Int)): Option[String] = Some(s"goal(s(${goal._1}, ${goal._2})).")

  protected object MaxMoves:
    def unapply(maxMovesOpt: Option[Int]): Option[Term] = maxMovesOpt match
      case Some(maxMoves) => Some(Term.createTerm(s"plan(P, $maxMoves)"))
      case None => Some(Term.createTerm("plan(P, M)"))

  protected object Theory:
    def unapply(pathOpt: Option[String]): Option[String] =
      pathOpt.flatMap { path =>
        Using(Source.fromFile(path))(_.mkString).toOption
      }

  protected object Tiles:
    def unapply(tiles: List[Tile]): Option[String] = Some( tiles.collect {
      case s: Special => s"passable(s(${s.x}, ${s.y})).\nspecial(s(${s.x}, ${s.y}), s(${s.newPos.x}, ${s.newPos.y}))."
      case p: Passage => s"passable(s(${p.x}, ${p.y}))."
      // case o: Obstacle => "" // s"blocked(${o.x}, ${o.y})."
    }.mkString("\n"))

  protected object Directions:
    def unapply(directions: List[Direction]): Option[String] = Some(generateMoveRules(directions))