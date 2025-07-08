
directions(D):- cardinals(D).
directions(D):- diagonals(D).

% Transaction Rules: move(State, Direction, NewState)
move(s(X,Y), up, s(X, Y1)) :-
    Y1 is Y - 1,
    passable(X, Y1).

move(s(X,Y), down, s(X, Y1)) :-
    Y1 is Y + 1,
    passable(X, Y1).

move(s(X,Y), left, s(X1, Y)) :-
    X1 is X - 1,
    passable(X1, Y).

move(s(X,Y), right, s(X1, Y)) :-
    X1 is X + 1,
    passable(X1, Y).

move(s(X,Y), rightDown, s(X1, Y1)) :-
    X1 is X + 1, Y1 is Y + 1,
    passable(X1, Y1).

move(s(X,Y), rightUp, s(X1, Y1)) :-
    X1 is X + 1, Y1 is Y - 1,
    passable(X1, Y1).

move(s(X,Y), leftUp, s(X1, Y1)) :-
    X1 is X - 1, Y1 is Y - 1,
    passable(X1, Y1).

move(s(X,Y), leftDown, s(X1, Y1)) :-
    X1 is X - 1, Y1 is Y + 1,
    passable(X1, Y1).

% Planner: find path from initial state to goal state
plan(Path, Moves) :-
    init(Init),
    goal(Goal),
    planner(Init, Goal, [Init], Path, Moves).

% Base case: at goal, no moves needed
planner(State, State, _, [], 0).

% Recursive case: explore possible directions
planner(State, Goal, Visited, [Dir|Rest], NewMoves) :-
    directions(Dir),
    move(State, Dir, NewState),
    \+ member(NewState, Visited),  % avoid cycles
    planner(NewState, Goal, [NewState|Visited], Rest, Moves),
  	NewMoves is Moves + 1.