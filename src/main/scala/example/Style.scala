package example

import scalatags.stylesheet._
import scalatags.Text.all._

trait Simple extends StyleSheet {
  def redBackground = cls(backgroundColor := "red")
}
