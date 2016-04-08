package example.scala

import scalatags.stylesheet._
import scalatags.Text.all._

trait Style extends StyleSheet {

  def redBackground = cls(backgroundColor := "red")

  def render = styleSheetText
}
