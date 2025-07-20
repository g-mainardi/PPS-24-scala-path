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
Oppure alternativamente se non si vuole specificare il numero di colonne:
```scala
class TestScenarioWithClosedWalls extends Scenario(10, 10):
  private val p = Position(7, 8)
  override def generate(): Unit =
    _tiles = grid():
      F | F | F | F | F | F | F | F | F | F ||;
      F | F | TP(p) | F | F | F | F | F | F | F ||;
      F | F | F | F | F | F | F | F | F | F ||;
      F | F | F | F | F | F | F | F | F | F ||;
      F | F | F | F | F | F | F | F | F | F ||;
      F | F | F | F | F | F | F | F | F | F ||;
      F | F | F | F | F | F | W | W | W | W ||;
      F | F | F | F | F | F | W | F | F | W ||;
      F | F | F | F | F | F | W | F | F | W ||;
      F | F | F | F | F | F | W | W | W | W ||
```

La classe GridBuilder, tiene traccia delle coordinate correnti e consente di costruire riga per riga una sequenza di celle, e garantisce che tutte le righe abbiano la stessa lunghezza.
Il metodo `+` aggiunge una nuova tile alla sequenza interna di tiles, nel caso sia stato sia stato definito il numero di colonne provvede anche ad aggiungere una nuova riga. Infine il metodo `build` restituisce la sequenza di tiles.

```scala
class GridBuilder(expectedColumns: Option[Int]):
  private var tiles: Seq[Tile] = Seq.empty
  private var currentRowIndex: Int = 0
  private var currentColumnIndex: Int = 0
  private var maxColumnIndex: Option[Int] = None

  def +(f: Position => Tile): GridBuilder =
    tiles = tiles :+ f(Position(currentColumnIndex, currentRowIndex))
    expectedColumns match
      case Some(columns) if currentColumnIndex == (columns -1) => ++()
      case _ => currentColumnIndex += 1
    this

  def ++(): GridBuilder =
    if maxColumnIndex.isEmpty then
      maxColumnIndex = Some(currentColumnIndex)
    if currentColumnIndex != maxColumnIndex.get then
      throw new IllegalArgumentException(s"Rows must be of the same length: expected ${maxColumnIndex.get}, found ${currentColumnIndex} at row ${currentRowIndex}")
    currentRowIndex += 1
    currentColumnIndex = 0
    this

  def build(): Seq[Tile] =
    expectedColumns match
      case Some(columns) if tiles.length % columns != 0 => throw new IllegalArgumentException(s"Rows must be of the same length: expected ${columns}}")
      case _ => ()
    tiles
```
Questi metodi vengono poi chiamati dall'object `GridDSL`. Ad esempio il metodo F utilizza implicitamente il `GridBuilder` e ne chiama il metodo `+` passando il costruttore della tile di tipo Floor. Questo concetto viene esteso ad ogni tipologia di tile. 
L'unica eccezione son le tile di tipo `Specials` che richiede qualche passaggio in più. Il metodo `|` funge da semplice separatore visivo, non impatta sulla creazione delle celle, mentre il metodo `||` permette di "andare a capo" e di dichiarare una nuova linea di celle, chiamando a sua volta il metodo `++` del `GridBuilder`. 

```scala
object GridDSL:
  def grid(columns: Int = -1)(body: GridBuilder ?=> Unit): Seq[Tile] =
    given builder: GridBuilder = new GridBuilder(if columns!= -1 then Some(columns) else None)
    body
    builder.build()

  def F(using b: GridBuilder): GridBuilder =
    b + Floor.apply

  def G(using b: GridBuilder): GridBuilder =
    b + Grass.apply

  def W(using b: GridBuilder): GridBuilder =
    b + Wall.apply

  /**
  * others tile type methods ...
  */

  def TP(to: Position)(using b: GridBuilder): GridBuilder =
    b + (pos => {
      val special = new SpecialTileBuilder
      special tile "TestTeleport" does (_ => to)
      val kind = SpecialTileRegistry.allKinds.find(_.name == "TestTeleport").get
      given Scenario.Dimensions = Scenario.Dimensions(to.x + 1, to.y + 1)
      SpecialTile(pos, kind)
    })

  extension (b: GridBuilder)
    def ||(next: GridBuilder): GridBuilder =
      b.++()

    def || : GridBuilder =
      b.++()

    def |(next: GridBuilder): GridBuilder = b
```
