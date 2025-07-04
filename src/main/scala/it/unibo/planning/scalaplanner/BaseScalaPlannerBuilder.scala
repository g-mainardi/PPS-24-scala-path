package it.unibo.planning.scalaplanner

import it.unibo.planning.{PathFindingAlgorithm, Plan, PlannerBuilder, ScalaBuilder}

class BaseScalaPlannerBuilder extends ScalaBuilder:
  protected var algorithm: Option[PathFindingAlgorithm] = None
  
  
  override def withAlgorithm(algorithm: PathFindingAlgorithm): PlannerBuilder =  
    this.algorithm = Some(algorithm)
    this
  

  override def run: Plan = ???

  


object BaseScalaPlannerBuilder:
  def apply(): BaseScalaPlannerBuilder = new BaseScalaPlannerBuilder()
