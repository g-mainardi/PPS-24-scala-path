package it.unibo.utils

object PartialFunctionExtension:

  def doOrNothing[T](e: T)(pred: PartialFunction[T, Unit]): Unit = if pred isDefinedAt e then pred(e) else ()

  def -?->[T](pred: PartialFunction[T, Boolean]): T => Boolean = t => (pred isDefinedAt t) && pred(t)

  def ~=~>[I, O](extractor: PartialFunction[I, Option[O]]): I => Option[O] =
    t => if extractor isDefinedAt t then extractor(t) else None

