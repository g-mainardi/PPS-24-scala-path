package it.unibo.model

import Tiling.Tile

trait Scenario: 
  def generateScenario(): List[Tile]
