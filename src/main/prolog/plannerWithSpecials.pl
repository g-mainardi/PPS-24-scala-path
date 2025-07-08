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
    %% directions(Dir),
    move(State, Dir, TempState),
    %% \+ member(TempState, Visited),
    checkSpecial(TempState, NewState),
    \+ member(NewState, Visited),
    planner(NewState, Goal, [NewState|Visited], Rest, Moves),
  	NewMoves is Moves + 1.