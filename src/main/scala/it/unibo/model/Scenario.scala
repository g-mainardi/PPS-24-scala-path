package it.unibo.model

import Tiling.Tile

object Scenario:
  val nRows = 10
  val nCols = 10

trait Scenario: 
  def generateScenario(): List[Tile]
