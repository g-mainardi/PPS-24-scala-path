# Sprint 2

| Priority |      Item       |                          Tasks                          | Assignee | Initial Size Estimate | Day 1 | Day 2 | Day 3 | Day 4 | Day 5 | Day 6 | Day 7 |
|:--------:|:---------------:|:-------------------------------------------------------:|:--------:|:---------------------:|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|
|    1     |   Controller    |    Controller Design pt2 (Dividing State Management)    | Mainardi |           6           |   4   |   2   |   0   |   0   |   0   |   0   |   0   |
|    2     |      View       | View Design (Handling different button press sequences) | Basigli  |           6           |   4   |   2   |   2   |   2   |   2   |   2   |   0   |
|    3     |     Planner     |                   Planner with Tiles                    |  Salman  |           4           |   2   |   0   |   0   |   0   |   0   |   0   |   0   |
|    4     |     Builder     |               Planner Builder Abstraction               |  Salman  |           2           |   2   |   2   |   2   |   2   |   0   |   0   |   0   |
|    5     |     Planner     |                     A* Search Algo                      | Basigli  |           8           |   8   |   8   |   6   |   4   |   4   |   2   |   0   |
|    6     |     Planner     |                     Dj Search Algo                      | Mainardi |           8           |   8   |   8   |   6   |   4   |   4   |   0   |   0   |
|    7     |   Integration   |       Planner MaxMove Refactor (Fully-Relational)       |  Salman  |           2           |   2   |   2   |   0   |   0   |   0   |   0   |   0   |
|    8     |      Test       |           Test for View, Controller, Planner            |   All    |           2           |   2   |   2   |   2   |   0   |   0   |   0   |   0   |
|    9     |  Documentation  |          Documenting View, Controller, Planner          |   All    |           2           |   2   |   2   |   2   |   2   |   0   |   0   |   0   |


### Sprint Goal
Start Date: 30/6/25
<br/>
End Date: 6/7/25

The aim of the sprint is to finalize the controller and view design, and to implement a planner that can handle obstacles and tiles.
Secondly, we aim to add also two more major algorithm for pathfinding.
If feasible, we aim to add variability and flexibility to the scenarios generation, so that we can have different variants of the maze, terrain and traps scenarios.

### Sprint Review
We have done a lot of refactoring to improve the overall design, making it more flexible, extensible and modular.
We have found ourselves spending a lot of time discussing design choices, cleaner code and different styles to achieve more conciseness.
Overall, we have refined the code we have built in the first sprint, creating a more solid foundation for next steps.
We have achieved an extended and modular planner builder, and we have added a partial implementation of two more algorithms for pathfinding: A* and Dijkstra.

### Sprint Retrospective
We have discovered that code can always become more clean and elegant, and design can always become more modular and flexible, 
therefore improvement and refactoring are a never ending process.


[Previous Sprint](sprint1.md) | [Index](../index.md) | [Next Sprint](sprint3.md)

