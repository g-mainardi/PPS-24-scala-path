# Base Prolog Planner

Il planner è in grado di esplorare una griglia 2D seguendo varie direzioni,
e trova un percorso da uno stato iniziale a uno stato obiettivo,
tenendo conto degli ostacoli e delle celle già visitate per evitare cicli.

Il planner è implementato in Prolog e utilizza un approccio di ricerca depth-first per esplorare le possibili mosse fino a raggiungere lo stato obiettivo.
Lo stato è rappresentato come una coppia di coordinate `s(X, Y)`

Può supportare diverse direzioni tipologie di direzioni, ad esempio cardinali e diagonali.
Prima di spostarsi verso una nuova cella, verifica se nello scenaroi è percorribile o meno.
Per evitare dei plan in cui si ritorna su propri passi, il planner tiene traccia delle celle già visitate.

Lo scenario viene generato a runtime da Scala e definisce per ogni cella se è percorribile o meno.
```prolog
passable(0, 1).
passable(1, 1).
blocked(2, 1).
passable(0, 2).
...
```

Ogni direzione è implementata con una regola `move/3` che definisce la nuova posizione e verifica se è percorribile:
```prolog
move(s(X,Y), up, s(X, Y1)) :-
    Y1 is Y - 1,
    passable(X, Y1).
```

Il planner è depth-first, quindi non garantisce il percorso minimo.
Esplora tutte le direzioni possibili, evitando le celle già visitate e gli ostacoli.
```prolog
% Caso base: stato raggiunto
planner(State, State, _, [], 0).

% Caso ricorsivo: esplora una direzione valida
planner(State, Goal, Visited, [Dir|Rest], NewMoves) :-
    directions(Dir),
    move(State, Dir, NewState),
    \+ member(NewState, Visited),  % evita cicli
    planner(NewState, Goal, [NewState|Visited], Rest, Moves),
    NewMoves is Moves + 1.
```

Il parametro `Moves` è fully-relational, se specificato viene usato come massimo numero di mosse.
Se non viene specificato, viene usato come output indicando il numero di mosse del piano generato.

[Index](../index.md)

