## Special Tiles

Per le caselle speciali, è stata costruita un API che permette di definire programmaticamente delle caselle speciali con un comportamento a scelta. 
Ad esempio è possibile definire una casella speciale che sposta l'agente in una posizione random, oppure una caseella che fa fare un salto di due caselle: 
```scala
val builder = new SpecialTileBuilder
    builder tile "Teleport" does (_ => Scenario.randomPosition)
    builder tile "JumpDown" does (pos => Position(pos.x, pos.y + 2))
    builder tile "StairsUp" does (pos => Position(pos.x, pos.y - 2))
```

E' stato implementato un builder che raccoglie in un Registry le definizioni di nuovi tipi di caselle speciali. 
```scala
object SpecialTileRegistry:
    var registry: Map[String, SpecialKind] = Map()
    def allKinds: Iterable[SpecialKind] = registry.values
    def clear(): Unit = registry = Map.empty

class SpecialTileBuilder:
    private var name: String = ""
    def tile(name: String): SpecialTileBuilder = this.name = name; this
    def does(compute: Position => Position): Unit = SpecialTileRegistry.registry += name -> SpecialKind(name, compute)
```

Dopodiché è stato implementato un scenario che mescola tutte le tipologie di caselle speciali presenti nel Registry, 
creando diverse istanze per ogni tipo in posizioni casuali: 
```scala
class Specials(nRows: Int, nCols: Int) extends Scenario(nRows, nCols):
    private val tilesPerKind = 4
    private val baseTiles = (for
      x <- 0 until nRows
      y <- 0 until nCols
    yield Floor(Position(x, y))).toList
    
    override def generate(): Unit =
    val specialPositions: Map[SpecialKind, Set[Position]] =
        SpecialTileRegistry.allKinds.map { kind =>
        kind -> Scenario.randomPositions(tilesPerKind)
        }.toMap
    
        _tiles = baseTiles.map:
        case TilePos(pos) =>
        specialPositions.collectFirst {
          case (kind, positions) if positions.contains(pos) =>
            SpecialTile(pos, kind)
        }.getOrElse(Floor(pos))
        case other => other
```


[Index](../index.md)