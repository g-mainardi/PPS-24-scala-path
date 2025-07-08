package it.unibo.utils

trait PrettyPrint:
  override def toString: String = s"${getClass.getSimpleName}"
