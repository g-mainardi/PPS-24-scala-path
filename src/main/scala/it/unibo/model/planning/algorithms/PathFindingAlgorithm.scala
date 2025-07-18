package it.unibo.model.planning.algorithms

import it.unibo.model.fundamentals.{Direction, Position, Tile}
import it.unibo.utils.PrettyPrint

trait PathFindingAlgorithm extends PrettyPrint:
  def run(start: Position, goal: Position, tiles: Seq[Tile], directions: Seq[Direction]): Option[Seq[Direction]]

enum Algorithm:
  case BFS
  case DFS
  case AStar