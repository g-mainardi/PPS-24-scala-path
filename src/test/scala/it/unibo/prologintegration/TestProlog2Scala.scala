package it.unibo.prologintegration

import alice.tuprolog._
import it.unibo.model.Direction
import it.unibo.model.Direction.{Cardinals, Diagonals}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import it.unibo.prologintegration.Prolog2Scala._
import it.unibo.prologintegration.Scala2Prolog.mkPrologEngine
import it.unibo.prologintegration.Scala2Prolog.Engine

class TestProlog2Scala extends AnyFlatSpec with Matchers:

  "extractListFromTerm" should "convert a Prolog list term into a Scala LazyList" in:
    val listTerm = Term.createTerm("[up, right, down]")
    val extracted = extractListFromTerm(listTerm).toList
    extracted shouldBe List("up", "right", "down")

  "getEnum" should "convert a cardinal direction string to Cardinals enum" in:
    getEnum("Up") shouldBe Cardinals.Up

  it should "convert a diagonal direction string to Diagonals enum" in:
    getEnum("RightDown") shouldBe Diagonals.RightDown

  "solveWithSuccess" should "return true if the goal is solvable" in:
    val theory = new Theory("a(1). a(2).")
    val engine: Engine = mkPrologEngine(theory)
    val goal = Term.createTerm("a(2)")
    engine.solveWithSuccess(goal) shouldBe true

  "solveOneAndGetTerm" should "extract the correct variable term from the first solution" in:
    val theory = new Theory("b(hello).")
    val engine: Engine = mkPrologEngine(theory)
    val goal = Term.createTerm("b(X)")
    val term = engine.solveOneAndGetTerm(goal, "X")
    term.toString shouldBe "hello"

  "extractSolutionsOf" should "collect all variable bindings from multiple solutions" in:
    val theory = new Theory("f(x). f(y). f(z).")
    val engine: Engine = mkPrologEngine(theory)
    val goal = Term.createTerm("f(X)")
    val solutions = engine(goal)
    val results = solutions.extractSolutionsOf("X")
    results.toList should contain theSameElementsAs List("x", "y", "z")

