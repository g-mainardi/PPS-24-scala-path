
## PrologBuilder
Il `PrologBuilder` è la classe che concretamente assembla il Planner in Prolog con le configurazioni raccolte tramite il Planner Builder.

Queste configurazioni vengono spacchettate ed tradotte nei corrispondenti predicati e fatti in Prolog, generando dinamicamente la teoria. 
Ad esempio, per init e goal vengono generati dei fatti `init(s(X, Y))` e `goal(s(X, Y))` in Prolog.

```Scala
private object InitPos:
  def unapply(init: (Int, Int)): Option[String] = Some(s"init(s(${init._1}, ${init._2})).")

private object Goal:
  def unapply(goal: (Int, Int)): Option[String] = Some(s"goal(s(${goal._1}, ${goal._2})).")
```

Lo scenario invece viene definito in Prolog come una serie di fatti che descrivono le celle percorribili e quelle speciali.
```Scala
private object Tiles:
    def unapply(scenario: Scenario): Option[String] = Some( scenario.tiles.collect {
      case s: Special => s"passable(s(${s.x}, ${s.y})).\nspecial(s(${s.x}, ${s.y}), s(${s.newPos.x}, ${s.newPos.y}))."
      case p: Passage => s"passable(s(${p.x}, ${p.y}))."
    }.mkString("\n"))
```

Le direzioni selezionate vengono convertite in predicati Prolog che rappresentano i movimenti possibili attraverso delle delta sulla posizione.
```Scala
private object Directions:
    def unapply(directions: List[Direction]): Option[String] =
      Some(generateDeltaClauses(directions))

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
```

Infine il dato MaxMoves è fully-relational in Prolog, quindi determina come viene generata la query: 
```Scala
  private object MaxMoves:
    def unapply(maxMovesOpt: Option[Int]): Option[Term] = maxMovesOpt match
    case Some(maxMoves) => Some(Term.createTerm(s"plan(P, $maxMoves)"))
    case None => Some(Term.createTerm("plan(P, M)"))
```
Alla fine il Builder assembra la teoria utilizando tutte queste unapply: 
```Scala
  def build: Planner = configuration match
    case Configuration(InitPos(initFact), Goal(goalFact), MaxMoves(goalTerm), Tiles(tileFacts), Directions(directionsFact), Theory(theoryString), _) =>
      val fullTheory = new Theory(s"$initFact\n$goalFact\n$directionsFact\n$tileFacts\n$theoryString")
      println(s"\n$fullTheory\n")
      val engine: Engine = mkPrologEngine(fullTheory)
      PrologPlanner(engine, goalTerm, configuration.maxMoves)
    case _ => throw FailedPlannerBuildException
```

[Index](../index.md)