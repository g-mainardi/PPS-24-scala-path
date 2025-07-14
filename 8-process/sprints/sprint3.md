# Sprint 3

| Priority |     Item     |                Tasks                | Assignee | Initial Size Estimate | Day 1 | Day 2 | Day 3 | Day 4 | Day 5 | Day 6 | Day 7 |
| :------: | :-----------: | :---------------------------------: | :------: | :-------------------: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|    1    |    Planner    |             A* Testing             | Basigli |           4           |   4   |   -   |   -   |   -   |   -   |   -   |   -   |
|    2    |    Planner    |           BFS Search Algo           | Mainardi |           2           |   5   |   3   |   1   |   0   |   0   |   0   |   0   |
|    3    |    Planner    |  Planner support for Special Tiles  |  Salman  |           2           |   0   |   0   |   0   |   0   |   0   |   -   |   -   |
|    4    |    Design    |     Mixing Different Scenarios     |  Salman  |           4           |   4   |   4   |   4   |   4   |   4   |   -   |   -   |
|    6    |    Design    |        Custom Special Tiles        |  Salman  |           4           |   4   |   2   |   0   |   -   |   -   |   -   |   -   |
|    7    |  Integration  |             A* Builder             | Basigli |           2           |   2   |   -   |   -   |   -   |   -   |   -   |   -   |
|    8    |  Integration  |             BFS Builder             |  Salman  |           4           |   2   |   0   |   -   |   -   |   -   |   -   |   -   |
|    9    |  Controller  |           Buttons on/off           | Mainardi |           2           |   2   |   1   |   2   |   0   |   0   |   0   |   0   |
|    10    | Documentation |    Model, View, Control, Builder    |   Team   |           6           |   6   |   -   |   -   |   -   |   -   |   -   |   -   |
|    11    |     View     |     Agent and algorithm chooser     | Basigli |           3           |   1   |   -   |   -   |   -   |   -   |   -   |   -   |
|    12    |  Controller  |    Agent and algorithm switcher    | Mainardi |           3           |   3   |   1   |   1   |   0   |   0   |   0   |   0   |
|    13    | Documentation | Requirements and domain explanation | Basigli |           4           |   4   |   2   |   -   |   -   |   -   |   -   |   -   |
|    14    |    Builder    |    Refactor with multiple traits    |  Salman  |           6           |   6   |   6   |   4   |   2   |   0   |   -   |   -   |

### Sprint Goal
Start Date: 7/7/25
<br/>
End Date: 13/7/25

In this sprint we aim to complete the two major algorithms for pathfinding: A* and BFS.
We should also start discussing how to structure different agents and how to integrate their behavior.
We plan to expand the concept of special tiles and add the possibility to create custom specials.
We also aim to add variability and flexibility to the scenarios generation or in the planner configuration,
so that we can have different variants of the same scenario of different configurations for the same planning algorithm.

### Sprint Review
We actually spent most of the time redesign and refactor code, to make it more modular and flexible, so that new features are easier to implement. 
We have spent time fixing bugs in the controller-view interaction after adding more buttons and dropdown menu.
The Planner builder has been completely refactored to enforce an order in which methods can be called, and to improve modularity when creating child builders.
The BFS algorithm was re-designed to have the same interface of the DFS algorithm, with a fully-relational parameter.

### Sprint Retrospective
Time really flies when you do serious refactors.

[Previous Sprint](sprint2.md) | [Index](../index.md) | [Next Sprint](sprint4.md)
