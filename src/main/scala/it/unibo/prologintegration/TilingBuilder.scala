package it.unibo.prologintegration

import it.unibo.model.Tiling.*

object TilingBuilder {
  def toPrologFacts(tiles: List[Tile]): String =
    tiles.map {
      case p: Passage => s"passable(${p.x}, ${p.y})."
      case o: Obstacle => s"blocked(${o.x}, ${o.y})."
    }.mkString("\n")
}

@main def testTilingFacts(): Unit =
  import it.unibo.model.Tiling.*

  val tiles = List(
    Floor(Position(0, 0)),
    Grass(Position(0, 1)),
    Trap(Position(1, 1)),
    Teleport(Position(2, 2))
  )

  val facts = TilingBuilder.toPrologFacts(tiles)
  println("Generated Prolog facts:\n" + facts)
