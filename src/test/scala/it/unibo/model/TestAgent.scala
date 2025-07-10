package it.unibo.model

import it.unibo.model.Direction.Cardinals
import it.unibo.model.Tiling.Position
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestAgent extends AnyFlatSpec with Matchers:
  val scenario: Scenario = new DummyScenario()

  "An Agent" should "have initial position as defined" in :
    val initialPosition = Position(1, 1)
    val agent: Agent = Agent(initialPosition, scenario.getTileAt)
    agent.pos shouldEqual initialPosition

  "An Agent" should "execute every Cardinals directions commands" in :
    val initialPosition = Position(1, 1)
    val agent: Agent = Agent(initialPosition, scenario.getTileAt)
    Map(
      Cardinals.Up    -> Position(1,0),
      Cardinals.Down  -> initialPosition,
      Cardinals.Right -> Position(2,1),
      Cardinals.Left  -> initialPosition
    ) foreach: (card, pos) =>
      agent computeCommand card
      agent.pos shouldEqual pos

  "An Agent" should "execute a Cardinals direction multiple times" in :
    val agent: Agent = Agent(Position(0, 0), scenario.getTileAt)
    1 to 5 foreach: i =>
      agent computeCommand Cardinals.Down
      agent.pos shouldEqual Position(0, i)