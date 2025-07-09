init(s(0, 0)).
goal(s(4, 4)).

move(s(X,Y), up, s(X, Y1)) :-
    Y1 is Y - 1,
    passable(s(X, Y1)).

move(s(X,Y), down, s(X, Y1)) :-
    Y1 is Y + 1,
    passable(s(X, Y1)).

move(s(X,Y), left, s(X1, Y)) :-
    X1 is X - 1,
    passable(s(X1, Y)).

move(s(X,Y), right, s(X1, Y)) :-
    X1 is X + 1,
    passable(s(X1, Y)).

move(s(X,Y), rightUp, s(X1, Y1)) :-
    X1 is X + 1,
    Y1 is Y - 1,
    passable(s(X1, Y1)).

move(s(X,Y), rightDown, s(X1, Y1)) :-
    X1 is X + 1,
    Y1 is Y + 1,
    passable(s(X1, Y1)).

move(s(X,Y), leftUp, s(X1, Y1)) :-
    X1 is X - 1,
    Y1 is Y - 1,
    passable(s(X1, Y1)).

move(s(X,Y), leftDown, s(X1, Y1)) :-
    X1 is X - 1,
    Y1 is Y + 1,
    passable(s(X1, Y1)).

passable(s(0, 0)).
passable(s(0, 1)).
passable(s(0, 2)).
passable(s(0, 3)).
passable(s(0, 4)).

passable(s(1, 0)).
passable(s(1, 3)).
passable(s(1, 4)).

passable(s(2, 0)).
passable(s(2, 2)).
passable(s(2, 3)).
passable(s(2, 4)).

passable(s(3, 0)).
passable(s(3, 1)).
passable(s(3, 2)).
passable(s(3, 3)).
passable(s(3, 4)).

passable(s(4, 0)).
passable(s(4, 1)).
passable(s(4, 2)).
passable(s(4, 3)).
passable(s(4, 4)).

% ---------------------------------

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
	Steps = [c(P, _) | T],
	% if Max is specified then I have to keep track of the lenght
	% if not, I will calculate the lenght only at the end
  (number(Max) -> length(Steps, L), L =< Max + 1 ; true),  % +1 because first move is 'none'
  find_new_paths(Steps, NewPaths),
  append(Others, NewPaths, QueueNext),
  bfs(QueueNext, Max, Dirs, Path).

% find_new_paths(+Steps, -NewPaths):-
find_new_paths(Steps, NewPaths):-
	Steps = [c(P, _) | T],
  findall(
  	[c(Next, Dir)| Steps],
    (move(P, Dir, Next), \+ member(c(Next, _), T)),
    NewPaths
  ).

% extract(+Queue, -Path, -Directions)
extract([], [], []).
extract([c(P, D) | T], [P | Ps], Dirs) :-
	D == none -> 
  	extract(T, Ps, Dirs); 
  	Dirs = [D | Ds], extract(T, Ps, Ds).

 

