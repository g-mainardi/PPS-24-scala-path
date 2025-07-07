% Initial and Goal States (specified by the user)
ipos(s(0, 0)).
gpos(s(3, 2)).
gridsize(6).

% validcoord(?Coordinate) => {0,1,2,3,4,5}
validcoord(C):- validcoord(0, C).
validcoord(A, A).
validcoord(A, X):- gridsize(B), A < B, A2 is A + 1, validcoord(A2, X).

% bounds
notupperbound(N):-
	validcoord(N),
	gridsize(S),	N < S.
notlowerbound(N):- validcoord(N), N > 0.

% validpos(?Pos) => {s(0,0), s(0,1), ..., s(5,5)}
validpos(s(X, Y)):- validcoord(X), validcoord(Y).

% freepos(?Pos) => {s(0,1), s(0,2), ..., s(4,5)}
freepos(P):- validpos(P),	not(ipos(P)), not(gpos(P)).

% Transaction Rules: move(State, Direction, NewState)
move(s(X,Y), up,         s(X, Y1))  :-    validcoord(X), notlowerbound(Y), Y1 is Y - 1.
move(s(X,Y), down,       s(X, Y1))  :-    validcoord(X), notupperbound(Y), Y1 is Y + 1.
move(s(X,Y), left,       s(X1, Y))  :- notlowerbound(X),    validcoord(Y), X1 is X - 1.
move(s(X,Y), right,      s(X1, Y))  :- notupperbound(X),    validcoord(Y), X1 is X + 1.

move(s(X,Y), rightDown,  s(X1, Y1)) :- notupperbound(X), notupperbound(Y), X1 is X + 1, Y1 is Y + 1.
move(s(X,Y), rightUp,    s(X1, Y1)) :- notupperbound(X), notlowerbound(Y), X1 is X + 1, Y1 is Y - 1.
move(s(X,Y), leftUp,     s(X1, Y1)) :- notlowerbound(X), notlowerbound(Y), X1 is X - 1, Y1 is Y - 1.
move(s(X,Y), leftDown,   s(X1, Y1)) :- notlowerbound(X), notupperbound(Y), X1 is X - 1, Y1 is Y + 1.

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

% Dijkstra algorithm

% directions(Position, List of possibile directions)
directions(Pos, Output):- validpos(Pos), findall(Dir, move(Pos, Dir, _), Output).

% neighbour(Pos, One of possible Positions)
reachable(Pos, Pos2):-
	validpos(Pos),
	move(Pos, _, Pos2). 

% step(Node, Node+1)
step(n(0, Pos), n(1, Pos2), [Pos]):-
	ipos(Pos), !,
	reachable(Pos, Pos2).
	
step(n(Dist, Pos), n(Dist2, Pos2), [Pos|Visited]):-
	step(_, n(Dist, Pos), Visited),
	reachable(Pos, Pos2),
  \+ member(Pos2, Visited),  % evita visitati
	Dist2 is Dist + 1.

% Node(Weight, Pos)
node(n(0, Pos)):- ipos(Pos).
node(n(1, Pos)):- validpos(Pos), not(ipos(Pos)).

% Dijkstra planner
planD(PathToGoal, Cost):-
	ipos(Pos),
	planD(n(0, Pos), PathToGoal, [Pos], Cost).

planD(N, [Pos], _, Cost):- node(N), N = n(Cost, Pos), gpos(Pos), !.

planD(N, [Pos|PathToGoal], Visited, Cost):-
	node(N),
	N = n(W, Pos),
	write(Pos),
	reachable(Pos, Pos2),
	\+member(Pos2, Visited),
	planD(n(_, Pos2), PathToGoal, [Pos2|Visited], Costm),
	Cost is Costm + W.

% foldleft(+L, +Init, +BinaryOp, -Final)
% where BinaryOp = op(E1, E2, O, BINARY_OP)
% e.g.: BinaryOp = op(X, Y, A, A is X+Y)    => sum
% e.g.: BinaryOp = op(X, _, A, A is X+1)    => size
foldleft([], Acc, _, Acc).
foldleft([H|T], Acc, BOp, F):-
	 copy_term(BOp, op(Acc, H, NAcc, OP)), call(OP), foldleft(T, NAcc, BOp, F).

% find_min(+NodeList, -MinNode)
find_min([H|T], Min) :-
    foldleft(T, H,
    	op(
    		Acc, N, Min,
    		(Acc = n(C1, _), N = n(C2, _), C1 =< C2 -> Min = Acc; Min = N)
  		),
  		Min
  	).






