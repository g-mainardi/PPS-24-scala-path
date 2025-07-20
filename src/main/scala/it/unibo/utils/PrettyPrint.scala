package it.unibo.utils

/** A trait that provides a simple pretty printing functionality for classes.
 *
 * This trait overrides the toString method to provide a clean string representation
 * of implementing classes by displaying only their simple class name.
 */
trait PrettyPrint:
  override def toString: String = s"${getClass.getSimpleName}"
