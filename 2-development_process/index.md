# Development Process

## Scrum
During development the team used a SCRUM-inspired development strategy, 
which consists in a division of the whole process in weekly sprints and 
taking on roles such as product owner and customer.

Our SCRUM-like development strategy followed these steps:
1. **Initial meeting**: the initial meeting took three hours, during which we assigned the role of "Product Owner", tasked with maximizing return on investment, to Gabriele Basigli; while Giosue' Mainardi took on the role of customer, who is tasked with providing feedback on the current state of the project. It's also defined a "Product Backlog", a document containing every step and its approximated difficulty needed to complete the product, 
2. **Sprint planning**: each sprint starts with a meeting where the previous sprint is discussed and used for assessing the work done, the work left and how to organize it. Each developer then takes a task from the product backlog to work on until the next sprint planning.
3. **Standup meeting**: On a daily basis, the team meets up to give updates on work: done, pending or to add.

## Version Control System
In this development process Git and Github were used as our version control system.
Before the development process started, utilization standards were formalized for usage of these tools namely:
* **Git Flow**: development was divided in different branches, each with a precise function and usage:
  * **main** branch: only used for major versioning of the project, always kept in a working state with all test passing
  * **develop** branch: used to channel code flows from feature branches into a common codebase to test the system altogether and to resolve conflicts
  * **feature** branches: used to develop a specific feature, often used by a single developer
  * **refactor** branches: used for major refactors of the code or the architecture
  * **gh-pages** branch: only used to store and render the documentation

* **Commit nomenclature**: in order to maintain an easy to navigate commit history, a precise commit nomenclature has been decided:
    
  `type: message` where "type" can be one of the following:

  *  **feat**: a new feature has been added
  *  **fix**: a bug has been fixed
  *  **docs**: updates to the documentation
  *  **style**: code has been conformed to stylistic standards
  *  **refactor**: code's structure has been rearranged
  *  **test**: adding new tests or correcting old tests
  *  **chore**: changes to libraries or configurations, general maintenance

Moreover, semantic versioning has been used to tag specific commits at least at the end of each sprint. 
Sometimes when have pushed semantic tags also in the middle of a sprint to mark a specific working state of the project. 

### Automation
A build automation tool was chose to ease the development process, `SBT` was chosen for its ease-of-use for the scala language.
Continuous Integration was implemented using GitHub Actions. Automated unit tests run for each new pushed commit. 
Instead, when a tag is pushed (using semantic versioning) a executable fat jar is produced and saved on GitHub as artifact.
Finally, to measure test coverage, the `scoverage` plugin for `SBT` has been adopted, aiming at a 60% test coverage.

| [Previous Chapter](../1-introduction/index.md) | [Index](../index.md) | [Next Chapter](../3-requirements/index.md) |
