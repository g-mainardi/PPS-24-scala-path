# Sprint 1

| Priority |         Item          |                        Tasks                         | Assignee | Initial Size Estimate | Day 1 | Day 2 | Day 3 | Day 4 | Day 5 | Day 6 | Day 7 |
|:--------:|:---------------------:|:----------------------------------------------------:|:--------:|:---------------------:|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|
|    1     | Project configuration |                   Repository Setup                   |   Team   |           3           |   0   |   0   |   0   |   0   |   0   |   0   |   0   |
|    2     |    Project design     |             Requirements & Architecture              |   Team   |           4           |   2   |   0   |   0   |   0   |   0   |   0   |   0   |
|    3     |     Domain Model      |                 Domain Model Design                  |   Team   |           4           |   2   |   0   |   0   |   0   |   0   |   0   |   0   |
|    4     |      Controller       |                  Controller Design                   | Mainardi |           7           |   7   |   7   |   4   |   4   |   4   |   0   |   0   |
|    5     |         View          |                     View Design                      | Basigli  |           7           |   7   |   7   |   4   |   4   |   4   |   0   |   0   |
|    6     |     Base Planner      |          Base Planner Design & Integration           |  Salman  |           7           |   7   |   7   |   4   |   4   |   4   |   0   |   0   |
|    7     |         Maze          |               Maze Scenario Generation               |  Salman  |           4           |   4   |   4   |   4   |   2   |   0   |   0   |   0   |
|    8     |        Terrain        |             Terrain Scenario Generation              | Basigli  |           4           |   4   |   4   |   4   |   2   |   0   |   0   |   0   |
|    9     |         Traps         |              Traps Scenario Generation               | Mainardi |           4           |   4   |   4   |   4   |   2   |   0   |   0   |   0   |



### Sprint Goal
Start Date: 23/6
<br/>
End Date: 29/6

L'obiettivo dello sprint è innanzitutto configurare il repository del progetto, lo strumento di build e la CI.
Successivamente, puntiamo a progettare l'architettura e il modello in modo che tutti siano allineati.
Inoltre, lo sprint ha l'obiettivo di progettare una View, un Controller e un Planner di base che saranno utilizzati come base comune.
Infine, miriamo a costruire la logica centrale per la generazione dei diversi Scenario.

### Sprint Review
Abbiamo configurato con successo il repository del progetto, lo strumento di build e la CI.
Abbiamo svolto molte sessioni di pair programming all'inizio per progettare il modello e l'architettura, dopodiché abbiamo suddiviso il lavoro in: (i) Controller, (ii) View, (iii) Planner di base.
Una volta ottenuta una base comune per la generazione degli Scenario, abbiamo suddiviso ulteriormente il lavoro in: (i) Maze, (ii) Terrain, (iii) Traps.
È stata realizzata una prima versione funzionante del Controller e della View, utilizzando uno Scenario fittizio.
È stata inoltre completata una prima versione dell’integrazione Scala-Prolog, utilizzando un Planner di esempio.

### Sprint Retrospective
Abbiamo sperimentato il pair programming e lo abbiamo trovato molto utile nella fase iniziale di un progetto. Finora ci ha permesso di essere tutti sulla stessa lunghezza d’onda.
Avere una base comune ci ha anche aiutato a suddividere meglio il lavoro e le responsabilità.

[Index](../index.md) | [Next Sprint](sprint2.md)
