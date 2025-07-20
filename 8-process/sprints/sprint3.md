# Sprint 3

| Priority |     Item      |                Tasks                | Assignee | Initial Size Estimate | Day 1 | Day 2 | Day 3 | Day 4 | Day 5 | Day 6 | Day 7 |
|:--------:|:-------------:|:-----------------------------------:|:--------:|:---------------------:|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|
|    1     |    Planner    |             A* Testing              | Basigli  |           4           |   4   |   -   |   -   |   -   |   -   |   -   |   -   |
|    2     |    Planner    |           BFS Search Algo           | Mainardi |           2           |   5   |   3   |   1   |   0   |   0   |   0   |   0   |
|    3     |    Planner    |  Planner support for Special Tiles  |  Salman  |           2           |   0   |   0   |   0   |   0   |   0   |   -   |   -   |
|    4     |    Design     |     Mixing Different Scenarios      |  Salman  |           4           |   4   |   4   |   4   |   4   |   4   |   -   |   -   |
|    6     |    Design     |        Custom Special Tiles         |  Salman  |           4           |   4   |   2   |   0   |   -   |   -   |   -   |   -   |
|    7     |  Integration  |             A* Builder              | Basigli  |           2           |   2   |   -   |   -   |   -   |   -   |   -   |   -   |
|    8     |  Integration  |             BFS Builder             |  Salman  |           4           |   2   |   0   |   -   |   -   |   -   |   -   |   -   |
|    9     |  Controller   |           Buttons on/off            | Mainardi |           2           |   2   |   1   |   2   |   0   |   0   |   0   |   0   |
|    10    | Documentation |    Model, View, Control, Builder    |   Team   |           6           |   6   |   -   |   -   |   -   |   -   |   -   |   -   |
|    11    |     View      |     Agent and algorithm chooser     | Basigli  |           3           |   1   |   -   |   -   |   -   |   -   |   -   |   -   |
|    12    |  Controller   |    Agent and algorithm switcher     | Mainardi |           3           |   3   |   1   |   1   |   0   |   0   |   0   |   0   |
|    13    | Documentation | Requirements and domain explanation | Basigli  |           4           |   4   |   2   |   -   |   -   |   -   |   -   |   -   |
|    14    |    Builder    |    Refactor with multiple traits    |  Salman  |           6           |   6   |   6   |   4   |   2   |   0   |   -   |   -   |

### Sprint Goal
Start Date: 7/7/25
<br/>
End Date: 13/7/25

In questo sprint puntiamo a completare i due principali algoritmi di pathfinding: A* e BFS.
Dovremmo inoltre iniziare a discutere come strutturare i diversi agenti e come integrare i loro comportamenti.
Abbiamo in programma di ampliare il concetto di special tiles e aggiungere la possibilità di creare special personalizzati.
Vogliamo anche introdurre variabilità e flessibilità nella generazione degli scenari o nella configurazione del planner,
in modo da poter avere diverse varianti dello stesso scenario o differenti configurazioni per lo stesso algoritmo di pianificazione.

### Sprint Review
Abbiamo effettivamente trascorso la maggior parte del tempo a ridisegnare e rifattorizzare il codice, per renderlo più modulare e flessibile, in modo che l’implementazione di nuove funzionalità risulti più semplice.
Abbiamo dedicato tempo a correggere bug nell’interazione tra Controller e View dopo l’aggiunta di nuovi pulsanti e menu a tendina.
Il PlannerBuilder è stato completamente rifattorizzato per imporre un ordine nelle chiamate ai metodi e per migliorare la modularità nella creazione di sotto-builder.
L’algoritmo BFS è stato riprogettato per avere la stessa interfaccia dell’algoritmo DFS, con un parametro fully-relational.

### Sprint Retrospective
Time really flies when you refactor.

[Previous Sprint](sprint2.md) | [Index](../index.md) | [Next Sprint](sprint4.md)
