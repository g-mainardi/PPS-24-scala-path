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
search_next(Src, Trace, Others, Next, Dir):-
  move(Src, Dir, Next),
  \+ member(c(Next, _), Trace),
  \+ member([c(Next, _)|_], Others).

% extract(+Queue, -Path, -Directions)
extract([], [], []).
extract([c(P, D) | T], [P | Ps], Dirs) :-
	D == none -> 
  	extract(T, Ps, Dirs); 
  	Dirs = [D | Ds], extract(T, Ps, Ds).
