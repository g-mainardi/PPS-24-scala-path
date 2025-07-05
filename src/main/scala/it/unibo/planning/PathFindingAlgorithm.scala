package it.unibo.planning

import it.unibo.model.Direction
import it.unibo.model.Tiling.{Position, Tile}

trait PathFindingAlgorithm:
  def run(start: Position, goal: Position, tiles: List[Tile]): Option[List[Direction]]