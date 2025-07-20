# Requirements

## 1. Requisiti di Business
- 1.1. L'applicazione deve fungere da strumento interattivo per osservare il comportamento di diversi algoritmi di pathfinding in diversi scenari.

## 2. Modello di Dominio
### Glossario
- **Agent**: Entità che si muove all'interno della griglia seguendo un piano (`plan`) per raggiungere un obiettivo (`goal`).
- **Goal**: Cella obiettivo che l'agente deve raggiungere all'interno della griglia.
- **Grid**: Rappresentazione bidimensionale dello spazio, suddivisa in celle (`tiles`), su cui si muove l’agente.
- **Obstacle**: Tipo di tile non attraversabile dall’agente (esempi: Wall, Trap, Water, Lava, Rock).
- **Passage**: Tipo di tile attraversabile liberamente dall’agente (esempi: Floor, Grass).
- **Pathfinding**: Processo mediante il quale viene calcolato un percorso ottimale da uno stato iniziale al `goal`, utilizzando algoritmi specifici.
- **Plan**: Sequenza di azioni o direzioni che l’agente deve seguire per raggiungere il `goal`.
- **Planner**: Entità che si occupa di calcolare il `plan`, ovvero il percorso ottimale per l’agente.
- **Scenario**: Contesto completo della simulazione, che comprende la griglia (`grid`), la posizione iniziale dell’agente e il `goal`.
- **Special**: Tipo di tile attraversabile con effetti particolari (esempi: Teleport, Arrow).
- **Tile**: Unità base della griglia. Ogni tile può appartenere a una delle seguenti categorie: `Passage`, `Obstacle` o `Special`.

## 3. Requisiti Funzionali

### 3.1. Utente 
- 3.1.1. L’utente deve poter scegliere quale algoritmo di ricerca utilizzare tramite una dropdown list (Dijkstra, A*, Greedy Best‑first, ecc.).
- 3.1.2. L'utente deve poter visualizzare la griglia di celle della simulazione.
- 3.1.3. Sulla griglia devono essere presenti il punto di partenza e il goal.
- 3.1.4. L'utente deve poter selezionare quale metodologia di generazione dello scenario l'applicazione deve utilizzare, tramite una dropdown list (Terrain, Maze, Trap).
- 3.1.5. L’utente deve poter generare un nuovo scenario casuale tramite un pulsante di refresh.
- 3.1.6. L’utente deve poter avviare o mettere in pausa la simulazione tramite un pulsante “Start/Stop”.
- 3.1.7. L’utente deve poter avanzare di un singolo passo nella simulazione tramite un pulsante “Step”.
- 3.1.8. L’utente deve poter resettare la simulazione allo stato iniziale tramite un pulsante “Reset”.
- 3.1.9. L’utente deve poter abilitare o disabilitare singole direzioni di movimento (cardinali e diagonali).
- 3.1.10. L’utente deve poter spostare interattivamente la posizione iniziale (“start”) o la destinazione (“goal”) cliccando sulla griglia, selezionando prima l’opzione “Change start” o “Change goal”.
- 3.1.11. L’utente deve ricevere popup informative o di errore in caso di eventi significativi o anomalie.

### 3.2. Di sistema
- 3.2.1. L’applicazione deve implementare funzioni di controllo della simulazione come “Start”, “Stop”, “Reset”, “Clear”, ecc.
- 3.2.2. L’applicazione deve implementare diversi algoritmi di ricerca del percorso (Dijkstra, A*, Greedy Best‑first, ecc.).
- 3.2.3. L’applicazione deve essere in grado di generare uno scenario in base alla tipologia di generazione scelta dall’utente.
- 3.2.4. Il sistema deve disegnare la griglia con le celle colorate in base al tipo di tile (Passage, Obstacle, Special).
- 3.2.5. Il sistema deve visualizzare l’agente e il goal come cerchi colorati (blu per l’agente, rosso per il goal).
- 3.2.6. Il sistema deve sovrapporre al pannello di disegno la sequenza di frecce che rappresentano il percorso calcolato dal planner.
- 3.2.7. Il sistema deve permettere soltanto i movimenti nelle direzioni abilitate dall’utente e sui tile attraversabili.
- 3.2.8. Il sistema deve gestire correttamente gli stati della simulazione (Running, Paused, Step, Reset, ChangeScenario, ChangeAlgorithm).
- 3.2.9. Il sistema deve invocare il controller per calcolare un nuovo piano ogni volta che l’utente cambia scenario o algoritmo.
- 3.2.10. Il sistema deve abilitare o disabilitare i pulsanti “Step”, “Reset”, “Start/Stop” e “Refresh Scenario” in base allo stato corrente della simulazione.
- 3.2.11. Il sistema deve intercettare e processare tutti gli eventi GUI (click su pulsanti, selezione dropdown, click sulla griglia) senza bloccare il thread dell’interfaccia.
- 3.2.12. L’applicazione deve tener conto, durante la ricerca del percorso, della tipologia di agente selezionato dall’utente.

## 4. Requisiti non Funzionali
- 4.1. Il sistema deve garantire una code coverage di almeno 50%.
- 4.2. I test devono essere eseguiti automaticamente all’interno della pipeline CI/CD.
- 4.3. L’esecuzione automatica dei test non deve superare i 5 minuti.
- 4.4. Il sistema deve fallire la build in caso di fallimento di uno o più test.

## 5. Requisiti di implementazione
- 5.1. Il progetto deve essere sviluppato utilizzando Scala 3.
- 5.2. Deve essere utilizzato il framework Scala Swing per l’interfaccia grafica.
- 5.3. Il progetto deve seguire un’architettura a livelli separando model, view e controller.
- 5.4. Gli algoritmi di pathfinding devono essere separati in componenti riutilizzabili.
- 5.5. Ogni modulo deve essere facilmente testabile tramite test di unità.
- 5.6. Deve essere possibile cambiare l’algoritmo di pathfinding senza modificare l’interfaccia controller.
- 5.7. Ogni nuova direzione aggiunta al sistema deve essere automaticamente gestita dall'algoritmo di pathfinding senza modificare il codice esistente.



| [Previous Chapter](../2-development_process/index.md) | [Index](../index.md) | [Next Chapter](../4-architectural_design/index.md) |

