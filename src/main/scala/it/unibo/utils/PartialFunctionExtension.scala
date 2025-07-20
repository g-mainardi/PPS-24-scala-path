package it.unibo.utils


/** Extension methods for working with partial functions.
 *
 * This object provides utility methods to handle partial functions in a more
 * convenient way, including safe execution and conversion to total functions.
 */
object PartialFunctionExtension:

  /** Executes a partial function if it is defined for the input, does nothing otherwise.
   *
   * @param e    the input value to test and potentially process
   * @param pred the partial function to conditionally execute
   * @tparam T the type of the input value
   */
  def doOrNothing[T](e: T)(pred: PartialFunction[T, Unit]): Unit = if pred isDefinedAt e then pred(e) else ()

  /** Converts a partial boolean function into a total boolean function.
   *
   * @param pred the partial function returning a boolean
   * @tparam T the type of the input value
   * @return a function that returns true only if the partial function is defined and returns true
   */
  def -?->[T](pred: PartialFunction[T, Boolean]): T => Boolean = t => (pred isDefinedAt t) && pred(t)

  /** Converts a partial function returning an Option into a total function.
   *
   * @param extractor the partial function returning an Option
   * @tparam I the input type
   * @tparam O the output type wrapped in Option
   * @return a function that returns None when the partial function is not defined
   */
  def ~=~>[I, O](extractor: PartialFunction[I, Option[O]]): I => Option[O] =
    t => if extractor isDefinedAt t then extractor(t) else None

