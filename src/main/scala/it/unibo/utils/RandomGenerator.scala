package it.unibo.utils

/** A utility object that provides random selection functionality for sequences.
 * 
 * Uses a fixed seed (42) for reproducible random generation.
 */
object RandomGenerator:
  import scala.util.Random
  private val rand = Random(seed = 42)
  
  extension[A](seq: Seq[A])
    /** Returns a random element from the sequence.
     *
     * @return Some containing a random element if the sequence is non-empty, None otherwise
     */
    def getRandomElement: Option[A] = seq match
      case Nil => None
      case _   => Some(seq(rand nextInt seq.size))

    /** Returns a sequence of n random elements from the sequence.
     * 
     * Elements are selected with replacement, meaning the same element can be chosen multiple times.
     *
     * @param n the number of elements to select
     * @return a sequence of n random elements, or empty sequence if input is empty
     */
    def getRandomElements(n: Int): Seq[A] = seq match
      case Nil => Seq.empty
      case _   => 0 until n map {_ => seq.getRandomElement.get}
