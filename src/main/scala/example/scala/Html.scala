package example.scala

import scalatags.Text.all._
import scalatags.Text.tags2.{ style => style2 }
import scalatags.Text.tags2.{ title => title2 }
import scalatags.stylesheet._

object Html {

  def apply(style : Style) = {
    html(
      head(
        title2("All Scala!"),
        style2(`type` := "text/css")(style.render),
        script(src := "javascript.js")),
      body(
        h1("All Scala!"),
        p(backgroundColor := "green")("Push the red button below for an alert!"),
        button(style.redBackground)(onclick := "example.Alerter().click()")("Press me!"))
      )
  }
}