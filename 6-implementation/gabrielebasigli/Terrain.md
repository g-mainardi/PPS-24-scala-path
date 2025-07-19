# Scenario Terrain
<img width="800" height="601" alt="image" src="https://github.com/user-attachments/assets/22cf6284-d262-493a-956a-dad3ca09a8e5" />

Questo scenario consiste in una simulazione di un mondo, composto da erba, acqua e roccia (e raramente da qualche pozza di lava), generato in maniera randomica.
La generazione dell’ambiente non avviene in modo completamente casuale, ma utilizza una tecnica chiamata Perlin Noise, che consente di creare una distribuzione dei terreni visivamente più naturale e realistica rispetto a un approccio puramente randomico.

## La classe Terrain

Come ogni scenario, anche questo estende `EmptyScenario` dal quale eredita il metodo `generate`. Questo metodo accetta in input una funzione che, a partire dalle coordinate (x, y), restituisce una Tile. In questo caso la logica risiede nell'oject `PerlinNoise`. 
```scala
class Terrain(nRows: Int, nCols: Int) extends EmptyScenario(nRows, nCols):
  override def generate(generator: (x: Int, y:Int) => Tile): Unit =
    super.generate {
      lazy val permutation = PerlinNoise.randomPermutation
      (x, y) => getTileFromNoise(PerlinNoise.getNoise(x, y, PerlinScale, permutation))(Position(x, y))
    }
```
La call-by-need (o lazy evaluation), viene usata nel metodo `generate` per evitare che la permutazione casuale venga calcolata una sola volta per l'intero scenario e poi riutilizzata in tutte le invocazioni successive. La PerlinNoise richiede una permutazione pseudo-casuale come base, questa permutazione dev'essere costante per tutte le coordinate di uno stesso scenario, altrimenti il paesaggio risulterebbe incoerente e disconnesso. Senza lazy, la permutazione verrebbe rigenerata a ogni chiamata della funzione (x, y) => ..., producendo risultati casuali e diversi per ogni cella.

Quindi per ogni coordinata viene generato un valore di noise, compreso tra 0 e 1, che viene poi passato come argomento al metodo `getTileFromNoise` della classe.
Questo restituice una `Tile` sulla base di soglie predefinite.

Questo valore, compreso tra 0 e 1, viene poi passato alla funzione getTileFromNoise, la quale lo trasforma in una Tile sulla base di soglie predefinite.
Ad esempio, valori molto bassi corrispondono all'acqua, quelli medi all'erba o alla roccia, mentre i valori più alti rappresentano la lava.

```scala
  private def getTileFromNoise(noise: Double)(position: Position): Tile =
    if noise < WaterThreshold then Water(position)
    else if noise < GrassThreshold then Grass(position)
    else if noise < RockThreshold then Rock(position)
    else Lava(position)
```
## L'oggetto PerlinNoise

Il metodo `randomPermutation` genera una permutazione casuale di 256 numeri interi, duplicata per evitare problemi di overflow. È la base del rumore e garantisce varietà nella generazione. Mentre, `fade` Serve a evitare spigolosità nel rumore, `lerp` (linear interpolation) è usata per combinare i contributi delle celle adiacenti nel calcolo del valore finale del rumore (il fattore che contribuisce a conferire al risultato una maggiore naturalezza). `grad` calcola il gradiente associato a un punto della griglia. Infine`getNoise` combina le funzioni precedenti per restituire un valore di rumore normalizzato tra 0 e 1 per una data coordinata (x, y), con un fattore di scala (che determina la frequenza delle variazioni).


[Index](../index.md)
