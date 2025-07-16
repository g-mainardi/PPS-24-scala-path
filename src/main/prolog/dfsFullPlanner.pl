init(s(0, 0)).
goal(s(4, 4)).

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

% Planner: find path from initial state to goal state
plan(Path, Moves) :-
    init(Init),
    goal(Goal),
    planner(Init, Goal, [Init], Path, Moves).

% Base case: at goal, no moves needed
planner(State, State, _, [], 0).

checkSpecial(TempState, NewState) :- special(TempState, NewState), !.
checkSpecial(TempState, TempState).

% Recursive case: explore possible directions
% planner(+CurrentState, +GoalState, +VisitedStates, -Path, -MoveCount)
planner(State, Goal, Visited, [Dir|Rest], NewMoves) :-
    move(State, Dir, TempState),
    checkSpecial(TempState, NewState),
    \+ member(NewState, Visited),
    planner(NewState, Goal, [NewState|Visited], Rest, Moves),
  	NewMoves is Moves + 1.