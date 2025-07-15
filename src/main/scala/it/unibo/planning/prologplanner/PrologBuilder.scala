package it.unibo.planning.prologplanner

import alice.tuprolog.{Term, Theory}
import it.unibo.model.{Agent, Direction, Scenario}
import it.unibo.model.Tiling.*
import it.unibo.planning.{Configuration, Plan, PrologPlanner}
import it.unibo.planning.prologplanner.MoveFactsGenerator.generateMoveRules
import it.unibo.prologintegration.Scala2Prolog.{Engine, mkPrologEngine}

import scala.io.Source
import scala.util.Using

import it.unibo.planning.prologplanner.Conversions.given_Conversion_Int_Int_Position

class PrologBuilder(configuration: Configuration):
  def build: Agent = configuration match
    case Configuration(InitPos(initFact), Goal(goalFact), MaxMoves(goalTerm), Tiles(tileFacts), Directions(directionsFact), Theory(theoryString), _) =>
      val fullTheory = new Theory(s"$initFact\n$goalFact\n$directionsFact\n$tileFacts\n$theoryString")
      println(s"\n$fullTheory\n")
      val engine: Engine = mkPrologEngine(fullTheory)
      val plan: Plan = PrologPlanner(engine, goalTerm, configuration.maxMoves).plan
      new Agent (
        configuration.initPos,
        plan,
        configuration.environmentTiles.checkSpecial,
      )
    case _ => throw new IllegalArgumentException("Invalid configuration for PrologBuilder") 
    
  private object InitPos:
    def unapply(init: (Int, Int)): Option[String] = Some(s"init(s(${init._1}, ${init._1})).")

  private object Goal:
    def unapply(goal: (Int, Int)): Option[String] = Some(s"goal(s(${goal._1}, ${goal._2})).")

  private object MaxMoves:
    def unapply(maxMovesOpt: Option[Int]): Option[Term] = maxMovesOpt match
      case Some(maxMoves) => Some(Term.createTerm(s"plan(P, $maxMoves)"))
      case None => Some(Term.createTerm("plan(P, M)"))

  private object Theory:
    def unapply(pathOpt: Option[String]): Option[String] =
      pathOpt.flatMap { path =>
        Using(Source.fromFile(path))(_.mkString).toOption
      }

  private object Tiles:
    def unapply(scenario: Scenario): Option[String] = Some( scenario.tiles.collect {
      case s: Special => s"passable(s(${s.x}, ${s.y})).\nspecial(s(${s.x}, ${s.y}), s(${s.newPos.x}, ${s.newPos.y}))."
      case p: Passage => s"passable(s(${p.x}, ${p.y}))."
      // case o: Obstacle => "" // s"blocked(${o.x}, ${o.y})."
    }.mkString("\n"))

  private object Directions:
    def unapply(directions: List[Direction]): Option[String] =
      Some(generateDeltaClauses(directions))
      //Some(generateMoveRules(directions))

  private def toCamelCase(name: String): String =
    name.head.toLower + name.tail

  private def generateDeltaClauses(directions: List[Direction]): String =
    val deltaClauses = directions.map {
      case d =>
        val Position(dx, dy, visited) = d.vector
        val name = toCamelCase(d.toString)
        s"delta($name, $dx, $dy)."
    }.distinct.mkString("\n")
    val moveClause = Using(Source.fromFile("src/main/prolog/moveClause.pl"))(_.mkString).get
    s"$deltaClauses\n\n$moveClause"