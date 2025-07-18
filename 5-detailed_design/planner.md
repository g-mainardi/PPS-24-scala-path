## Planner

Il Planner è un interfaccia che incapsula una configurazione ed un algoritmo di pathfinding.
La configurazione comprende tutte le informazioni legate allo scenario, alla posizione iniziale ed al goal.
Inoltre la configurazione determina anche quali movimenti può fare l'agente all'interno dello scenario. 
Ad esempio potrebbe venir configurato per muoversi solo in diagonale.

L'interfaccia Planner viene poi concretizzata dal delle classi che utilizzano effettivamente la configurazione 
per calcolare un piano di movimento. In base al risultato della ricerca ritornano un successo o un fallimento.

Il Planner offre poi la possibilit di creare un agente con una proprio stato interno 
che incapsula il piano calcolato dal Planner.

<p align="center">
  <img src="../resources/planner.png" alt="Planner" title="Planner" />
</p>