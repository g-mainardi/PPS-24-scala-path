# L'algoritmo A*
Nel contesto di questo progetto, mi sono occupato personalmente dell’implementazione di un algoritmo di pathfinding utilizzando esclusivamente il linguaggio Scala, a differenza dei miei colleghi che si sono invece concentrati sull’approccio in Prolog. L’algoritmo scelto è A*, uno dei più efficienti algoritmi euristici per la ricerca del percorso più breve.
Un aspetto interessante dell’implementazione è il metodo neighbors, che si occupa di determinare tutte le posizioni adiacenti a una posizione data. Questo metodo riceve in input l’elenco delle direzioni che l’agente può percorrere, offrendo così una maggiore flessibilità nel definire il comportamento di movimento.
Un dettaglio rilevante riguarda la gestione delle Tile speciali (come teletrasporti o frecce direzionali): se durante l’analisi delle celle adiacenti viene incontrata una di queste, oltre alla cella adiacente viene aggiunta alla lista dei vicini anche la posizione di destinazione (newPos) associata alla Tile speciale.

```scala
def neighbors(pos: Position, directions: Seq[Direction], tiles: Seq[Tile]): Seq[Position] =
  directions.flatMap { dir =>
    val candidate = pos + dir.vector
    tiles.find(t => t.x == candidate.x && t.y == candidate.y) match
      case Some(s: Special) => Seq(s.newPos)
      case Some(_)          => Seq(candidate)
      case _                => Seq.empty
  }
```
Questo comportamento garantisce che il grafo esplorato da A* includa anche i "salti" all’interno della griglia, come quelli indotti dalle Tile speciali. In questo modo, l’algoritmo continua a funzionare in modo del tutto trasparente, senza richiedere modifiche aggiuntive alla logica di calcolo del percorso.
[Index](../index.md)
