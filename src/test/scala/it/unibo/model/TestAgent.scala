package it.unibo.model

import it.unibo.model.fundamentals.Direction.{Cardinals, allDirections}
import it.unibo.model.fundamentals.Position
import it.unibo.model.agent.Agent
import it.unibo.model.fundamentals.{Direction, Position}
import it.unibo.model.planning.Plan
import it.unibo.model.planning.Plan.{SucceededPlan, FailedPlan, SucceededPlanWithMoves}
import it.unibo.model.scenario.SpecialTile
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestAgent extends AnyFlatSpec with Matchers:
  val scenarioSize = 5
  val directions: List[Direction] = List.fill(scenarioSize)(Cardinals.Down)
  val plan: Plan = SucceededPlan(directions)
  val planWithMoves: Plan = SucceededPlanWithMoves(directions, directions.length)
  val failedPlan: Plan = FailedPlan("plan not found")
  val ignoreSpecials: Position => Option[SpecialTile] = _ => None

  val initialPosition: Position = Position(1, 1)
  val agent: Agent = Agent(initialPosition, () => plan, ignoreSpecials)

  "An Agent" should "have initial position as defined" in :
    agent.pos should be (initialPosition)

  "An Agent" should "execute every Cardinals directions commands" in :
    Map(
      Cardinals.Up    -> Position(1,0),
      Cardinals.Down  -> initialPosition,
      Cardinals.Right -> Position(2,1),
      Cardinals.Left  -> initialPosition
    ) foreach: (card, pos) =>
      agent computeCommand card
      agent.pos should be (pos)

  "An Agent" should "execute a Cardinals direction multiple times" in :
    val agent: Agent = Agent(Position(0, 0), () => plan, ignoreSpecials)
    1 to 5 foreach: i =>
      agent computeCommand Cardinals.Down
      agent.pos shouldEqual Position(0, i)
  
  "An Agent" should "correctly collect and reset positions" in :
    object TestPathManager extends Agent(initialPosition, () => plan, ignoreSpecials):
      def testAdd(x: Int, y: Int, dir: Direction): Unit = addToPath(Position(x, y), dir)
      def testReset(): Unit = resetPath()
  
    // Initial state
    TestPathManager.path should be(empty)
  
    // Adding positions
    val dim: Int = 5
    for
      x <- 0 until dim
      y <- 0 until dim
      dir = allDirections((x + y) % allDirections.length)
    do
      TestPathManager testAdd(x, y, dir)
      TestPathManager.path.last should be(Position(x, y), dir)
    TestPathManager.path should have size dim * dim
  
    // Resetting
    TestPathManager.testReset()
    TestPathManager.path should be(empty)