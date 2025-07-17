package it.unibo.model.planning.algorithms

import it.unibo.model.fundamentals.{Direction, Position, Tile}
import it.unibo.utils.PrettyPrint

trait PathFindingAlgorithm extends PrettyPrint:
  def run(start: Position, goal: Position, tiles: List[Tile], directions: List[Direction]): Option[List[Direction]]

enum Algorithm:
  case BFS
  case DFS
  case AStar