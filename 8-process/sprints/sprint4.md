# Sprint 4
| Priority |     Item      |                  Tasks                   | Assignee | Initial Size Estimate | Day 1 | Day 2 | Day 3 | Day 4 | Day 5 | Day 6 | Day 7 |
|:--------:|:-------------:|:----------------------------------------:|:--------:|:---------------------:|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|
|    1     |      GUI      |           Configuration Menus            | Basigli  |           6           |   6   |   4   |   2   |   0   |   0   |   0   |   0   |
|    2     |  Controller   |      Handling Configuration Inputs       | Mainardi |           4           |   2   |   0   |   0   |   0   |   0   |   0   |   0   |
|    3     |     Model     |         Model & Agents Refactor          |  Salman  |           6           |   4   |   2   |   0   |   0   |   0   |   0   |   0   |
|    4     |  Controller   | Sugar Refactor for Controller Structure  | Mainardi |           4           |   4   |   2   |   0   |   0   |   0   |   0   |   0   |
|    5     |     Test      |     ASCII art DSL to tests scenarios     | Basigli  |           4           |   4   |   4   |   4   |   2   |   0   |   0   |   0   |
|    6     |     Model     | Specials Adaptation to Scenario Refactor |  Salman  |           4           |   4   |   4   |   2   |   0   |   0   |   0   |   0   |
|    7     | Documentation |                Scala Doc                 |   Team   |          10           |  10   |  10   |   7   |   4   |   0   |   0   |   0   |
|    8     | Documentation |               Final Report               |   Team   |          10           |  10   |  10   |   7   |   4   |   0   |   0   |   0   |

### Sprint Goal
Start Date: 14/7
<br/>
End Date: 20/7

In this final sprint we aim to complete the controller and the GUI adding the possibility to configure the scenario and the agent. 
We also need to refactor and expand the agent concept, that we have neglected so far.
There are some sugar refactors that can be done in the controller, if time allows.
We also aim to build and ASCII-art DSL to tests scenario generation.


### Sprint Review
In this sprint we managed to add many functionalities to the GUI and the corresponding management in the controller. 
We added the possibility to choose the directions, the scenario dimensions and the init and goal positions.
The scenario object has been separated in different parts, because it had too many responsibilities, like managing init e goal positions.
The agent concept has been refactored and is now a more autonomous concept, it's no longer encapsulated in other objects.
The Planner has been adapted to return and agent which includes the plan to execute.

### Sprint Retrospective
We have learned that after many refactors during previous sprints, the code is now more flexible and it was easy to add new functionalities.
In fact, this was maybe the sprints with the most functionalities added. 
We have worked on code almost until the end because there were always some fixes or little improvements to do.
This wasn't good, there were important changes and features that we could have added before this sprint instead of the last week.
We haven't noticed that some classes still have an old designed that needed to be updated.

[Previous Sprint](sprint3.md) | [Index](../index.md)