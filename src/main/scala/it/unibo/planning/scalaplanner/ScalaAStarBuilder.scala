package it.unibo.planning.scalaplanner

import it.unibo.model.Tiling.Position
import it.unibo.planning.{Configuration, Planner, ScalaPlanner}

class ScalaAStarBuilder(configuration: Configuration) extends BaseScalaBuilder:
  def build: Planner =
    ScalaPlanner(Position(configuration.initPos), Position(configuration.goalPos), configuration.environmentTiles, configuration.directions, configuration.algorithm.get)