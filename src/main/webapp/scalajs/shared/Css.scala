package shared

import scalatags.Text.all._
import scalatags.stylesheet.StyleSheet

class Stylisch extends StyleSheet {

  val hidden = cls(display := "none")
  
  override def customSheetName = Some("scalatags")
}

