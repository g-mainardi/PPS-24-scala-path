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


Poiché l'algoritmo A* opera internamente su posizioni (`Position`), ma il planner richiede in output una sequenza di direzioni (`Direction`), l'ultimo passaggio consiste nella ricostruzione del percorso a ritroso, traducendo le posizioni in direzioni. Questa operazione è possibile grazie alla mappa `cameFrom`, che associa a ogni posizione esplorata la posizione da cui è stata raggiunta. Per ogni posizione nel percorso, si consulta la mappa `cameFrom` per ottenere la posizione precedente, si calcola lo spostamento (come differenza tra le due posizioni), e si mappa tale spostamento sulla corrispondente direzione. La direzione ottenuta viene quindi aggiunta alla lista accumulata che rappresenta il percorso completo.
Con l’introduzione delle tile speciali (come i teletrasporti), è stato necessario aggiungere una gestione dedicata.
Se la differenza tra due posizioni supera di una cella in orizzontale o verticale, si assume che si tratti di un salto (come un teletrasporto). In tal caso, si cerca all’interno della lista di tile quella di tipo Special che punta alla posizione attuale (`newPos`). Una volta individuata, si ricalcola lo spostamento a partire dalla posizione effettiva della tile Special, in modo da dedurre la direzione corretta anche in presenza di "salti".
Il codice seguente implementa questa logica:
```scala
  private def reconstructPath(cameFrom: Map[Position, Position], current: Position, tiles: Seq[Tile]): Seq[Direction] =
    @tailrec
    def _reconstructPath(pos: Position, acc: List[Direction]): Seq[Direction] =
      cameFrom.get(pos) match
        case Some(prev) =>
          var delta = pos - prev
          if delta.x.abs > 1 || delta.y.abs > 1 then  // special case like a teleport
            val originalTile = tiles.find(
              t => t match
                case s: Special => s.newPos == pos
                case _ => false
            )
            originalTile match
              case Some(t) => delta = Position(t.x, t.y) - prev
              case _ => None
            delta = Position(0, 0)
          Direction.allDirections.find(_.vector == delta) match
            case Some(dir) => _reconstructPath(prev, dir :: acc)
            case _ => _reconstructPath(prev, acc)
        case None => acc
    _reconstructPath(current, Nil)
```
[Index](../index.md)
