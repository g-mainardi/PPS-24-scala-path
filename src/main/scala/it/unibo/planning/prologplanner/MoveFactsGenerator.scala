package it.unibo.planning.prologplanner

import it.unibo.model.{Direction, Special}
import it.unibo.model.Direction.{Cardinals, Diagonals}

object MoveFactsGenerator {
  def generateMoveRules(selectedDirections: List[Direction]): String =
    def toMoveFact(direction: Direction): String =
      val dx = direction.vector.x
      val dy = direction.vector.y
      val directionName = direction.toString.head.toLower + direction.toString.tail // e.g. RightUp -> rightUp

      val xExpr = dx match
        case 0 => "X"
        case dx if dx > 0 => s"X + $dx"
        case dx if dx < 0 => s"X - ${-dx}"

      val yExpr = dy match
        case 0 => "Y"
        case dy if dy > 0 => s"Y + $dy"
        case dy if dy < 0 => s"Y - ${-dy}"

      val xDecl = if dx != 0 then "X1 is " + xExpr else ""
      val yDecl = if dy != 0 then "Y1 is " + yExpr else ""
      val xNew = if dx != 0 then "X1" else "X"
      val yNew = if dy != 0 then "Y1" else "Y"

      val bodyLines = List(xDecl, yDecl).filter(_.nonEmpty) :+ s"passable($xNew, $yNew)."
      val body = bodyLines.mkString(",\n    ")
      s"""move(s(X,Y), $directionName, s($xNew, $yNew)) :-\n    $body"""

    selectedDirections.map(toMoveFact).mkString("\n", "\n\n", "\n")
}
