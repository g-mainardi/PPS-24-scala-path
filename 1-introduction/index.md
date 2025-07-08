# Introduction
Lo scopo di questo progetto è quello di realizzare una applicazione che permette di simulare la ricerca di percorsi all'interno di diversi scenari costituiti da una griglia (grid), ovvero una rappresentazione bidimensionale dello spazio suddiviso in celle (tiles). 
Una cella può essere di diversi tipi: 
- Passage: tile attraversabile dall’agente (es. Floor, Grass).
- Obstacle: tile non attraversabile (es. Wall, Trap, Water, Lava, Rock).
- Special: tile attraversabile con effetti particolari (es. Teleport, Arrow).

Nella griglia è presente una entità (agent), che si muove seguendo un piano (plan) fornito da una entità che calcola il percorso ottimale (planner). L'obbiettivo dell'agente è quello di raggiungere una determinata cella (goal).
La configurazione della griglia, la posizione iniziale dell'agente e il goal sono racchiuse in una sola entità chiamata scenario.

| [Index](../index.md) | [Next Chapter](../2-development_process/index.md) |
