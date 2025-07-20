# Sprint 2

| Priority |      Item       |                          Tasks                          | Assignee | Initial Size Estimate | Day 1 | Day 2 | Day 3 | Day 4 | Day 5 | Day 6 | Day 7 |
|:--------:|:---------------:|:-------------------------------------------------------:|:--------:|:---------------------:|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|
|    1     |   Controller    |    Controller Design pt2 (Dividing State Management)    | Mainardi |           6           |   4   |   2   |   0   |   0   |   0   |   0   |   0   |
|    2     |      View       | View Design (Handling different button press sequences) | Basigli  |           6           |   4   |   2   |   2   |   2   |   2   |   2   |   0   |
|    3     |     Planner     |                   Planner with Tiles                    |  Salman  |           4           |   2   |   0   |   0   |   0   |   0   |   0   |   0   |
|    4     |     Builder     |               Planner Builder Abstraction               |  Salman  |           2           |   2   |   2   |   2   |   2   |   0   |   0   |   0   |
|    5     |     Planner     |                     A* Search Algo                      | Basigli  |           8           |   8   |   8   |   6   |   4   |   4   |   2   |   0   |
|    6     |     Planner     |                     Dj Search Algo                      | Mainardi |           8           |   8   |   8   |   6   |   4   |   4   |   0   |   0   |
|    7     |   Integration   |       Planner MaxMove Refactor (Fully-Relational)       |  Salman  |           2           |   2   |   2   |   0   |   0   |   0   |   0   |   0   |
|    8     |      Test       |           Test for View, Controller, Planner            |   All    |           2           |   2   |   2   |   2   |   0   |   0   |   0   |   0   |
|    9     |  Documentation  |          Documenting View, Controller, Planner          |   All    |           2           |   2   |   2   |   2   |   2   |   0   |   0   |   0   |


### Sprint Goal
Start Date: 30/6/25
<br/>
End Date: 6/7/25

L'obiettivo dello sprint è finalizzare la progettazione del Controller e della View, e implementare un Planner in grado di gestire ostacoli e celle (Tile).
Inoltre, puntiamo ad aggiungere due algoritmi principali per il pathfinding (BFS e A*).
Se possibile, vogliamo introdurre variabilità e flessibilità nella generazione degli Scenario, in modo da ottenere diverse varianti degli scenari di tipo Maze, Terrain e Traps.

### Sprint Review
Abbiamo effettuato molti refactoring per migliorare il design complessivo, rendendolo più flessibile, estensibile e modulare.
Abbiamo dedicato molto tempo a discutere le scelte di design, clean code e diversi stili per ottenere maggiore concisione.
Nel complesso, abbiamo raffinato il codice sviluppato nel primo sprint, creando una base più solida per i prossimi passi.
Abbiamo realizzato un PlannerBuilder esteso e modulare, e abbiamo aggiunto una prima implementazione parziale di due ulteriori algoritmi di pathfinding: A* e BFS.

### Sprint Retrospective
Abbiamo scoperto che il codice può sempre diventare più pulito ed elegante, e il design può sempre essere reso più modulare e flessibile;
di conseguenza, il miglioramento e il refactoring sono un processo senza fine.


[Previous Sprint](sprint1.md) | [Index](../index.md) | [Next Sprint](sprint3.md)

