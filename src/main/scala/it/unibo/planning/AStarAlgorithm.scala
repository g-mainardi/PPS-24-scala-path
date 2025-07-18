package it.unibo.planning

import it.unibo.model.Direction
import it.unibo.model.Tiling.{Obstacle, Passage, Position, Tile}

import scala.annotation.tailrec

object AStarAlgorithm extends PathFindingAlgorithm:

  /**
   *
   * @param cameFrom
   * @param current
   * @return
   */
  private def reconstructPath(cameFrom: Map[Position, Position], current: Position): List[Direction] =
    @tailrec
    def _reconstructPath(pos: Position, acc: List[Direction]): List[Direction] =
      cameFrom.get(pos) match
        case Some(prev) =>
          val delta = pos - prev
          val dir = Direction.allDirections.find(_.vector == delta).get   // todo use a method inside Direction (like getFromVector)
          _reconstructPath(prev, dir :: acc)
        case None => acc

    _reconstructPath(current, Nil)


  /**
   * Computes the distance between two points on a 2D grid.
   * @param start
   * @param target
   * @return the distance as a Double
   */
  private def heuristic(start: Position, target: Position): Double =
    //(start.x - target.x).abs max (start.y - target.y).abs
    (start.x - target.x)*(start.x - target.x) + (start.y - target.y)*(start.y - target.y)


  /**
   * Maybe this function will become a parameter in the future
   * @param pos
   * @return
   */
  def neighbors(pos: Position, directions: List[Direction]): List[Position] =
    directions.map(dir => pos + dir.vector)


  override def run(start: Position, goal: Position, tiles: List[Tile], directions: List[Direction]): Option[List[Direction]] =
    val initG = Map(start -> 0.0)
    val initF = heuristic(start, goal)
    val passable = (position: Position) =>
      val tile = tiles.find(t => t.x == position.x && t.y == position.y)
      tile match
        case Some(_: Passage) => true
        case Some(_: Obstacle) => false
        case None => false


    given Ordering[(Double, Position)] = Ordering.by(-_._1)
    @tailrec
    def _run(openSet: Set[Position], cameFrom: Map[Position, Position], gScore: Map[Position, Double], fQueue: List[(Double, Position)]): Option[List[Direction]] = fQueue match
      case Nil => None
      case (_, current) :: rest if current == goal => Some(reconstructPath(cameFrom, current))
      case (_, current) :: rest =>
        var newOpen = openSet - current
        var updatedCameFrom = cameFrom
        var updatedGScore = gScore
        var updatedQueue = rest

        for neighbor <- neighbors(current, directions).filter(passable) do
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
