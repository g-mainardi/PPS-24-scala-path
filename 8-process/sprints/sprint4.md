# Sprint 4


| Priority |     Item      |                  Tasks                   | Assignee | Initial Size Estimate | Day 1 | Day 2 | Day 3 | Day 4 | Day 5 | Day 6 | Day 7 |
|:--------:|:-------------:|:----------------------------------------:|:--------:|:---------------------:|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|
|    1     |      GUI      |           Configuration Menus            | Basigli  |           6           |   6   |   4   |   2   |   0   |   0   |   0   |   0   |
|    2     |  Controller   |      Handling Configuration Inputs       | Mainardi |           4           |   3   |   2   |   1   |   0   |   0   |   0   |   0   |
|    3     |     Model     |         Model & Agents Refactor          |  Salman  |           6           |   4   |   2   |   0   |   0   |   0   |   0   |   0   |
|    4     |  Controller   | Sugar Refactor for Controller Structure  | Mainardi |           4           |   4   |   3   |   2   |   1   |   0   |   0   |   0   |
|    5     |     Test      |     ASCII art DSL to tests scenarios     | Basigli  |           4           |   4   |   4   |   4   |   2   |   0   |   0   |   0   |
|    6     |     Model     | Specials Adaptation to Scenario Refactor |  Salman  |           4           |   4   |   4   |   2   |   0   |   0   |   0   |   0   |
|    7     | Documentation |                Scala Doc                 |   Team   |          10           |  10   |  10   |   7   |   4   |   0   |   0   |   0   |
|    8     | Documentation |               Final Report               |   Team   |          10           |  10   |  10   |   7   |   4   |   0   |   0   |   0   |
|    9     |     Test      |           Controller Managers            | Mainardi |           3           |   3   |   3   |   3   |   2   |   1   |   0   |   0   |

### Sprint Goal

Start Date: 14/7
<br/>
End Date: 20/7

In questo sprint finale puntiamo a completare il Controller e la GUI, aggiungendo la possibilità di configurare lo Scenario e l’Agent.
È inoltre necessario rifattorizzare e ampliare il concetto di Agent, finora trascurato.
Sono previsti alcuni sugar refactor nel Controller, tempo permettendo.
Infine, vogliamo costruire un DSL in stile ASCII-art per testare la generazione degli scenari.

### Sprint Review

In questo sprint siamo riusciti ad aggiungere molte funzionalità alla GUI e alla corrispondente gestione nel controller.
Abbiamo introdotto la possibilità di scegliere le direzioni, le dimensioni dello scenario e le posizioni di inizio (init) e obiettivo (goal).
L’oggetto Scenario è stato suddiviso in parti diverse, poiché aveva troppe responsabilità, come la gestione delle posizioni init e goal.
Il concetto di Agent è stato rifattorizzato ed è ora un’entità più autonoma, non più incapsulata in altri oggetti.
Il Planner è stato adattato per restituire un agente che include il piano da eseguire.

### Sprint Retrospective

Abbiamo imparato che, dopo molti refactor nei sprint precedenti, il codice è ora più flessibile ed è stato facile aggiungere nuove funzionalità.
Infatti, questo è stato probabilmente il sprint con il maggior numero di funzionalità aggiunte.
Abbiamo lavorato sul codice quasi fino alla fine, perché c’erano sempre correzioni o piccoli miglioramenti da fare.
Non è stato ottimale arrivare all'ultimo sprint con molte piccole funzionalità da aggiungere, alcuni cambiamenti importanti avremmo potuto farli prima di questo sprint. Non ci siamo accorti che alcune classi mantengono ancora un design vecchio che andava aggiornato.

[Previous Sprint](sprint3.md) | [Index](../index.md)
