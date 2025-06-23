% Basic planner
% mine predicates for expansion
gridbound(3).

init(s(0, 0)). % initial condition
goal(s(B, B)):- gridbound(B).

% move(?Dir, @Pos, ?NewPos)
move(up   , s(X, Y), s(X2, Y)):- X > 0, X2 is X - 1.
move(down , s(X, Y), s(X2, Y)):- gridbound(B), X < B, X2 is X + 1.
move(left , s(X, Y), s(X, Y2)):- Y > 0, Y2 is Y - 1.
move(right, s(X, Y), s(X, Y2)):- gridbound(B), Y < B, Y2 is Y + 1.

% plan(+MaxN, -Trace)
% produce the Trace (i.e. a list of commands) that, starting from (0 ,0) moves the robot to (3 ,3) 
% plan should use move in order to create a list of commands (no longer than MaxN)
plan(N, T):- init(P), plan(N, T, P).
plan(_, [], P):- goal(P), !.
plan(0, [], _):- !, fail.
plan(N, [Cmd|T], P):- move(Cmd, P, Pn), Nn is N - 1, plan(Nn, T, Pn).

% Improvements

% validcoord(?Coordinate)
% => {0,1,2,3}
validcoord(C):- validcoord(0, C).
validcoord(A, A).
validcoord(A, X):- gridbound(B), A < B, A2 is A + 1, validcoord(A2, X).

% validpos(?Pos)
% => {s(0,0), s(0,1), ..., s(3,3)}
validpos(s(X, Y)):- validcoord(X), validcoord(Y).

% freepos(?Pos)
% => {s(0,1), s(0,2), ..., s(3,2)}
freepos(P):- validpos(P),	not(init(P)), not(goal(P)).

% utils to check numbers (not fully relational)
even(0).
even(N):- number(N), N > 1, N1 is N - 2, even(N1), !.
odd(N):- 	number(N), N > 0, not(even(N)).

% loose(+Pos)	
% => {s(0, 1), s(0, 3), s(2,1), s(2,3)}
loose(P):-
	freepos(P), copy_term(P, s(X, Y)),
	even(X), odd(Y). % losing conditions

% trampoline(+Pos, -NewPos)
% => Pos in {s(1, 3), s(3, 1)}
% In determined positions, the robot will jump back in X or Y of 2
trampoline(P, Pn):-
	freepos(P), not(loose(P)), copy_term(P, s(X, Y)),
	odd(X),	odd(Y),
	back(P, Pn).

% back(+Pos, -NewPos)
% => back(s(1,3), s(1,1))
% A position with a new one with X or Y diminished by 2
back(s(X, Y), P):- validcoord(X), Xn is X - 2, P = s(Xn, Y),	validpos(P).
back(s(X, Y), P):- validcoord(Y), Yn is Y - 2, P = s(X, Yn),	validpos(P).

% double move bonus available
bonus(1).

% jump(?Dir, @Pos, ?NewPos)
% => Get commands that rapresents using move two times
jump(up2   , P, P2):- move(up, P, P1)   , move(up, P1, P2).
jump(down2 , P, P2):- move(down, P, P1) , move(down, P1, P2).
jump(left2 , P, P2):- move(left, P, P1) , move(left, P1, P2).
jump(right2, P, P2):- move(right, P, P1), move(right, P1, P2).

% planV2(+MaxN, -Trace)
% produce the Trace (i.e. a list of commands) that, starting from (0 ,0) moves the robot to (3 ,3) 
% plan should use move in order to create a list of commands (no longer than MaxN)
planV2(N, T):- init(P), bonus(B), planV2(N, T, P, B).
planV2(_, [], P, _):- goal(P), !.
planV2(0, [], _, _):- !, fail.
planV2(_, [], P, _):- loose(P), !, fail.
planV2(N, T, P, B):- trampoline(P, Pn), !, planV2(N, T, Pn, B).
planV2(N, [Cmd|T], P, B):- move(Cmd, P, Pn), Nn is N - 1, planV2(Nn, T, Pn, B).
planV2(N, [Cmd|T], P, B):- 
	B > 0, Bn is B - 1,
	jump(Cmd, P, Pn), validpos(Pn),
	Nn is N - 1, planV2(Nn, T, Pn, Bn).

% equivalent version with double use of move 
%planV2(N, [Cmd, Cmd|T], P, B):- 
%	B > 0, Bn is B -1,
%	move(Cmd, P, P1), validpos(P1), move(Cmd, P1, P2), validpos(P2),
%	Nn is N - 1, planV2(Nn, T, P2, Bn).