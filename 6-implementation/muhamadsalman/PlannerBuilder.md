# Planner Builder
Planner Builder è un modulo che consente di configurare e costruire un planner per generare percorsi nello scenario. 
Il planner accumula come configurazioni la posizione iniziale, il goal, lo scenario ed eventualmente il numero massimo di mosse. 

Il builder controlla di essere completamente configurato prima di costruire il planner o lanciare la pianificazione. 
In caso contrario, ritorna un FailedPlan con un messaggio di errore.
Il builder viene poi specializzato per costruire i diversi tipi di planner, che siano in Prolog o in Scala.


### Method Ordering
Per obbligare l'utilizzatore a configurare il planner in modo corretto, il builder è composto da una gerarchia di interfacce
che obbligano a chiamare i metodi in un certo ordine.

Ogni interfaccia rappresenta un passo nella configurazione del planner, quando riceve in input il dato
ritorna come tipo l'interfaccia dello step successivo, e così via fino al completamento.

```scala
trait BuilderInit:
    protected var initPos: (Int, Int) = (0, 0)
    def withInit(initPos: (Int, Int)): BuilderGoal

trait BuilderGoal:
    protected var goalPos: (Int, Int) = (0,0)
    def withGoal(goal: (Int, Int)): BuilderConstraints

trait BuilderConstraints:
    protected var maxMoves: Option[Int] = None
    def withMaxMoves(maxMoves: Option[Int]): BuilderEnvironment

trait BuilderEnvironment:
    protected var environmentTiles: Scenario = _
    def withTiles(scenario: Scenario): BuilderDirections

trait BuilderDirections:
    protected var directions: List[Direction] = List.empty
    def withDirections(directions: List[Direction]): BuilderAlgorithm

trait BuilderAlgorithm:
    protected var algorithm: Algorithm = BFS
    def withAlgorithm(algorithm: Algorithm): CompleteBuilder

trait CompleteBuilder:
    def build: Agent
```

Alla fine la creazione del Planner con la configurazione concreta viene delegata ad un Builder specifico, 
come `PrologBuilder` o `ScalaBuilder`, che implementano il metodo `build`.
```Scala
case class Configuration(initPos: (Int, Int),
                         goalPos: (Int, Int),
                         maxMoves: Option[Int] = None,
                         environmentTiles: Scenario,
                         directions: List[Direction],
                         theoryPath: Option[String] = None,
                         algorithm: Option[PathFindingAlgorithm] = None)
def build: Planner =
    val configuration: Configuration = Configuration (
    initPos,
    goalPos,
    maxMoves,
    environmentTiles,
    directions)

    algorithm match
      case DFS => new PrologBuilder(configuration.copy(theoryPath = Some(theoryPaths(DFS)))).build
      case BFS => new PrologBuilder(configuration.copy(theoryPath = Some(theoryPaths(BFS)))).build
      case AStar => new ScalaBuilder(configuration.copy(algorithm = Some(AStarAlgorithm))).build
```

[Index](../index.md)