cardinals(up).
cardinals(down).
cardinals(left).
cardinals(right).

diagonals(rightDown).
diagonals(rightUp).
diagonals(leftUp).
diagonals(leftDown).

directions(D):- cardinals(D).
directions(D):- diagonals(D).

% Stato iniziale e obiettivo
init(s(0, 0)).
goal(s(5, 3)).

% Regole di transizione: move(State, Direction, NewState)
move(s(X,Y), up,         s(X, Y1)) :- Y1 is Y + 1.
move(s(X,Y), down,       s(X, Y1)) :- Y1 is Y - 1.
move(s(X,Y), left,       s(X1, Y)) :- X1 is X - 1.
move(s(X,Y), right,      s(X1, Y)) :- X1 is X + 1.

move(s(X,Y), rightDown,  s(X1, Y1)) :- X1 is X + 1, Y1 is Y - 1.
move(s(X,Y), rightUp,    s(X1, Y1)) :- X1 is X + 1, Y1 is Y + 1.
move(s(X,Y), leftUp,     s(X1, Y1)) :- X1 is X - 1, Y1 is Y + 1.
move(s(X,Y), leftDown,   s(X1, Y1)) :- X1 is X - 1, Y1 is Y - 1.

% Planner: trova un path dalla posizione corrente al goal
plan(Path, MaxMoves) :-
    init(Init),
    goal(Goal),
    planner(Init, Goal, [Init], Path, MaxMoves).

% Caso base: già nel goal, nessuna mossa
planner(State, State, _, [], _).

% Espandi una mossa
planner(State, Goal, Visited, [Dir|Rest], MovesLeft) :-
    MovesLeft > 0,
    directions(Dir),
    move(State, Dir, NewState),
    \+ member(NewState, Visited),  % evita cicli
    NewMovesLeft is MovesLeft - 1,
    planner(NewState, Goal, [NewState|Visited], Rest, NewMovesLeft).
