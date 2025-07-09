package it.unibo.planning.scalaplanner

import it.unibo.model.Tiling.Position
import it.unibo.planning.Plan.*
import it.unibo.planning.{PathFindingAlgorithm, Plan, PlannerBuilder}


class BaseScalaPlannerBuilder extends ScalaBuilder:
  protected var algorithm: Option[PathFindingAlgorithm] = None
  
  
  override def withAlgorithm(algorithm: PathFindingAlgorithm): PlannerBuilder =  
    this.algorithm = Some(algorithm)
    this
  

  override def run: Plan =
    var start = this.initPos.get
    var goal = this.goalPos.get


    this.directions = this.algorithm.get.run(Position(start._1, start._2), Position(goal._1, goal._2), this.environmentTiles.get)
    val path = this.directions.get
    var actor = Position(start._1, start._2)
    path.foreach(i => actor + i.vector)
    if actor == Position(goal._1, goal._2) then
      SucceededPlan(path)
    else
      FailedPlan("Impossible to reach the goal")




object BaseScalaPlannerBuilder:
  def apply(): BaseScalaPlannerBuilder = new BaseScalaPlannerBuilder()
