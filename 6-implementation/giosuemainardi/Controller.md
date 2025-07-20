[Index](../index.md)

Al centro dell'architettura del controller troviamo `ScalaPathController`, un object singleton che funge da
orchestratore principale, implementazione di diversi trait che, attraverso il meccanismo dei mixin, permette di comporre modularmente le
funzionalità del sistema.
Questo componente non è semplicemente un aggregatore di funzionalità, ma rappresenta il **punto di convergenza** di tutte
le responsabilità del sistema.
La sua natura di singleton garantisce che ci sia sempre un'unica fonte di verità per lo stato della simulazione.

```scala
object ScalaPathController
  extends DisplayableController
  with SpeedManager
  with ViewManager:

  def start(): Unit = loop(Simulation.current)
```
### Main Loop
Il controller implementa un **main loop reattivo** che monitora continuamente i cambiamenti di stato attraverso il 
meccanismo di `Simulation.current`. 
```scala
@tailrec
private def loop(previous: State): Unit =
  val current = Simulation.current
  Simulation.exec:
    handleTransition(previous, current)
    handleState(current)
  delay()
  loop(current)
```
Questa architettura event-driven permette al sistema di reagire in tempo reale alle interazioni dell'utente e agli 
eventi della simulazione, mantenendo sempre sincronizzata l'interfaccia utente con lo stato interno.

Si può dire che il main loop in implementa un pattern **Observer-Controller** ibrido: invece di registrare listener, 
il controller fa "polling" attivamente sullo stato, ma lo fa in modo intelligente, reagendo
solo ai **cambiamenti significativi**. 

Questo approccio bilancia semplicità di implementazione con responsività del sistema.

La separazione tra `handleTransition` e `handleState` riflette due diverse filosofie di processing:
- **Transizioni**: azioni che devono avvenire quando si passa da uno stato all'altro
- **Stati**: azioni continue che caratterizzano un particolare stato oppure un evento singolo

Questa distinzione rende il sistema molto più predicibile e debuggable.

#### Simulation State

L'oggetto `Simulation` rappresenta una delle scelte architetturali più interessanti del sistema. 
Utilizza un **state machine pattern** sofisticato che definisce chiaramente le transizioni possibili tra i diversi stati 
della simulazione. 
Gli stati sono modellati attraverso enum e sealed trait, garantendo type safety e completezza nell'handling dei casi.

```scala
sealed trait State

enum ExecutionState extends State:
  case Running
  case Paused(fromUser: Boolean = false)
  case Step
  case Empty

enum UICommand extends State:
  case ChangeScenario(scenarioIndex: Int)
  case ChangeAlgorithm(algorithmIndex: Int)
  case DirectionsChoice(directions: Seq[Direction])
  case SetPosition(toSet: SettablePosition)
  case SetAnimationSpeed(speed: Double)
  case SetScenarioSize(nRows: Int, nCols: Int)
```

L'accesso allo stato corrente avviene attraverso una variabile `volatile` che può essere acceduta e modificata solo in maniera sincrona:
```scala
  @volatile private var _current: State = Empty

def current: State = synchronized{_current}

def set(s: State): Unit = synchronized{_current = s}

def exec(action: => Unit): Unit = synchronized{action}
```

Compare pure un metodo `exec` per garantire l'esecuzione di parti di codice senza che il Simulation state sia toccato

La presenza di **custom extractors** come `Resume()`, `Pause()`, `FirstStep()` trasforma il pattern matching da semplice
controllo di uguaglianza a un sistema di riconoscimento di transizioni semanticamente significative. 
```scala
object Resume:
  def unapply(t: Transition): Boolean = -?-> { case (Empty | Paused(_), Running) => true }(t)

object Pause:
  def unapply(t: Transition): Boolean = -?-> { case (Running, Paused(true)) => true }(t)

object ChangeSpeed:
  def unapply(t: Transition): Option[(State, Double)] = 
    ~=~> { case (prev: State, SetAnimationSpeed(s)) => Some(prev, s)}(t)
// altri...
```
Questo approccio rende il codice nel main loop estremamente leggibile e manutenibile, grazie anche all'uso del modulo 
creato `PartialFunctionExtension` che comprende:
```scala
def -?->[T](pred: PartialFunction[T, Boolean]): T => Boolean = 
  t => (pred isDefinedAt t) && pred(t)

def ~=~>[I, O](extractor: PartialFunction[I, Option[O]]): I => Option[O] =
  t => if extractor isDefinedAt t then extractor(t) else None
```
### DisplayableController
`DisplayableController` è l'interfaccia che il controller mostra alla View e incarna il principio di 
**composizione over inheritance** aggregando quattro manager specializzati:

- **ScenarioManager**: gestisce la creazione e configurazione dei diversi tipi di scenario (Terrain, Maze, Specials)
- **AlgorithmManager**: mantiene la selezione dell'algoritmo di planning attivo
- **DirectionManager**: controlla le direzioni di movimento disponibili per l'agente
- **AgentManager**: collega al dominio d'esecuzione del controller

Il punto forte di questa struttura sta nel fatto che ogni manager è completamente indipendente dagli altri, permettendo 
modifiche ed estensioni senza effetti collaterali.

#### ScenarioManager

Il **ScenarioManager** gestisce tutti gli aspetti dello scenario di simulazione, inclusa la selezione del tipo di mappa, 
le dimensioni e le posizioni di partenza e destinazione dell'agente.

```scala
trait ScenarioManager:
  private val scenarioMap: ListMap[String, (Int, Int) => Scenario] = ListMap(
    "Terrain" -> Terrain.apply,
    "Maze" -> Maze.apply,
    "Specials" -> Specials.apply
  )
  
  private var _scenario: Scenario = EmptyScenario(_nRows, _nCols)
  private var _init: Position = randomPosition
  private var _goal: Position = randomPosition
```

