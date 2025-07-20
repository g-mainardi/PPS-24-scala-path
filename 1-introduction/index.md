# Introduzione
Lo scopo di questo progetto è quello di realizzare una applicazione che permette di simulare la ricerca di percorsi all'interno di diversi tipi di scenari costituiti da una griglia (grid), ovvero una rappresentazione bidimensionale dello spazio suddiviso in celle (tiles). 
Una cella può essere di diversi tipi: 
- Passage: tile attraversabile dall’agente (es. Floor, Grass).
- Obstacle: tile non attraversabile (es. Wall, Trap, Water, Lava, Rock).
- Special: tile attraversabile con effetti particolari (es. Teleport, Arrow).

Nella griglia è presente una entità (agent), che si muove seguendo un piano (plan) fornito da una entità che calcola il percorso ottimale (planner). A questo scopo il planner fornisce il plan in base a diversi algoritmi di ricerca del percorso (pathfinding). L'obbiettivo dell'agente è quello di raggiungere una determinata cella (goal).

 [Index](../index.md) | [Next Chapter](../2-development_process/index.md) |
