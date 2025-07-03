package it.unibo.prologintegration

import it.unibo.model.Tiling.{Floor, Position, Tile, Wall}

trait TestPlanner {
  val passableTiles: List[Tile] = (
    for {
      x <- 0 to 2
      y <- 0 to 2
    } yield Floor(Position(x, y))
    ).toList

  val blockingTiles: List[Tile] = (
    for {
      x <- 0 to 2
      y <- 0 to 2
    } yield Wall(Position(x, y))
    ).toList

}
