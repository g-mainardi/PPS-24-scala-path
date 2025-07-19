package it.unibo.controller


class MockDisplayableController extends DisplayableController:
  protected def assembleAgent(): Unit = {}
  protected def handleNoPlan(errorMessage: String): Unit = {}
  protected def handleValidPlan(nMoves: Option[Int]): Unit = {}
  protected def startSearch(): Unit = {}