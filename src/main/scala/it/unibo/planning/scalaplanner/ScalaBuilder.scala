package it.unibo.planning.scalaplanner

import it.unibo.model.Agent
import it.unibo.model.Tiling.Position
import it.unibo.planning.{Configuration, Plan, Planner, ScalaPlanner}
import it.unibo.planning.prologplanner.Conversions.given_Conversion_Int_Int_Position

class ScalaBuilder(configuration: Configuration):
  def build: Agent =
    val plan: Plan = ScalaPlanner(configuration.initPos, configuration.goalPos, configuration.environmentTiles.tiles, configuration.directions, configuration.algorithm.get).plan
    
    new Agent(
      configuration.initPos,
      plan,
      configuration.environmentTiles.checkSpecial,
    )
    
    