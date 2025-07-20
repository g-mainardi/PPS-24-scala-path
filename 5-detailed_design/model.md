## Model

Il model racchiude i concetti ed entità principali che rappresetano il dominio della simulazione: 
1. **Direction**: rappresenta i possibili versi in cui un agente si può muovere, ad esempio quelle cardinali e quelle diagonali.
2. **Position**: rappresenta una posizione nella griglia, può essere riferita ad una casella o ad un agente.
3. **Tile**: rappresenta una casella della griglia, ce ne possono essere di diverso tipo, sia percorribili che ostacoli.
4. **Scenario**: rappresenta un insieme di tile che formano una griglia, il tiling può essere di diverso tipo ad esempio labirinto o terreno.
5. **Agent**: entità attiva con uno stato interno che si muove all'interno dello scenario. 

Il comportameno dell'agent è influenzato dal tipo di algoritmo di pathfinding selezionato e dal tipo di scenario in cui si trova. Ha bisogno di queste informazioni per poter funzionare. Inoltre anche la scelta delle possibili direzioni di movimento avrà influenza sul piano calcolato ed eseguito dall'Agent.


<p align="center">
  <img src="../resources/model.png" alt="Model" title="Model" />
</p>

[Index](../index.md)