# Maze Impl
### Summary
Il DFS parte da una cella (r, c)
Ottiene i vicini non visitati: (nr, nc)
Per ciascuno:
- Rimuove il muro tra (r, c) e (nr, nc)
- Visita ricorsivamente (nr, nc)


### Logical Grid
Immaginiamo che ogni cella “logica” sia separata da un muro orizzontale o verticale. 
Per esempio, una griglia rows x cols ha effettivamente dimensione 2*rows+1 x 2*cols+1, dove:
- Le celle pari (2x+1, 2y+1) sono le stanze/floor. 
- Le celle dispari adiacenti sono muri (orizzontali o verticali), tipo (2x+1, 2y) o (2x, 2y+1).


### Carve
Step ricorsivo carve(r, c):
- segna (r, c) come visitata
- segna la posizione fisica (2r+1, 2c+1) come Floor
- ottieni i vicini non visitati, in ordine casuale
- per ogni vicino:
    - rimuovi il muro tra la cella corrente e il vicino
    - chiama carve sul vicino


### Visited
Vengono considerati solo i vicini non ancora visitati.
Si scava una volta sola verso ogni vicino non visitato.
Dopo che un vicino è stato visitato, non verrà più scavato da un’altra cella.
Pertanto non verranno tolti tutti i muri ma solo alcuni.


### Diff
Supponiamo:
Stai nella cella logica (1, 1) → posizione reale (3, 3)
Vuoi scavare verso Sud → vicino logico (2, 1)
allora: dr = +1, dc = 0
il muro tra (1, 1) e (2, 1) è a (3 + 1, 3 + 0) = (4, 3)
Quel Wall in (4, 3) viene trasformato in Floor.

### Exactly One Solution
L’algoritmo parte da una cella e visita ogni cella esattamente una volta.
Si forma un albero aciclico in cui tutte le celle sono connesse tra loro.

Ogni volta che raggiunge una nuova cella, la connette al resto del labirinto rimuovendo un solo muro
Continua finché tutte le celle sono visitate (quindi sono tutte connesse al labirinto)

Dal momento che il DFS scava solo verso celle non visitate, non si formano mai cicli. Quindi:
1. tra due celle qualsiasi c’è esattamente un percorso. 
2. il labirinto è sempre solvibile da qualunque punto a qualunque altro punto 