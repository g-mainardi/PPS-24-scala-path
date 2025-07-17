package it.unibo.utils

object RandomGenerator:
  import scala.util.Random
  private val rand = Random(seed = 42)
  
  extension[A](seq: Seq[A])
    def getRandomElement: Option[A] = seq match
      case Nil => None
      case _   => Some(seq(rand nextInt seq.size))

    def getRandomElements(n: Int): Set[A] = seq match
      case Nil => Set.empty
      case _   => (0 until n map {_ => seq.getRandomElement.get}).toSet
