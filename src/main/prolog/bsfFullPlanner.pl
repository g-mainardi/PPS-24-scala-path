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

% ---------------------------------

plan(Dirs, Max) :-
  init(Init),
  bfs([[c(Init, none)]], 0, Max, Dirs, _).  % la queue contiene percorsi (lista di posizioni)

% bfs(+Queue, +LengthTillNow, ?MaxMoves, -Directions, -Path)
bfs([[c(Pos, Dir) | Rest] | _], _, Max, Dirs, Path) :-
    goal(Pos),
    reverse([c(Pos, Dir) | Rest], Full),
    extract_path(Full, Path),
    extract_directions(Full, Dirs),
    (var(Max) -> length(Dirs, Max); true).

bfs([[c(Pos, Old) | Rest] | Others], _, Max, Dirs, Path) :-
    (number(Max) -> length([c(Pos, Old) | Rest], L), L =< Max + 1 ; L=none),  % +1 perché la prima mossa è 'none'
    findall([c(Next, Dir), c(Pos, Old) | Rest],
        ( move(Pos, Dir, Next),
          \+ member(c(Next, _), Rest),
          passable(Next)
        ),
        NewPaths),
    append(Others, NewPaths, QueueNext),
    bfs(QueueNext, L, Max, Dirs, Path).
    
extract_path([], []).
extract_path([c(P, _) | Rest], [P | Ps]) :-
    extract_path(Rest, Ps).

extract_directions([], []).
extract_directions([c(_, none) | Rest], Dirs) :-
    extract_directions(Rest, Dirs).
extract_directions([c(_, M) | Rest], [M | Ms]) :-
    M \= none,
    extract_directions(Rest, Ms).

