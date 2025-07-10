package it.unibo.planning.prologplanner

class TempStuff {

  // extension Method per mappare ai blocchi moves salvati su file?
  // oppure generazione dinamica a runtime, così non serve sapere a priori le dirs?
  // Tanto le dirs sono più statiche rispetto ai tiles
//  extension (direction: Direction)
//    def moveFact: String = direction match
//      case Cardinals.Down => Source.fromFile("src/main/prolog/moves/down.pl").mkString
//      case Cardinals.Up => Source.fromFile("src/main/prolog/moves/up.pl").mkString
//      case Cardinals.Left => Source.fromFile("src/main/prolog/moves/left.pl").mkString
//      case Cardinals.Right => Source.fromFile("src/main/prolog/moves/right.pl").mkString
//      case Diagonals.LeftDown => Source.fromFile("src/main/prolog/moves/downLeft.pl").mkString
//      case Diagonals.RightDown => Source.fromFile("src/main/prolog/moves/downRight.pl").mkString
//      case Diagonals.LeftUp => Source.fromFile("src/main/prolog/moves/upLeft.pl").mkString
//      case Diagonals.RightUp => Source.fromFile("src/main/prolog/moves/upRight.pl").mkString

}

//private object IncompletePlannerConfig:
//  def unapply(config: Configuration): Option[String] =
//    val labels = List("init position", "goal", "environmental tiles", "possible directions", "theory path")
//    val options = List(config.initPos, config.goalPos, config.environmentTiles, config.directions, config.theoryPath)
//    labels.zip(options).collectFirst {
//      case (label, None) => s"missing $label"
//    }

//  private object Directions:
//    def unapply(o: Option[List[Direction]]): Option[String] = o map (direction => direction.map {
//      case c: Cardinals => s"cardinals(${c.toString.toLowerCase})."
//      case d: Diagonals => s"diagonals(${d.toString.toLowerCase})."
//    }.mkString("\n"))

//private object Directions:
//  def unapply(o: Option[List[Direction]]): Option[String] = o map { directions =>
//    val hasCardinals = directions.exists(_.isInstanceOf[Cardinals])
//    val hasDiagonals = directions.exists(_.isInstanceOf[Diagonals])
//    val facts = directions.map {
//      case c: Cardinals => s"cardinals(${c.toString.toLowerCase})."
//      case d: Diagonals => s"diagonals(${d.toString.head.toLower + d.toString.tail})."
//      case s: Special => s"special(${s.toString.toLowerCase})."
//    }
//    val header = Seq(
//      if (hasCardinals) Some("directions(D):- cardinals(D).") else None,
//      if (hasDiagonals) Some("directions(D):- diagonals(D).") else None
//    ).flatten
//    (facts ++ header).mkString("\n") + generateMoveRules(directions)
//  }
