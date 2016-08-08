package shared.util

sealed trait Validated[E, A] {
  /** Return `true` if this validation is success. */
  def isSuccess: Boolean = this match {
    case OK(_) => true
    case KO(_) => false
  }
  /** Return `true` if this validation is failure. */
  def isFailure: Boolean = !isSuccess
}
final case class OK[E, A](value: A) extends Validated[E, A]
final case class KO[E, A](value: E) extends Validated[E, A]