# ScalaBuilder
Lo `ScalaBuilder` è la classe che concretamente assembla il Planner in Scala con le configurazioni raccolte tramite il Planner Builder. Utilizzando il parametro `configuration` per istanziare `ScalaPlanner` con la posizione iniziale, il goal, le tile che compongono lo scenario, la sequenza delle possibili direzioni e l'algoritmo da utilizzare.
Questo incapsulamento permette di seguire il principio open/closed, per cui si ha un sistema aperto alle estensioni ma chiuso alle modifiche. Lo `ScalaBuilder` permette infatti di lavorare con diversi tipi di algoritmi di pathfinding. In futuro per aggiungere un nuovo algoritmo, non sarà necessario modificare la logica di costruzione del planner ma basterà aggiungere un modulo che estende l'interfaccia `PathFindingAlgorithm`.

```scala
class ScalaBuilder(using configuration: Configuration):
  def build: Planner = 
    ScalaPlanner(configuration.initPos, 
      configuration.goalPos, 
      configuration.environmentTiles.tiles, 
      configuration.directions, 
      configuration.algorithm.get)
```
La classe `ScalaPlanner` esegue l'algoritmo con i parametri forniti e ritorna il `Plan` che contine la sequenza di direzioni che l'agente dovrà seguire.
```scala
class ScalaPlanner(start: Position,
                   goal: Position, 
                   tiles: Seq[Tile], 
                   directions: Seq[Direction], 
                   algorithm: PathFindingAlgorithm)(using configuration: Configuration) extends Planner with BaseScalaPlanner:
  override def plan: Plan =
    checkSolution(algorithm.run(start, goal, tiles, directions))
```
[Index](../index.md)
