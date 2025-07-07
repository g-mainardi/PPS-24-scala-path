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
	maxmoves(M),
	interval(1, M, N),
	plan(Dirs, N, Path).
plan([], _, P, [P], _):- 	goal(P), !.
plan([], 0, _, [], _):- !, fail.

plan([Cmd|Dirs], N, Pos, [Pos|Path], Visited):-
	move(Pos, Cmd, Pos2),
	\+ member(Pos2, Visited),
	Nn is N - 1,
	plan(Dirs, Nn, Pos2, Path, [Pos2|Visited]).