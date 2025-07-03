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

% Initial and Goal States (specified by the user)
% init(s(0, 0)).
% goal(s(2, 3)).
gridbound(5).

% Transaction Rules: move(State, Direction, NewState)
move(s(X,Y), up,         s(X, Y1)) :- Y > 0, Y1 is Y - 1.
move(s(X,Y), down,       s(X, Y1)) :- gridbound(B), Y < B, Y1 is Y + 1.
move(s(X,Y), left,       s(X1, Y)) :- X > 0, X1 is X - 1.
move(s(X,Y), right,      s(X1, Y)) :- gridbound(B), X < B, X1 is X + 1.

move(s(X,Y), rightDown,  s(X1, Y1)) :- gridbound(B), X < B, Y < B, X1 is X + 1, Y1 is Y + 1.
move(s(X,Y), rightUp,    s(X1, Y1)) :- gridbound(B), X < B, Y > 0, X1 is X + 1, Y1 is Y - 1.
move(s(X,Y), leftUp,     s(X1, Y1)) :- gridbound(B), X > 0, Y > 0, X1 is X - 1, Y1 is Y - 1.
move(s(X,Y), leftDown,   s(X1, Y1)) :- gridbound(B), X > 0, Y < B, X1 is X - 1, Y1 is Y + 1.

% Planner: find path from initial state to goal state
plan(Path, Moves) :-
    init(Init),
    planner(Init, [Init], Path),
    length(Path, Moves).

% Base case: at goal, no moves needed
planner(State, _, []):- goal(State), !.

% Recursive case: explore possible directions
planner(State, Visited, [Dir|Rest]) :-
    move(State, Dir, NewState),
    \+ member(NewState, Visited),  % avoid cycles
    planner(NewState, [NewState|Visited], Rest).
