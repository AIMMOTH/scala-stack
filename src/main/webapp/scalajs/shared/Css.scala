package shared

import scalatags.Text.all._
import scalatags.stylesheet.StyleSheet

trait Stylisch { this : StyleSheet =>

  val hidden = cls(display := "none")
  
  override def customSheetName = Some("scalatags")
}

object Css {
  
  def apply = new StyleSheet with Stylisch
}