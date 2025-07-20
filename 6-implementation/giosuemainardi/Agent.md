[Index](../index.md)

La classe `Agent` rappresenta il componente centrale del sistema di pathfinding, responsabile dell'esecuzione della 
navigazione e del tracciamento del percorso.

L'Agent infatti utilizza la **mixin composition** di Scala per combinare le due responsabilità distinte:
- **PathManager**: Gestisce il percorso effettivamente seguito dall'agent
- **PlanManager**: Gestisce il piano di esecuzione e la navigazione tra le direzioni

```scala
class Agent(val initialPosition: Position, plan: () => Plan, getTileAt: Position => Option[Tile])
extends PathManager
with PlanManager
```

L'Agent riceve le sue dipendenze attraverso il costruttore:
- `plan: () => Plan`: Funzione a zero argomenti per ottenere un piano di navigazione (supplier)
- `getTileAt: Position => Option[Tile]`: Funzione per accedere alla destinazione delle tile di teletrasporto nel mondo

Questo approccio garantisce **separazione delle responsabilità** e **testabilità**.

Tra le funzionalità principali, la prima è la gestione della posizione: quella iniziale, ma anche quella corrente, 
compresi metodi per l'accesso e il reset.

Il metodo `step()` orchestra un singolo passo di movimento:
1. Registra la posizione e direzione correnti nel percorso
2. Esegue il movimento nella direzione successiva
3. Controlla eventuali tile speciali, che, eventualmente, modificano la posizione dopo l'esecuzione di un comando

```scala
def step(): Unit = 
  addToPath(_pos, currentDirection)
  this computeCommand nextDirection

def computeCommand(direction: Direction): Unit =
  _pos = _pos + direction.vector
  checkSpecial()
```

La ricerca del piano viene fatta partire evocando il supplier ottenuto come parametro del costruttore, il quale solo ora
viene effettivamente costruito, avendolo ricevuto by-value ma sottoforma di `Function0[Plan]`
```scala
def searchPlan: Option[Int] = plan() match
  case SucceededPlanWithMoves(directions, nMoves) =>
    currentPlan = directions
    Some(nMoves)
  case SucceededPlan(directions) =>
    currentPlan = directions
    None
  case FailedPlan(error) =>
    currentPlan = Seq.empty
    throw PlanNotFoundException(error)
```


Il metodo gestisce i diversi risultati della pianificazione attraverso **pattern matching**:
- **SucceededPlanWithMoves**: Piano trovato con numero di mosse utilizzate
- **SucceededPlan**: Piano trovato senza stima delle mosse, perchè specificato il massimo in input
- **FailedPlan**: Nessun piano trovato


