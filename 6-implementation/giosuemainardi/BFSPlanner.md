Il **Breadth-First Search (BFS)** è un algoritmo di ricerca non informato che esplora sistematicamente un grafo visitando
tutti i nodi a distanza _k_ prima di procedere con quelli a distanza _k+1_. 
Questo approccio garantisce di trovare sempre il **percorso più breve** in termini di numero di archi (o mosse) quando 
tutti gli archi hanno lo stesso peso.

L'algoritmo utilizza una **coda FIFO** (First In, First Out) per mantenere l'ordine di esplorazione: i nodi vengono 
aggiunti in coda man mano che vengono scoperti e processati in ordine di inserimento. Questa caratteristica assicura 
l'esplorazione **livello per livello**, rendendo BFS particolarmente adatto per problemi di pathfinding su griglie dove 
si cerca il percorso con il minor numero di passi.

#### Entry Point e Struttura generale

La teoria del planner si basa su una configurazione comune generata a runtime dall'applicazione, che definisce:

- Le posizioni percorribili sulla griglia
- L'insieme delle mosse consentite
- La posizione di partenza (init)
- La posizione di arrivo (goal)
- Il comportamento delle caselle speciali che modificano la posizione del giocatore

Per maggiori dettagli sulla generazione di questa configurazione si rimanda alla documentazione del DFS-Planner.

L'algoritmo utilizza una **coda** per esplorare sistematicamente tutti i percorsi possibili, garantendo che il primo 
percorso trovato sia quello con il minor numero di mosse.

```prolog
plan(Dirs, Max) :-
  init(Init),
  bfs([[c(Init, none)]], Max, Dirs, _).
```

L' entry-point è `plan/2`, predicato **fully-relational** che avvia la ricerca partendo dalla posizione iniziale, rappresentata come una lista contenente un singolo step `c(Init, none)`.

#### BFS: Il cuore della ricerca

Il cuore dell'implementazione è il predicato `bfs/4` che gestisce due casi principali:

```prolog
bfs([Steps | _], Max, Dirs, Path) :-
  Steps = [c(P, D) | T],
  goal(P),
  reverse(Steps, Full),
  extract(Full, Path, Dirs),
  (var(Max) -> length(Dirs, Max); true).
```

Quando la posizione corrente corrisponde all'obiettivo, l'algoritmo estrae il percorso e le direzioni dalla lista di step, 
invertendola per ottenere l'ordine corretto.

```prolog
bfs([Steps|Others], Max, Dirs, Path) :-
  Steps = [c(P, _) | _],
  (number(Max) -> length(Steps, L), L =< Max + 1 ; true),
  find_new_paths(Steps, Others, NewPaths),
  append(Others, NewPaths, QueueNext),
  bfs(QueueNext, Max, Dirs, Path).
```

Il caso ricorsivo espande il percorso corrente, genera tutti i nuovi percorsi possibili e li aggiunge alla coda per l'esplorazione successiva.

#### Generazione di Nuovi Percorsi

```prolog
search_next(Src, Trace, Others, Out, Dir):-
  move(Src, Dir, Next),
  \+ member(c(Next, _), Trace),
  \+ member([c(Next, _)|_], Others),
  checkSpecial(Next, Out).
```

La funzione `search_next/5` genera le mosse valide assicurandosi che:
- La mossa sia consentita (`move/3`)
- La posizione di destinazione non sia già stata visitata nel percorso corrente
- La posizione non sia già in esplorazione in altri percorsi della coda
- Gestisca eventuali tile speciali tramite `checkSpecial/2`

#### Predicato find_new_paths
Il predicato `find_new_paths/3` è responsabile della **generazione di tutti i nuovi percorsi validi** a partire dal 
percorso corrente, espandendo sistematicamente le possibili mosse.
```prolog
find_new_paths(Steps, Others, NewPaths):-
	Steps = [c(P, _) | T],
  findall(
  	[c(Next, Dir)| Steps],
  	search_next(P, T, Others, Next, Dir),
    NewPaths
  ).
```
Il predicato estrae la posizione corrente `P` dalla testa della lista di step e utilizza `findall/3` per raccogliere
**tutti** i percorsi possibili che possono essere generati da quella posizione. 
Per ogni mossa valida trovata da `search_next/5`, viene creato un nuovo percorso aggiungendo il nuovo step `c(Next, Dir)` 
in testa alla lista esistente. 
Questa strategia mantiene l'**ordine cronologico inverso** degli step, che verrà poi corretto nella fase di estrazione 
del risultato finale.

#### Predicato extract

Il predicato `extract/3` trasforma la rappresentazione interna del percorso in un formato utilizzabile, 
separando le **posizioni** dalle **direzioni** di movimento.

```prolog
extract([], [], []).
extract([c(P, D) | T], [P | Ps], Dirs) :-
	D == none -> 
  	extract(T, Ps, Dirs); 
  	Dirs = [D | Ds], extract(T, Ps, Ds).
```

La funzione opera ricorsivamente sulla lista di step, estraendo le posizioni `P` in una lista separata. Per le direzioni, distingue due casi: se la direzione è `none` (tipicamente il primo step dalla posizione iniziale), viene **ignorata** e non inclusa nella lista delle direzioni finali. Altrimenti, la direzione viene aggiunta alla lista risultante. Questo meccanismo produce come output una **sequenza pulita** di comandi di movimento che l'agente può eseguire per raggiungere l'obiettivo.


#### Gestione delle Tile Speciali

```prolog
checkSpecial(Next, Sub) :- special(Next, Sub), !.
checkSpecial(Next, Next).
```

Il sistema supporta tile speciali che possono modificare la posizione di destinazione (ad esempio, teleport o trappole). 
Se una posizione non è speciale, rimane invariata.