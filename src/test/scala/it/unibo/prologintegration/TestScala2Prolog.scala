package it.unibo.prologintegration

import alice.tuprolog.{SolveInfo, Term, Theory}
import it.unibo.prologintegration.Scala2Prolog.*
import it.unibo.prologintegration.Prolog2Scala.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.{File, PrintWriter}

class TestScala2Prolog extends AnyFlatSpec with Matchers:

  "mkPrologEngine" should "solve a simple fact from theory" in:
    val theory = Theory("hello(world).")
    val engine = mkPrologEngine(theory)
    val goal = Term.createTerm("hello(world)")
    engine(goal).headOption.exists(_.isSuccess) shouldBe true

  it should "fail if goal doesn't match theory" in:
    val theory = Theory("hello(world).")
    val engine = mkPrologEngine(theory)
    val goal = Term.createTerm("hello(moon)")
    engine(goal).headOption.exists(_.isSuccess) shouldBe false

  it should "support recursive rules like member/3" in:
    val theory = Theory("""
      member([H|T], H, T).
      member([H|T], E ,[H|T2]) :- member(T, E, T2).
    """)
    val engine: Engine = mkPrologEngine(theory)
    val goal: Term = Term.createTerm("member([a,b,c], b, T)")
    val solutions = engine(goal)
    val tailTerm = solutions.headOption.map(extractTerm(_, "T")).get
    val elements = extractListFromTerm(tailTerm).toList
    elements should contain ("c")

  "mkPrologEngineFromFile" should "load a theory from file and execute it" in:
    val theoryStr =
      """
        test(x).
        test(y).
      """
    val file = File.createTempFile("testTheory", ".pl")
    val writer = new PrintWriter(file)
    writer.write(theoryStr)
    writer.close()

    val engine = mkPrologEngineFromFile(file.getAbsolutePath)
    val goal = Term.createTerm("test(x)")
    engine(goal).headOption.exists(_.isSuccess) shouldBe true

    file.delete()

  it should "throw an exception when the file is not found" in:
    an [Exception] should be thrownBy mkPrologEngineFromFile("nonexistent-file.pl")

