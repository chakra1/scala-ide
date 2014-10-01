package org.scalaide.util.internal

import org.scalaide.logging.HasLogger
import scala.reflect.ClassTag

object Utils extends HasLogger with org.scalaide.util.Utils {

  /** Return the time in ms required to evaluate `f()`. */
  def time(f: => Any): Long = {
    val start = System.currentTimeMillis()
    f
    System.currentTimeMillis() - start
  }

  /** Evaluate 'f' and return its value and the time required to compute it. */
  def timed[A](f: => A): (A, Long) = {
    val start = System.currentTimeMillis()
    val res = f
    (res, System.currentTimeMillis() - start)
  }

  def debugTimed[A](name: String)(op: => A): A = {
    val start = System.currentTimeMillis
    val res = op
    val end = System.currentTimeMillis

    logger.debug(f"$name: \t ${end - start}%,3d ms")
    res
  }

  def tryExecute[T](action: => T, msgIfError: => Option[String] = None): Option[T] = {
    try Some(action)
    catch {
      case t: Throwable =>
        msgIfError match {
          case Some(errMsg) => eclipseLog.error(errMsg, t)
          case None         => eclipseLog.error("Error during tryExecute", t)
        }
        None
    }
  }

  implicit class WithAsInstanceOfOpt(obj: AnyRef) extends org.scalaide.util.UtilsImplicits.WithAsInstanceOfOpt(obj) {

    def asInstanceOfOpt[B: ClassTag]: Option[B] = obj match {
      case b: B => Some(b)
      case _    => None
    }
  }
}
