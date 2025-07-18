package it.unibo.controller

import it.unibo.model.agent.Agent
import it.unibo.model.fundamentals.Tiling.Floor
import it.unibo.model.fundamentals.{Position, Tile}
import it.unibo.model.planning.Plan
import it.unibo.model.planning.Plan.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestAgentManager extends AnyFlatSpec with Matchers:
  class AgentManagerMock extends AgentManager:
    private var _searchStarted = false
    private var _planSuccess = false
    private var _planResult: Option[Int] = None
    private var _planFail: Option[String] = None
    private var _planSelected: Plan = SucceededPlan(Seq.empty)

    override protected def startSearch(): Unit = _searchStarted = true

    override protected def handleNoPlan(errorMessage: String): Unit = _planFail = Some(errorMessage)

    override protected def handleValidPlan(nMoves: Option[Int]): Unit =
      _planSuccess = true
      _planResult = nMoves

    override protected def assembleAgent(): Unit =
      val samePosToTile: Position => Option[Tile] = p => Some(Floor(p))
      agent = Agent(Position(0, 0), () => _planSelected, samePosToTile)

    def testPlanWithMoves(n: Int): Unit =
      _planSelected = SucceededPlanWithMoves(Seq.empty, n)
      assembleAgent()
      searchPlan()

    def testPlan(): Unit =
      _planSelected = SucceededPlan(Seq.empty)
      assembleAgent()
      searchPlan()

    def testFailedPlan(msg: String): Unit =
      _planSelected = FailedPlan(msg)
      assembleAgent()
      searchPlan()

    def searchStarted: Boolean = _searchStarted
    def planSuccess: Boolean = _planSuccess
    def planResult: Option[Int] = _planResult
    def planFail: Option[String] = _planFail

  "An AgentManager" should "give a plan" in :
    val mockAM = AgentManagerMock()
    mockAM.testPlan()

    mockAM.searchStarted shouldBe true
    mockAM.planSuccess shouldBe true
    mockAM.planResult shouldBe empty
    mockAM.planFail shouldBe empty

  "An AgentManager" should "give a plan with no moves" in :
    val mockAM = AgentManagerMock()
    val nMoves = 0
    mockAM testPlanWithMoves nMoves

    mockAM.searchStarted shouldBe true
    mockAM.planSuccess shouldBe true
    mockAM.planResult shouldBe Some(nMoves)
    mockAM.planFail shouldBe empty

  "An AgentManager" should "give a failed plan" in :
    val mockAM = AgentManagerMock()
    val msg = "plan not found"
    mockAM.testFailedPlan(msg)

    mockAM.searchStarted shouldBe true
    mockAM.planFail shouldBe Some(msg)
    mockAM.planSuccess shouldBe false
    mockAM.planResult shouldBe empty


