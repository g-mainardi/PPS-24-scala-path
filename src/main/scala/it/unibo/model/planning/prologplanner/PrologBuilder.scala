package it.unibo.model.planning.prologplanner

import alice.tuprolog.{Term, Theory}
import it.unibo.model.fundamentals.Tiling.*
import it.unibo.model.exceptions.FailedPlannerBuildException
import it.unibo.model.fundamentals.{Direction, Position}
import it.unibo.model.planning.{Configuration, Plan, Planner, PrologPlanner}
import it.unibo.model.scenario.Scenario
import it.unibo.utils.prologintegration.Scala2Prolog.{Engine, mkPrologEngine}

import scala.io.Source
import scala.util.Using

/**
 * Builds a Prolog-based planner using a configuration and a theory file.
 * It converts an implicit configuration into a prolog theory.
 * It then initializes a tuProlog engine and returns a ready-to-use {@code PrologPlanner}.
 *
 * This builder relies on pattern matching extractors defined as private objects
 * (e.g., {@code InitPos}, {@code Tiles}, {@code Theory}) for modular and readable code.
 */
class PrologBuilder(using configuration: Configuration):

  /**
   * Builds a {@code PrologPlanner} from the provided configuration.
   * It extracts all required Prolog terms and source code fragments,
   * combines them into a unified theory, and initializes the Prolog engine.
   * Throws {@code FailedPlannerBuildException} if configuration is invalid.
   *
   * @return a new instance of {@code PrologPlanner}
   */
  def build: Planner = configuration match
    case Configuration(InitPos(initFact), Goal(goalFact), MaxMoves(goalTerm), Tiles(tileFacts), Directions(directionsFact), Theory(theoryString), _) =>
      val fullTheory = new Theory(s"$initFact\n$goalFact\n$directionsFact\n$tileFacts\n$theoryString")
      println(s"\n$fullTheory\n")
      val engine = mkPrologEngine(fullTheory)
      PrologPlanner(engine, goalTerm)
    case _ => throw FailedPlannerBuildException

  /**
   * Extractor objects used to transform configuration fields into Prolog facts or terms.
   *
   * These extractors convert:
   * - Init position into `init(...)` fact
   * - Goal position into `goal(...)` fact
   * - Max moves into a `plan(P, M)` Prolog term
   * - Scenario tiles into `passable(...)` and `special(...)` facts
   * - Direction list into `delta(...)` clauses plus a general move clause
   * - Theory path into the contents of a `.pl` file
   *
   * They are used to deconstruct the configuration via pattern matching in the `build` method.
   */
  private object InitPos:
    def unapply(init: Position): Option[String] = Some(s"init(s(${init.x}, ${init.y})).")

  private object Goal:
    def unapply(goal: Position): Option[String] = Some(s"goal(s(${goal.x}, ${goal.y})).")

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
    }.mkString("\n"))

  private object Directions:
    def unapply(directions: List[Direction]): Option[String] =
      Some(generateDeltaClauses(directions))

  private def toCamelCase(name: String) =
    name.head.toLower + name.tail

  private def generateDeltaClauses(directions: List[Direction]) =
    val deltaClauses = directions.map {
      case d =>
        val Position(dx, dy) = d.vector
        val name = toCamelCase(d.toString)
        s"delta($name, $dx, $dy)."
    }.distinct.mkString("\n")
    val moveClause = Using(Source.fromFile("src/main/prolog/moveClause.pl"))(_.mkString).get
    s"$deltaClauses\n\n$moveClause"