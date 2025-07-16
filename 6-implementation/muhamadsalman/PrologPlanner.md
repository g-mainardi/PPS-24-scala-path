# PrologPlanner
Il Prolog planner è un'implementazione concreta del planner che utilizza Prolog per calcolare un piano da init a goal.
Il planner si occupa anche di fare il parsing del risultato in output da Prolog e trasformarlo in una lista di mosse in Scala.

Utilizza delle unapply su degli Optional per controllare sia completamente confgiurato prima di costruire il planner.
Utilizza invece delle given per convertire le mosse prodotte da Prolog in oggetti Directions in Scala.

Prima di tutto controllare se Prolog ha effettivamente trovato un piano: 
```scala
  def checkSolutions(solutions: LazyList[SolveInfo], maxMoves: Option[Int]): Plan = solutions match
      case solveInfo #:: _ if solveInfo.isSuccess => convertToPlan(solveInfo, maxMoves)
      case _ => FailedPlan("No valid plan found")
```

Dopodiché fa il parsing del risultato per trasformarlo in una lista di Directions, 
utilizza una given per fare implicitamente la conversione da Term a Direction.
```scala
  private def convertToPlan(solveInfo: SolveInfo, maxMoves: Option[Int]): Plan =
    import Conversions.given
    val listTerm: Term = extractTerm(solveInfo, "P")
    val directions: List[Direction] = extractListFromTerm(listTerm).toList map (s => s: Direction)

object Conversions:
    given Conversion[String, Direction] with
    def apply(s: String): Direction =
      Try(Cardinals valueOf s.capitalize) getOrElse (Diagonals valueOf s.capitalize)
```

Infine controlla se è da estrapolare anche il parametro fully-relational MaxMoves, nel caso non fosse stato specificato in input: 
```scala
maxMoves match
  case None =>
    val movesTerm: Term = extractTerm(solveInfo, "M")
    SucceededPlanWithMoves(directions, movesTerm.toString.toInt)
  case _ => SucceededPlan(directions)
```

[Index](../index.md)