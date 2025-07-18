package it.unibo.controller

import it.unibo.model.scenario.{EmptyScenario, Scenario}

class MockDisplayableController extends DisplayableController {
  override def scenario: Scenario = new EmptyScenario(20, 20)
  protected def assembleAgent(): Unit = {}
  protected def handleNoPlan(errorMessage: String): Unit = {}
  protected def handleValidPlan(nMoves: Option[Int]): Unit = {}
  protected def startSearch(): Unit = {}
}