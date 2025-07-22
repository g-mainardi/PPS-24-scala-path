package it.unibo.model.planning.algorithms

import it.unibo.model.fundamentals.Direction
import it.unibo.model.fundamentals.Tiling.{Obstacle, Passage, Special}
import it.unibo.model.fundamentals.{Position, Tile}
import it.unibo.model.planning.algorithms.PathFindingAlgorithm

import scala.annotation.tailrec

object AStarAlgorithm extends PathFindingAlgorithm:

  /**
   * Reconstructs the path from the start position to the goal position using the `cameFrom` map.
   * @param cameFrom a map that contains the previous position for each position in the path
   * @param current the current position
   * @param tiles the sequence of tiles in the scenario
   * @return a sequence of directions representing the path
   */
  private def reconstructPath(cameFrom: Map[Position, Position], current: Position, tiles: Seq[Tile]): Seq[Direction] =
    @tailrec
    def _reconstructPath(pos: Position, acc: List[Direction]): Seq[Direction] =
      cameFrom.get(pos) match
        case Some(prev) =>
          var delta = pos - prev
          if delta.x.abs > 1 || delta.y.abs > 1 then  // special case like a teleport
            val originalTile = tiles.find(
              t => t match
                case s: Special => s.newPos == pos
                case _ => false
            )
            originalTile match
              case Some(t) => delta = Position(t.x, t.y) - prev
              case _ => None
            // delta = Position(0, 0)
          Direction.allDirections.find(_.vector == delta) match
            case Some(dir) => _reconstructPath(prev, dir :: acc)
            case _ => _reconstructPath(prev, acc)
        case None => acc
    _reconstructPath(current, Nil)


  /**
   * Computes the distance between two points on a 2D grid.
   * @param start the starting position
   * @param target the target position
   * @return the distance as a Double
   */
  private def heuristic(start: Position, target: Position): Double =
    (start.x - target.x)*(start.x - target.x) + (start.y - target.y)*(start.y - target.y)


  /**
   * Returns the neighbors of a given position based on the provided directions.
   * @param pos the current position
   * @param directions the directions to consider for neighboring positions
   * @param tiles the sequence of tiles in the scenario
   * @return a sequence of neighboring positions
   */
  def neighbors(pos: Position, directions: Seq[Direction], tiles: Seq[Tile]): Seq[Position] =
    directions.flatMap { dir =>
      val candidate = pos + dir.vector
      tiles.find(t => t.x == candidate.x && t.y == candidate.y) match
        case Some(s: Special) => Seq(s.newPos)
        case Some(_)          => Seq(candidate)
        case _                => Seq.empty
    }

  /**
   * Runs the A* algorithm to find a path from the start position to the goal position.
   * @param start the starting position
   * @param goal the goal position
   * @param tiles the sequence of tiles in the scenario
   * @param directions the sequence of directions the agent can move
   * @return an optional sequence of directions representing the path from start to goal
   */
  override def run(start: Position, goal: Position, tiles: Seq[Tile], directions: Seq[Direction]): Option[Seq[Direction]] =
    val initG = Map(start -> 0.0)
    val initF = heuristic(start, goal)
    val passable = (position: Position) =>
      val tile = tiles.find(t => t.x == position.x && t.y == position.y)
      tile match
        case Some(_: Passage) => true
        case Some(_: Obstacle) => false
        case _ => false

    given Ordering[(Double, Position)] = Ordering.by(-_._1)
    @tailrec
    def _run(openSet: Set[Position], cameFrom: Map[Position, Position], gScore: Map[Position, Double], fQueue: List[(Double, Position)]): Option[Seq[Direction]] = fQueue match
      case Nil => None
      case (_, current) :: rest if current == goal => Some(reconstructPath(cameFrom, current, tiles))
      case (_, current) :: rest =>
        var newOpen = openSet - current
        var updatedCameFrom = cameFrom
        var updatedGScore = gScore
        var updatedQueue = rest

        for neighbor <- neighbors(current, directions, tiles).filter(passable) do
          val tentativeG = gScore(current) + 1
          val currentG = gScore.getOrElse(neighbor, Double.PositiveInfinity)

          if tentativeG < currentG then
            updatedCameFrom += (neighbor -> current)
            updatedGScore += (neighbor -> tentativeG)
            val f = tentativeG + heuristic(neighbor, goal)
            updatedQueue = (f, neighbor) :: updatedQueue
            newOpen = newOpen + neighbor

        _run(newOpen, updatedCameFrom, updatedGScore, updatedQueue.sortBy(_._1))

    _run(Set(start), Map(), initG, List((initF, start)))
