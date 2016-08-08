package shared.html

import scalatags.Text.all._
import scalatags.Text.tags2.{ title => title2 }

object ServerError {
  
  def InternalServerError() = {
    html(
        head(
            title2("5xx")
            ),
        body(
            h1("5xx"),
            p("Internal server error!")
            )
        
        )
  }
}