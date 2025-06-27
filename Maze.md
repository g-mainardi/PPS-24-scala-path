Immaginiamo che ogni cella “logica” sia separata da un muro orizzontale o verticale. 
Per esempio, una griglia rows x cols ha effettivamente dimensione 2*rows+1 x 2*cols+1, dove:
- Le celle pari (2x+1, 2y+1) sono le stanze/floor. 
- Le celle dispari adiacenti sono muri (orizzontali o verticali), tipo (2x+1, 2y) o (2x, 2y+1).


Step ricorsivo carve(r, c):
- segna (r, c) come visitata
- segna la posizione fisica (2r+1, 2c+1) come Floor
- ottieni i vicini non visitati, in ordine casuale
- per ogni vicino:
    - rimuovi il muro tra la cella corrente e il vicino
    - chiama carve sul vicino

Vengono considerati solo i vicini non ancora visitati.
Si scava una volta sola verso ogni vicino non visitato.
Dopo che un vicino è stato visitato, non verrà più scavato da un’altra cella.