# Maze
Lo scenario labirinto viene generato in modo procedurale usando l'algoritmo DFS (Depth-First Search).
Per gestire il labirinto, si parte da una cella e si scava verso celle adiacenti non visitate, rimuovendo i muri tra di esse.
Viene utilizzata una griglia fisica ed una logica per rappresentare le celle e i muri.


### Logical Grid
L'algoritmo utilizza una griglia logica ed una fisica. In quella logica i muri sono delle linee che non occupano spazio,
invece in quella fisica i muri sono delle celle che occupano spazio.
Inzialmente tutte le celle sono isolate dalle altre attraverso dei muri.

Per esempio, una griglia rows x cols ha effettivamente dimensione 2*rows+1 x 2*cols+1, dove:
- Le celle pari (2x+1, 2y+1) sono floor. 
- Le celle dispari adiacenti sono muri (orizzontali o verticali), ad esempio (2x+1, 2y) o (2x, 2y+1).

```scala
  private val logicalRows = Scenario.nRows / 2
  private val logicalCols = Scenario.nCols / 2
  private val gridRows = 2 * logicalRows + 1
  private val gridCols = 2 * logicalCols + 1
```


### Carve
Per collegare le celle, si utilizza un algoritmo di scavo ricorsivo (carve) che parte da una cella iniziale 
ed inizia a scavare verso le celle adiacenti non visitate.

Step ricorsivo carve(r, c):
- segna (r, c) come visitata
- segna la posizione fisica (2r+1, 2c+1) come Floor
- ottieni i vicini non visitati, in ordine casuale
- per ogni vicino:
    - rimuovi il muro tra la cella corrente e il vicino
    - chiama carve sul vicino

```scala
def carve(row: Int, col: Int, visited: Set[(Int, Int)], maze: Map[Position, Tile]): (Set[(Int, Int)], Map[Position, Tile]) =
  val newVisited = visited + ((row, col))
  val x = 2 * row + 1
  val y = 2 * col + 1
  val mazeWithRoom = maze + (Position(x, y) -> Floor(Position(x, y)))
  
  neighbors(row, col).foldLeft((newVisited, mazeWithRoom)) {
    case ((v, m), (nr, nc, dr, dc)) if inBounds(nr, nc) && !v.contains((nr, nc)) =>
      val wallX = x + dr
      val wallY = y + dc
      val m1 = m + (Position(wallX, wallY) -> Floor(Position(wallX, wallY)))
      val (v2, m2) = carve(nr, nc, v + ((nr, nc)), m1)
      (v2, m2)
  
    case ((v, m), _) => (v, m)
}
```


Vengono considerati solo i vicini non ancora visitati e si scava una volta sola verso ogni vicino non visitato.
Dopo che un vicino è stato visitato, non verrà più scavato da un’altra cella pertanto non verranno tolti tutti i muri ma solo alcuni.
Per generare labirinti casuali sempre diversi i vicini vengono visitati in ordine casuale.
```scala
   def neighbors(row: Int, col: Int): List[(Int, Int, Int, Int)] =
      Random.shuffle(List(
        (row - 1, col, -1, 0),
        (row + 1, col, 1, 0),
        (row, col - 1, 0, -1),
        (row, col + 1, 0, 1)
      ))
```

### Difference
Per esemplificare il calcolo delle posizioni fisiche dei muri, consideriamo una cella logica (r, c) e un vicino logico (nr, nc).
- Stando nella cella logica (1, 1), la posizione reale è (3, 3)
- Se si vuole scavare verso Sud, il vicino logico è (2, 1)
- Di conseguenza la differenza tra le celle logiche è: dr = +1, dc = 0 
- Quindi il muro tra (1, 1) e (2, 1) è a (3 + 1, 3 + 0) = (4, 3)
- Quel Wall in (4, 3) viene trasformato in Floor.


### Perfect Maze
L’algoritmo parte da una cella e visita ogni cella esattamente una volta.
Di conseguenza si forma un labirinto perfetto, ovvero un labirinto senza cicli e senza dead-end,
con una singola soluzione per ogni goal, ed in cui ogni cella è raggiungibile da ogni altra cella.

Questo perché l'algoritmo ogni volta che raggiunge una nuova cella, la connette al resto del labirinto rimuovendo un solo muro.
Continua finché tutte le celle sono visitate (quindi sono tutte connesse al labirinto).

Dal momento che il DFS scava solo verso celle non visitate, non si formano mai cicli. Quindi:
1. tra due celle qualsiasi c’è esattamente un percorso. 
2. il labirinto è sempre risolvibile da qualunque punto a qualunque altro punto.

In questo modo il labirinto può essere risolto con qualsiasi coppia (init, goal), 
che eventualmente può essere specificata dall'utente.

[Index](../index.md)