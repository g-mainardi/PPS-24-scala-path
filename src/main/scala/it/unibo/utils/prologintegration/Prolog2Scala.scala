package it.unibo.utils.prologintegration

import alice.tuprolog.{SolveInfo, Struct, Term, Theory, Var}
import it.unibo.model.fundamentals.Direction.{Cardinals, Diagonals}
import it.unibo.model.fundamentals.Direction
import Scala2Prolog.Engine

object Prolog2Scala:
  given Conversion[Term, String] = _.toString
  given Conversion[Seq[_], Term] = s => Term createTerm s.mkString("[", ",", "]")
  
  extension (engine: Engine)
    def solveWithSuccess(goal: Term): Boolean = engine(goal).map(_.isSuccess).headOption contains true
    def solveOneAndGetTerm(goal: Term, term: String): Term = engine(goal).headOption.map(extractTerm(_, term)).get

  def extractTerm(solveInfo: SolveInfo, i: Integer): Term =
    (solveInfo.getSolution.asInstanceOf[Struct] getArg i).getTerm

  def extractTerm(solveInfo: SolveInfo, s: String): Term =
    solveInfo getTerm s

  def extractListFromTerm(t: Term): LazyList[String] = t match
    case struct: Struct if struct.getName == "." =>
      LazyList((struct getArg 0).toString) ++ extractListFromTerm(struct getArg 1)
    case v: Var => extractListFromTerm(v.getTerm)
    case _ => LazyList.empty

  extension (solutions: LazyList[SolveInfo])
    def extractSolutionsOf(target: String): LazyList[String] = solutions map (extractTerm(_, s"$target"))

  def getEnum(s: String): Direction =
    try
      Cardinals valueOf s
    catch
      case e: IllegalArgumentException =>
        Diagonals valueOf s

  @main
  def testProlog2Scala(): Unit =
    println:
      getEnum("rightUp")