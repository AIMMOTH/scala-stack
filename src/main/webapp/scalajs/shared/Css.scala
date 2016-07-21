package shared

import scalatags.Text.all._
import scalatags.stylesheet._

trait Stylisch { this : StyleSheet =>

  val hidden = cls(display := "none")
}