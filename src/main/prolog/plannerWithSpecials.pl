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
    move(State, Dir, TempState),
    \+ member(TempState, Visited),
    ( special(TempState, NewState) -> true ; NewState = TempState ),
    \+ member(NewState, Visited),
    planner(NewState, Goal, [NewState|Visited], Rest, Moves),
  	NewMoves is Moves + 1.