% Initial and Goal States (specified by the user)
init(s(0, 0)).
goal(s(2, 2)).
gridsize(5).

% validcoord(?Coordinate) => {0,1,2,3,4,5}
validcoord(C):- validcoord(0, C).
validcoord(A, A).
validcoord(A, X):- gridsize(B), A < B, A2 is A + 1, validcoord(A2, X).

% validpos(?Pos) => {s(0,0), s(0,1), ..., s(5,5)}
validpos(s(X, Y)):- validcoord(X), validcoord(Y).

% freepos(?Pos) => {s(0,1), s(0,2), ..., s(4,5)}
freepos(P):- validpos(P),	not(init(P)), not(goal(P)).

% Transaction Rules: move(State, Direction, NewState)
move(s(X,Y), up,         s(X, Y1)) :- Y > 0, Y1 is Y - 1, validcoord(X), validcoord(Y).
move(s(X,Y), down,       s(X, Y1)) :- gridsize(B), Y < B, Y1 is Y + 1, validcoord(X), validcoord(Y).
move(s(X,Y), left,       s(X1, Y)) :- X > 0, X1 is X - 1, validcoord(X), validcoord(Y).
move(s(X,Y), right,      s(X1, Y)) :- gridsize(B), X < B, X1 is X + 1, validcoord(X), validcoord(Y).

move(s(X,Y), rightDown,  s(X1, Y1)) :- gridsize(B), X < B, Y < B, X1 is X + 1, Y1 is Y + 1, validcoord(X), validcoord(Y).
move(s(X,Y), rightUp,    s(X1, Y1)) :- gridsize(B), X < B, Y > 0, X1 is X + 1, Y1 is Y - 1, validcoord(X), validcoord(Y).
move(s(X,Y), leftUp,     s(X1, Y1)) :- gridsize(B), X > 0, Y > 0, X1 is X - 1, Y1 is Y - 1, validcoord(X), validcoord(Y).
move(s(X,Y), leftDown,   s(X1, Y1)) :- gridsize(B), X > 0, Y < B, X1 is X - 1, Y1 is Y + 1, validcoord(X), validcoord(Y).

% interval / range
interval(A, B, A):- A < B.
interval(A, B, X):-
    A < B,
    A2 is A + 1,
    interval(A2, B, X).

% Basic plan
plan(Dirs):- plan(Dirs, _).
plan(Dirs, N):- plan(Dirs, N, Path).
plan(Dirs, N, Path):- number(N), !, init(Pos), plan(Dirs, N, Pos, Path, [Pos]).
plan(Dirs, N, Path):- 
	gridsize(MaxMoves),
	interval(1, MaxMoves, N),
	plan(Dirs, N, Path).
plan([], _, P, [P], _):- 	goal(P), !.
plan([], 0, _, [], _):- !, fail.

plan([Cmd|Dirs], N, Pos, [Pos|Path], Visited):- 
	move(Pos, Cmd, Posn),
	\+ member(Posn, Visited),  % opzionale: evita cicli
	Nn is N - 1, 
	plan(Dirs, Nn, Posn, Path, [Posn|Visited]).

% Dijkstra algorithm

% directions(Position, List of possibile directions)
directions(Pos, Output):- validpos(Pos), findall(Dir, move(Pos, Dir, _), Output).

% neighbour(Pos, One of possible Positions)
reachable(Pos, Pos2):-
	validpos(Pos),
	move(Pos, _, Pos2). 

% step(Node, Node+1)
step(n(0, Pos), n(1, Pos2), [Pos]):-
	init(Pos),
	reachable(Pos, Pos2).
	
step(n(Dist, Pos), n(Dist2, Pos2), [Pos|Visited]):-
	step(_, n(Dist, Pos), Visited),
	reachable(Pos, Pos2),
  \+ member(Pos2, Visited),  % evita visitati
	Dist2 is Dist + 1.
	


