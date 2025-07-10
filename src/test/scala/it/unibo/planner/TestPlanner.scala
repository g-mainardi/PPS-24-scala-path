package it.unibo.planner

import it.unibo.model.Tiling.*

import scala.util.Random

trait TestPlanner {
  val gridrows: Int = 3
  val gridcols: Int = 3

  val passableTiles: List[Tile] = (
    for {
      x <- 0 to gridrows
      y <- 0 to gridcols
    } yield Floor(Position(x, y))
    ).toList

  val blockingTiles: List[Tile] = (
    for {
      x <- 0 to gridrows
      y <- 0 to gridcols
    } yield Wall(Position(x, y))
    ).toList

  val specialTiles: List[Tile] = (
    for {
      x <- 0 to gridrows
      y <- 0 to gridcols
    } yield {
      val pos = Position(x, y)
      if (x == 1 && y == 1) Teleport(pos)
      if (x == 2 && y == 2) Teleport(pos)
      else Floor(pos)
    }).toList
    }
