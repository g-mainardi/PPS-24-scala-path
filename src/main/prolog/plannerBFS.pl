% interval / range
interval(A, B, A):- A < B.
interval(A, B, X):-
    A < B,
    A2 is A + 1,
    interval(A2, B, X).

% Basic plan
plan(Dirs):- plan(Dirs, _).
plan(Dirs, N):- plan(Dirs, N, Path).
plan(Dirs, N, Path):- number(N), !, ipos(Pos), plan(Dirs, N, Pos, Path, [Pos]).
plan(Dirs, N, Path):-
	gridsize(MaxMoves),
	interval(1, MaxMoves, N),
	plan(Dirs, N, Path).
plan([], _, P, [P], _):- 	gpos(P), !.
plan([], 0, _, [], _):- !, fail.

plan([Cmd|Dirs], N, Pos, [Pos|Path], Visited):-
	move(Pos, Cmd, Posn),
	\+ member(Posn, Visited),  % opzionale: evita cicli
	Nn is N - 1,
	plan(Dirs, Nn, Posn, Path, [Posn|Visited]).