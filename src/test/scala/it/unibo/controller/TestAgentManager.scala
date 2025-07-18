package it.unibo.controller

import it.unibo.model.agent.Agent
import it.unibo.model.exceptions.AgentNotBuiltException
import it.unibo.model.fundamentals.Tiling.Floor
import it.unibo.model.fundamentals.{Direction, Position, Tile}
import it.unibo.model.fundamentals.Direction.Cardinals.*
import it.unibo.model.planning.Plan
import it.unibo.model.planning.Plan.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestAgentManager extends AnyFlatSpec with Matchers:
  private val directions: Seq[Direction] = Up :: Down :: Right :: Left :: Nil

  class AgentManagerMock extends AgentManager:
    private var _searchStarted = false
    private var _planSuccess = false
    private var _planResult: Option[Int] = None
    private var _planFail: Option[String] = None
    private var _planSelected: Option[Plan] = None

    override protected def startSearch(): Unit = _searchStarted = true

    override protected def handleNoPlan(errorMessage: String): Unit = _planFail = Some(errorMessage)

    override protected def handleValidPlan(nMoves: Option[Int]): Unit =
      _planSuccess = true
      _planResult = nMoves

    override protected def assembleAgent(): Unit =
      val samePosToTile: Position => Option[Tile] = p => Some(Floor(p))
      _planSelected foreach: plan =>
        agent = Agent(Position(0, 0), () => plan, samePosToTile)

    private def assembleWith(plan: Plan): Unit =
      _planSelected = Some(plan)
      assembleAgent()

    def assembleAndSearchWith(plan: Plan): Unit =
      assembleWith(plan)
      searchPlan()

    def testSearch(): Unit = searchPlan()

    def testDrop(): Unit = dropAgent()

    def testAssemble(): Unit = assembleWith:
      SucceededPlan(Seq.empty)

    def testPlanOver: Boolean = planOver

    def testStep(): Unit = stepAgent()

    def searchStarted: Boolean = _searchStarted
    def planSuccess: Boolean = _planSuccess
    def planResult: Option[Int] = _planResult
    def planFail: Option[String] = _planFail

  it should "give an agent after assemble" in:
    val mockAM = AgentManagerMock()
    mockAM.testAssemble()
    mockAM.agent should not be empty

  it should "drop an agent" in :
    val mockAM = AgentManagerMock()
    mockAM.testAssemble()
    mockAM.testDrop()
    mockAM.agent shouldBe empty

  it should "give a plan" in :
    val mockAM = AgentManagerMock()
    mockAM assembleAndSearchWith:
      SucceededPlan(Seq.empty)

    mockAM.searchStarted shouldBe true
    mockAM.planSuccess shouldBe true
    mockAM.planResult shouldBe empty
    mockAM.planFail shouldBe empty

  it should "give a plan with moves" in :
    val mockAM = AgentManagerMock()

    mockAM assembleAndSearchWith:
      SucceededPlanWithMoves(directions, directions.length)

    mockAM.searchStarted shouldBe true
    mockAM.planSuccess shouldBe true
    mockAM.planResult shouldBe Some(directions.length)
    mockAM.planFail shouldBe empty

  it should "give a failed plan" in :
    val mockAM = AgentManagerMock()
    an[AgentNotBuiltException.type] should be thrownBy:
      mockAM.testSearch()

  it should "have plan over on absent agent" in:
    val mockAM = AgentManagerMock()
    mockAM.testPlanOver shouldBe true

    mockAM.assembleAndSearchWith:
      SucceededPlan(directions)
    mockAM.testPlanOver shouldBe false

    mockAM.testDrop()
    mockAM.testPlanOver shouldBe true

  it should "step the agent then plan over" in:
    val mockAM = AgentManagerMock()

    mockAM.assembleAndSearchWith:
      SucceededPlan(Seq.empty)
    mockAM.testPlanOver shouldBe true

    mockAM.assembleAndSearchWith:
      SucceededPlan(directions)
    mockAM.testPlanOver shouldBe false

    directions foreach: _ =>
      mockAM.testStep()
    mockAM.testPlanOver shouldBe true




