## DFS Planner

Il Depth-First-Search Planner è in grado di esplorare una griglia 2D seguendo varie direzioni,
e trovare un percorso da uno stato iniziale a uno stato obiettivo, tenendo conto degli ostacoli e delle celle già visitate per evitare cicli.

Il planner è implementato in Prolog e utilizza un approccio di ricerca depth-first per esplorare le possibili mosse fino a raggiungere lo stato obiettivo.
Lo stato è rappresentato come una coppia di coordinate `s(X, Y)`.

Utilizzando una tecnica DFS il planner non ottiene percorsi ottimali, tuttavia è servito come base di partenza
per poi implementare un planner più avanzato come il BFS.

Può supportare diverse direzioni tipologie di direzioni, ad esempio cardinali e diagonali.
Prima di spostarsi verso una nuova cella, verifica se nello scenario è una cella percorribile o meno.
Per evitare dei piani in cui si ritorna su propri passi, il planner tiene traccia delle celle già visitate.

Lo scenario viene generato a runtime da Scala e definisce per ogni cella se è percorribile.
Se una cella non è specificata come percorribile, viene considerata bloccata da un ostacolo.
```prolog
passable(s(0, 1)).
passable(s(1, 1)).
passable(s(0, 2)).
...
```
Le possibili direzioni di movimento sono definite come delta rispetto alla posizione corrente.
Il predicato `move/3` controlla tutte le direzioni possibili e verifica se la nuova cella è percorribile.
```prolog
delta(up, 0, -1).
delta(down, 0, 1).
delta(left, -1, 0).
delta(right, 1, 0).

delta(rightUp, 1, -1).
delta(rightDown, 1, 1).
delta(leftUp, -1, -1).
delta(leftDown, -1, 1).

move(s(X,Y), Dir, s(X1,Y1)) :-
    delta(Dir, DX, DY),
    X1 is X + DX,
    Y1 is Y + DY,
    passable(s(X1,Y1)).
```

Il planner è depth-first, quindi non garantisce il percorso minimo.
Esplora tutte le direzioni possibili, evitando le celle già visitate e gli ostacoli.
```prolog
% Caso base: stato raggiunto
planner(State, State, _, [], 0).

% Caso ricorsivo: esplora una direzione valida
planner(State, Goal, Visited, [Dir|Rest], NewMoves) :-
    move(State, Dir, TempState),
    checkSpecial(TempState, NewState),
    \+ member(NewState, Visited),
    planner(NewState, Goal, [NewState|Visited], Rest, Moves),
    NewMoves is Moves + 1.
```
Il parametro `Moves` è fully-relational, se specificato viene usato come massimo numero di mosse.
Se non viene specificato, viene usato come output indicando il numero di mosse del piano generato.
In questo ultimo caso ne verrà fatto il parsing lato scala ed eventualmente mostrato all'utente.


Il planner gestisce anche i casi di celle speciali, che causano un cambio di stato in base al tipo cella speciale.
```prolog
    checkSpecial(TempState, NewState) :- special(TempState, NewState), !.
    checkSpecial(TempState, TempState).
```
Il comportamento delle celle speciali viene configurato in Scala e poi generato in Prolog.
```prolog
passable(s(0, 3)).
special(s(0, 3), s(0, 0)).
```

[Index](../index.md)

