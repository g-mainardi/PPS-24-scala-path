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

maxmoves(20).

% interval / range
interval(A, B, A):- A =< B.
interval(A, B, X):-
    A < B,
    A2 is A + 1,
    interval(A2, B, X).

count(N):-
	findall(Pos, passable(Pos), List),
	length(List, N).


% Basic plan
plan(Dirs, N):- plan(Dirs, N, _).
plan(Dirs, N, Path):- number(N), !, init(Pos), plan(Dirs, N, Pos, Path, [Pos]).
plan(Dirs, N, Path):-
	maxmoves(M),
	interval(1, M, N),
	plan(Dirs, N, Path).

% real algorithm
plan([], _, P, [P], _):- 	goal(P), !.
plan([], 0, _, [], _):- !, fail.
plan([Cmd|Dirs], N, Pos, [Pos|Path], Visited):-
	move(Pos, Cmd, Pos2),
	\+ member(Pos2, Visited),
	Nn is N - 1,
	plan(Dirs, Nn, Pos2, Path, [Pos2|Visited]).