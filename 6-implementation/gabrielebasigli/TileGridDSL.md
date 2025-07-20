# TileGridDSL 
Per facilitare la creazione di nuovi scenari, usati per lo più per eseguire test, è stato realizzato un dsl che permette di costruire una griglia di tile in maniera più intuitiva. Ad esempio, per testare le tile di tipo teletrasporto, era necessario creare uno scenario contenente una "stanza" chiusa in cui si trovasse il goal. L’unico accesso possibile alla stanza è tramite una tile di tipo teletrasporto, che consente all’agente di raggiungerla. Grazie al DSL, è possibile definire questo scenario in poche righe di codice, nel seguente modo.

```scala
class TestScenarioWithClosedWalls extends Scenario(10, 10):
  private val p = Position(7, 8)
  override def generate(): Unit =
    _tiles = grid(10):
      F | F | F | F | F | F | F | F | F | F
      F | F | TP(p) | F | F | F | F | F | F | F
      F | F | F | F | F | F | F | F | F | F
      F | F | F | F | F | F | F | F | F | F
      F | F | F | F | F | F | F | F | F | F
      F | F | F | F | F | F | F | F | F | F
      F | F | F | F | F | F | W | W | W | W
      F | F | F | F | F | F | W | F | F | W
      F | F | F | F | F | F | W | F | F | W
      F | F | F | F | F | F | W | W | W | W
```


La classe GridBuilder, tiene traccia delle coordinate correnti e consente di costruire riga per riga una sequenza di celle, e garantisce che tutte le righe abbiano la stessa lunghezza.

```scala
class GridBuilder(expectedColumns: Option[Int]):
  var tiles: Seq[Tile] = Seq.empty
  private var currentRowIndex: Int = 0
  private var currentColumnIndex: Int = 0
  private var maxColumnIndex: Option[Int] = None

  def add(f: Position => Tile): Unit =
    tiles = tiles :+ f(Position(currentColumnIndex, currentRowIndex))
    expectedColumns match
      case Some(columns) if currentColumnIndex == (columns -1) => newRow()
      case _ => currentColumnIndex += 1

  def newRow(): Unit =
    if maxColumnIndex.isEmpty then
      maxColumnIndex = Some(currentColumnIndex)
    if currentColumnIndex != maxColumnIndex.get then
      throw new IllegalArgumentException(s"Rows must be of the same length: expected ${maxColumnIndex.get}, found ${currentColumnIndex} at row ${currentRowIndex}")
    currentRowIndex += 1
    currentColumnIndex = 0

  def build(): Seq[Tile] =
    expectedColumns match
      case Some(columns) if tiles.length % columns != 0 => throw new IllegalArgumentException(s"Rows must be of the same length: expected ${columns}}")
      case _ => ()
    tiles
```
