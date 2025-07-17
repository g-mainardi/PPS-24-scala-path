package it.unibo.model.prologintegration

import alice.tuprolog.{Prolog, SolveInfo, Term, Theory}

import scala.io.Source

object Scala2Prolog:
  type Engine = Term => LazyList[SolveInfo]
  given Conversion[String, Theory] = new Theory(_)

  def mkPrologEngine(theory: Theory): Engine =
    val engine = Prolog()
    engine setTheory theory

    goal =>
      new Iterable[SolveInfo] {
        override def iterator: Iterator[SolveInfo] = new Iterator[SolveInfo]:
          var solution: Option[SolveInfo] = Some(engine solve goal)

          override def hasNext: Boolean = solution.isDefined &&
            (solution.get.isSuccess || solution.get.hasOpenAlternatives)

          override def next(): SolveInfo =
            try solution.get
            finally solution = if solution.get.hasOpenAlternatives then Some(engine.solveNext()) else None
      } to LazyList

  def mkPrologEngineFromFile(path: String): Engine =
    try
      val source = Source fromFile path
      val content = source.mkString
      source.close()
      mkPrologEngine(content)
    catch
      case e: Exception => println(s"Errore nel caricamento del file $path: ${e.getMessage}"); throw e
  
  @main def testLoadingTheory(): Unit = 
    val engine: Engine = mkPrologEngine("""
      member([H|T],H,T).
      member([H|T],E,[H|T2]):- member(T,E,T2).
      permutation([],[]).
      permutation(L,[H|TP]) :- member(L,H,T), permutation(T,TP).
    """)
  
    val goal = Term createTerm "permutation([1,2,3],L)"
    engine(goal) foreach println
    // permutation([1,2,3],[1,2,3]) ... permutation([1,2,3],[3,2,1])