Il trait mantiene una mappa dei tipi di scenario disponibili e gestisce lo stato corrente del scenario, inizialmente 
impostato sull'`EmptyScenario`, scegliendo casualmente le posizioni iniziale e di destinazione dell'agente. 

Il metodo `generateScenario()` si occupa della generazione procedurale del contenuto, 
mentre `resizeScenario()` permette di modificare dinamicamente le dimensioni della griglia.

#### AlgorithmManager

L'**AlgorithmManager** fornisce l'interfaccia per la selezione e gestione degli algoritmi di pathfinding disponibili nel sistema.

```scala
trait AlgorithmManager:
  val algorithms: Seq[Algorithm] = Algorithm.values.toList
  private var _algorithm: Algorithm = algorithms.head

  def algorithm: Algorithm = _algorithm
  protected def algorithm_=(newAlgorithm: Algorithm): Unit = _algorithm = newAlgorithm
```

Il manager mantiene un riferimento all'algoritmo attualmente selezionato e offre metodi per accedere alla lista completa 
degli algoritmi disponibili. 
La selezione dell'algoritmo influenza direttamente la strategia di ricerca utilizzata durante la fase di planning dell'agente.

#### DirectionManager

Il **DirectionManager** controlla le direzioni di movimento consentite all'agente durante il pathfinding, permettendo di limitare o personalizzare le opzioni di spostamento.

```scala
trait DirectionManager:
  private var _directions: Seq[Direction] = allDirections

  def directions: Seq[Direction] = _directions
  protected def directions_=(directions: Seq[Direction]): Unit = _directions = directions
```

Di default, tutte le direzioni disponibili sono abilitate, ma il controller può restringere il set di movimenti permettendo, ad esempio, solo movimenti cardinali o escludendo direzioni specifiche. 
Questa configurazione viene utilizzata durante la costruzione del planner per definire lo spazio di ricerca.

### AgentManager: Il Ponte verso il Dominio

`AgentManager` rappresenta l'interfaccia tra il controller e il domain model degli agenti. 

#### Implementazione parziale
La sua implementazione è particolarmente interessante perché utilizza il pattern **Template Method**:
```scala
trait AgentManager:
  private var _agent: Option[Agent] = None

  def agent: Option[Agent] = _agent
  def agent_=(agent: Agent): Unit = _agent = Some(agent)

  protected def planOver: Boolean = _agent match
    case Some(a) => a.planOver
    case None    => true

  protected def searchPlan(): Unit =
    startSearch()
    _agent match 
      case Some(agent) =>
        try handleValidPlan(agent.searchPlan)
        catch
          case e: Exception => handleNoPlan(e.getMessage)
      case None => throw AgentNotBuiltException
```
Il flusso generale delle operazioni è definito (search → handle result) ma lascia alle implementazioni concrete la 
definizione della gestione dei risultati specifici.


#### Implementazione concreta: Processo di assemblaggio
Nell'implementazione concreta è richiesto l'assemblaggio dell'agente, avviato dalla selezione dell'algoritmo di ricerca. 
Questo processo avviene attraverso il metodo `assembleAgent()`, il quale si occupa di raccogliere tutti i parametri 
configurati per creare un planner specializzato.
Il planner si trasforma nell'agente con all'interno un piano non ancora computato ma che potrà essere utilizzato per
calcolare il percorso ottimale quando necessario.
```scala
  def assembleAgent(): Unit =
    agent =
      PlannerBuilder.start
        .withInit(init)
        .withGoal(goal)
        .withMaxMoves(None)
        .withTiles(scenario)
        .withDirections(directions)
        .withAlgorithm(algorithm)
        .build
        .toAgent
```

### ViewManager: Separazione UI/Logic

`ViewManager` implementa una **clean separation** tra logica di controllo e presentazione. 

```scala
trait ViewManager[V <: ControllableView]:
  private var _view: Option[V] = None

  final def attachView(v: V): Unit = _view = Some(v)
  
  protected def exec(op: => Unit): Unit = onEDT(op)

  final private def applyToView(viewAction: V => Unit): Unit = exec:
    _view foreach viewAction
```
Al suo interno ha l'oggetto `View` che nasconde i dettagli dell'implementazione Swing e fornisce un'interfaccia semantica ad alto livello.

```scala
    
  protected object View:
    def update(): Unit = applyToView: v =>
      v.repaint()

    def disableControls(): Unit = applyToView: v =>
      v.disableStepButton()
      v.disableResetButton()
      v.disableStartStopButton()

    def pause(): Unit = applyToView: v =>
      v.enableStepButton()
  
    // altri...
```

La gestione thread-safe attraverso `onEDT` assicura che tutti gli aggiornamenti dell'interfaccia avvengano nel thread 
corretto, prevenendo race conditions tipiche delle applicazioni GUI.

### SpeedManager: Controllo Temporale

`SpeedManager` racchiude una gestione particolare del timing della simulazione. 

```scala
trait SpeedManager:
  private val _delay: Int = 400
  private var _speed: Double = 1.0
  private var _shouldSleep: Boolean = false

  def delay(): Unit =
    if _shouldSleep then
      _shouldSleep = false
      Thread sleep (_delay * _speed).toLong
```

La separazione tra il **delay fisso** e il **moltiplicatore di velocità** permette un controllo granulare dell'animazione, 
mentre il flag `shouldSleep` implementa un meccanismo di **lazy delay** che introduce pause solo quando necessario.
