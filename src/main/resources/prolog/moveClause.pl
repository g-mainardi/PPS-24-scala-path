move(s(X,Y), Dir, s(X1,Y1)) :-
    delta(Dir, DX, DY),
    X1 is X + DX,
    Y1 is Y + DY,
    passable(s(X1,Y1)).
