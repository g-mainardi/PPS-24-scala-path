package it.unibo.model
import scala.util.Random
import scala.math.*
import Tiling.*

object PerlinNoise:

  def randomPermutation: Array[Int] =
    val base = Array.tabulate(256)(identity)
    val shuffled = Random.shuffle(base.toSeq).toArray
    shuffled ++ shuffled

  private def fade(t: Double): Double =
    t * t * t * (t * (t * 6 - 15) + 10)

  private def lerp(t: Double, a: Double, b: Double): Double =
    a + t * (b - a)

  private def grad(hash: Int, x: Double, y: Double): Double =
    val h = hash & 0x3F
    val u = if h < 4 then x else y
    val v = if h < 4 then y else x
    (if (h & 1) == 0 then u else -u) + (if (h & 2) == 0 then v else -v)


  /**
   * The algorithm involves:
   *
   * - Generating gradient vectors at grid intersections.
   * - Calculating dot products between each gradient vector and the vector pointing from the grid intersection to the input coordinate.
   * - Interpolating these dot products to produce the final noise value.
   *
   * @param x
   * @param y
   * @return
   */
  def getNoise(x: Int, y: Int, scale: Double, permutation: Array[Int]): Double =
    val X = floor(x * scale).toInt & 255
    val Y = floor(y * scale).toInt & 255

    val xf = (x * scale) - floor(x * scale)
    val yf = (y * scale) - floor(y * scale)

    val u = fade(xf)
    val v = fade(yf)

    val aa = permutation(permutation(X) + Y)
    val ab = permutation(permutation(X) + Y + 1)
    val ba = permutation(permutation(X + 1) + Y)
    val bb = permutation(permutation(X + 1) + Y + 1)

    val x1 = lerp(u, grad(aa, xf, yf), grad(ba, xf - 1, yf))
    val x2 = lerp(u, grad(ab, xf, yf - 1), grad(bb, xf - 1, yf - 1))

    (lerp(v, x1, x2) + 1) / 2.0


class Terrain(nRows: Int, nCols: Int) extends EmptyScenario(nRows, nCols):

  private def getTileFromNoise(noise: Double)(position: Position): Tile =
    if noise < 0.4 then Water(position)
    else if noise < 0.7 then Grass(position)
    else if noise < 0.9 then Rock(position)
    else Lava(position)

  override def generate(generator: (x: Int, y:Int) => Tile): Unit =
    super.generate {
      lazy val permutation = PerlinNoise.randomPermutation
      (x, y) => getTileFromNoise(PerlinNoise.getNoise(x, y, 0.15, permutation))(Position(x, y))
    }
