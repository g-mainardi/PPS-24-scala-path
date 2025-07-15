package it.unibo.planning

import it.unibo.model.Direction
import it.unibo.model.Tiling.{Position, Tile}
import it.unibo.utils.PrettyPrint

trait PathFindingAlgorithm extends PrettyPrint:
  def run(start: Position, goal: Position, tiles: List[Tile], directions: List[Direction]): Option[List[Direction]]

enum Algorithm:
  case BFS
  case DFS
  case AStar