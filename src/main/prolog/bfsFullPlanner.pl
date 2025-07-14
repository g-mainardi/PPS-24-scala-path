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

passable(s(0, 0)).
passable(s(0, 1)).
passable(s(0, 2)).
special(s(0, 2), s(2, 2)).
passable(s(0, 3)).
special(s(0, 3), s(0, 0)).
passable(s(0, 4)).
passable(s(1, 0)).
passable(s(1, 1)).
special(s(1, 1), s(-1, 1)).
passable(s(1, 2)).
special(s(1, 2), s(1, 2)).
passable(s(1, 3)).
passable(s(1, 4)).
passable(s(2, 0)).
passable(s(2, 1)).
passable(s(2, 2)).
passable(s(2, 3)).
passable(s(2, 4)).
passable(s(3, 0)).
special(s(3, 0), s(0, 0)).
passable(s(3, 1)).
passable(s(3, 2)).
passable(s(3, 3)).
passable(s(3, 4)).
passable(s(4, 0)).
passable(s(4, 1)).
special(s(4, 1), s(1, 1)).
passable(s(4, 2)).
special(s(4, 2), s(0, 2)).
passable(s(4, 3)).
passable(s(4, 4)).

% ---------------------------------

% checkSpecial(+NextPosition, -SubstitutePosition)
checkSpecial(Next, Sub) :- special(Next, Sub), !.
checkSpecial(Next, Next).

plan(Dirs, Max) :-
  init(Init),
  bfs([[c(Init, none)]], Max, Dirs, _).  % queue contains Steps (list of steps)

% bfs(+Queue, ?MaxMoves, -Directions, -Path)

bfs([Steps | _], Max, Dirs, Path) :-    % case: goal reached
	Steps = [c(P, D) | T],
  goal(P),
  % Prepare the outputs
  reverse(Steps, Full),
  extract(Full, Path, Dirs),
  (var(Max) -> length(Dirs, Max); true).

bfs([Steps|Others], Max, Dirs, Path) :- % case: recursion on queue
	Steps = [c(P, _) | _],
	% if Max is specified then I have to keep track of the lenght
	% if not, I will calculate the lenght only at the end
  (number(Max) -> length(Steps, L), L =< Max + 1 ; true),  % +1 because first move is 'none'
  find_new_paths(Steps, Others, NewPaths),
  append(Others, NewPaths, QueueNext),
  bfs(QueueNext, Max, Dirs, Path).

% find_new_paths(+Steps, +Others, -NewPaths):-
find_new_paths(Steps, Others, NewPaths):-
	Steps = [c(P, _) | T],
  findall(
  	[c(Next, Dir)| Steps],
  	% search a move to a position not in this trace or in the head of the others
  	search_next(P, T, Others, Next, Dir),
    NewPaths
  ).

% search_next(+Position, +Visited, +OtherPaths, -NextPosition, -ChoosenDirection)
search_next(Src, Trace, Others, Out, Dir):-
  move(Src, Dir, Next),
  \+ member(c(Next, _), Trace),
  \+ member([c(Next, _)|_], Others),
  checkSpecial(Next, Out).

% extract(+Queue, -Path, -Directions)
extract([], [], []).
extract([c(P, D) | T], [P | Ps], Dirs) :-
	D == none -> 
  	extract(T, Ps, Dirs); 
  	Dirs = [D | Ds], extract(T, Ps, Ds).

 

