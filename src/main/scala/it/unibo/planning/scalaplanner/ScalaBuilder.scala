package it.unibo.planning.scalaplanner

import it.unibo.model.Agent
import it.unibo.model.Tiling.Position
import it.unibo.planning.{Configuration, Plan, Planner, ScalaPlanner}
import it.unibo.planning.prologplanner.Conversions.given_Conversion_Int_Int_Position

class ScalaBuilder(using configuration: Configuration):
  def build: Planner = 
    ScalaPlanner(configuration.initPos, configuration.goalPos, configuration.environmentTiles.tiles, configuration.directions, configuration.algorithm.get)
  
    