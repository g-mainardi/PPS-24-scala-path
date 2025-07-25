package it.unibo.model.planning.scalaplanner

import it.unibo.model.fundamentals.Position
import it.unibo.model.agent.Agent
import it.unibo.model.planning.{Configuration, Plan, Planner, ScalaPlanner}

class ScalaBuilder(using configuration: Configuration):
  def build: Planner = 
    ScalaPlanner(configuration.initPos, configuration.goalPos, configuration.environmentTiles.tiles, configuration.directions, configuration.algorithm.get)