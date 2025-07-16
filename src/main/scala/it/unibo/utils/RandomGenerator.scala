package it.unibo.utils

object RandomGenerator:
  import scala.util.Random
  private val rand = Random(seed = 42)
  
  extension[A](seq: Seq[A])
    def getRandomElement: Option[A] = seq match
      case Nil => None
      case _   => Some(seq(rand nextInt seq.size))
