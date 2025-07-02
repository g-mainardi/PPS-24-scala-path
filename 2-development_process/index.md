---
title: Development Process
---

# Development Process

## Scrum

During development the team used a SCRUM-inspired development strategy, 
which consists in a division of the whole process in weekly sprints and 
taking on roles such as product owner and customer.

Our SCRUM-like development strategy followed these steps:
1. **Initial meeting**: the initial meeting took four hours, during which we assigned the role of "Product Owner", tasked with maximizing return on investment, to Marco Pesic; while Alessandro Agosta took on the role of customer, who is tasked with providing feedback on the current state of the project. It's also defined a "Product Backlog", a document containing every step and its approximated difficulty needed to complete the product, 
2. **Sprint planning**: each sprint starts with a meeting where the previous sprint is discussed and used for assessing the work done, the work left and how to organize it. Each developer then takes a task from the product backlog to work on until the next sprint planning.
3. **Standup meeting**: On a daily basis, the team meets up to give updates on work: done, pending or to add.

## Test-Driven Development
Test-Driven Development or TDD is a development strategy which dictates that for every feature a test must be written before even implementing such feature.
For each feature then, TDD is applied in three steps:
1. "Red": a test is written in order to outline the main effects and desired behaviours of a feature
2. "Green": an initial implementation is developed, minimal for passing tests
3. "Refactor": cleanup and refactoring of code, which doesn't affect tests

## Version Control System
A version control system was needed to organize, centralize and manage code between its developers. In this development process Git and Github were used as our version control system.
These tools allow to save and describe changes in the form of a "commit". Before the development process started, utilization standards were formalized for usage of these tools namely:
* **Git Flow**: development was divided in three main branches, each with a precise function and usage:
  * **main** branch: only used for major versioning of the project
  * **develop** branch: used to channel code flows from developers into a common codebase to test the system altogether
  *  **gh-pages** branch: only used for changes to the documentation

* **Commit nomenclature**: in order to maintain an easy to navigate commit history, a precise commit nomenclature has been decided where each commit is structured as such 

    `[type]: message` where "type" can be one of the following:

  * **feat**: a new feature has been completed
  *  **fix**: a bug has been fixed
  *  **docs**: updates to the documentation
  *  **style**: code has been conformed to stylistic standards
  *  **refactor**: code's structure has been rearranged with no impact to its functionalities
  *  **test**: adding new tests or correcting old tests
  *  **chore**: changes to libraries or configurations

## Code Formatting
In order to keep consistent code-formatting standards, the team chose to use ScalaFMT, a tool that dictates formatting standards and applies them to each line of code generated.
Our project's ScalaFTM configuration can be found [here](https://github.com/Agostax0/PPS-24-Briscala/blob/master/.scalafmt.conf).

## Build Automation
A build automation tool was chose to ease the development process, [SBT](https://www.scala-sbt.org) (Simple Build Tool) was chose for its ease-of-use and its ample plugin library.

## Continuous Integration
Continuous Integration was implemented using GitHub-native workflows, which are tasked with continuous testing at every change/commit on clean machines with varying operative systems.
These configurations can be found [here](https://github.com/Agostax0/PPS-24-Briscala/tree/master/.github/workflows).


| [Previous Chapter](../1-introduction/index.md) | [Index](../index.md) | [Next Chapter](../3-requirements/index.md) |
