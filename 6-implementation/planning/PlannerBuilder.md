# Planner Builder
Planner Builder è un modulo che consente di configurare e costruire un planner per generare percorsi nello scenario. 
Il planner accumula come configurazioni la posizione iniziale, il goal, lo scenario ed eventualmente il numero massimo di mosse. 

Il builder controllare di essere completamente configurato prima di costruire il planner o lanciare la pianificazione. 
In caso contrario, ritorna un FailedPlan con un messaggio di errore.

Il builder viene poi specializzato per costruire i diversi tipi di planner, che siano in Prolog o in Scala.

## BasePlannerBuilder
Il `BasePlannerBuilder` è un'implementazione di base del planner che consente di
costruire un planner Prolog, generando dinamicamente i fatti che descrivono lo scenario.
Dopodiché si occupa di fare il parsing del risultato in output da Prolog.

Utilizza delle unapply su degli Optional per controllare che il planner sia completamente confgiurato prima di costruire il planner. 
Utilizza invece delle given per convertire le mosse prodotte da Prolog in oggetti Directions in Scala.