package shared

sealed trait Result[E, A] {
  /** Return `true` if this validation is success. */
  def isSuccess: Boolean = this match {
    case Success(_) => true
    case Failure(_) => false
  }
  /** Return `true` if this validation is failure. */
  def isFailure: Boolean = !isSuccess
}
final case class Success[E, A](value: A) extends Result[E, A]
final case class Failure[E, A](value: E) extends Result[E, A]