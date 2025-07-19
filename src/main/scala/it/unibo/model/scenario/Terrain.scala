package it.unibo.model.scenario

import it.unibo.model.fundamentals.{Position, Tile}
import it.unibo.model.fundamentals.Tiling.*

import scala.math.*
import scala.util.Random

object PerlinNoise:

  /**
   * Generates a random permutation of integers from 0 to 255, which is used to create the noise.
   * The permutation is duplicated to avoid index out of bounds when accessing the array.
   * @return an array of integers representing the permutation
   */
  def randomPermutation: Array[Int] =
    val base = Array.tabulate(256)(identity)
    val shuffled = Random.shuffle(base.toSeq).toArray
    shuffled ++ shuffled


  /**
   * Fade function as defined by Ken Perlin. This smooths the interpolation between grid points.
   * The fade function is a polynomial that eases the transition between values.
   * @param t the input value in the range [0, 1]
   * @return the faded value
   */
  def fade(t: Double): Double =
    t * t * t * (t * (t * 6 - 15) + 10)


  /**
   * Linear interpolation function.
   * @param t the interpolation factor in the range [0, 1]
   * @param a the start value
   * @param b the end value
   * @return the interpolated value
   */
  def lerp(t: Double, a: Double, b: Double): Double =
    a + t * (b - a)


  /**
   * Computes the gradient based on a hash value and the coordinates.
   * The hash value is used to determine the direction of the gradient.
   * @param hash the hash value derived from the permutation
   * @param x the x coordinate
   * @param y the y coordinate
   * @return the gradient value
   */
  def grad(hash: Int, x: Double, y: Double): Double =
    val h = hash & 0x3F
    val u = if h < 4 then x else y
    val v = if h < 4 then y else x
    (if (h & 1) == 0 then u else -u) + (if (h & 2) == 0 then v else -v)


  /**
   * Generates Perlin noise for a given (x, y) position with a specified scale.
   * @param x the x coordinate
   * @param y the y coordinate
   * @param scale the scale factor for the noise
   * @param permutation the precomputed permutation array used for noise generation
   * @return a noise value in the range [0, 1]
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

/**
 * A scenario that generates a terrain based on Perlin noise.
 * The terrain consists of different types of tiles (Water, Grass, Rock, Lava)
 * determined by the noise value at each position.
 *
 * @param nRows number of rows in the scenario
 * @param nCols number of columns in the scenario
 */
class Terrain(nRows: Int, nCols: Int) extends EmptyScenario(nRows, nCols):
  private val WaterThreshold = 0.4
  private val GrassThreshold = 0.7
  private val RockThreshold = 0.9
  private val PerlinScale = 0.15

  /**
   * Converts a noise value to a specific tile type based on predefined thresholds.
   * @param noise the noise value in the range [0, 1]
   * @return a function that takes a Position and returns a Tile
   */
  private def getTileFromNoise(noise: Double)(position: Position): Tile =
    if noise < WaterThreshold then Water(position)
    else if noise < GrassThreshold then Grass(position)
    else if noise < RockThreshold then Rock(position)
    else Lava(position)

  /**
   * Generates the terrain by applying Perlin noise to each position in the grid.
   * The noise value is used to determine the type of tile at that position.
   * The scale factor controls the frequency of the noise.
   */
  override def generate(generator: (x: Int, y:Int) => Tile): Unit =
    super.generate {
      lazy val permutation = PerlinNoise.randomPermutation
      (x, y) => getTileFromNoise(PerlinNoise.getNoise(x, y, PerlinScale, permutation))(Position(x, y))
    }
