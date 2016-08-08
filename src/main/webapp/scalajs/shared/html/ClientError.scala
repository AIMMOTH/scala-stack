package shared.html

import scalatags.Text.all._
import scalatags.Text.tags2.{ title => title2 }

object ClientError {
  
  def NotFound() = {
    html(
        head(
            title2("404")
            ),
        body(
            h1("404"),
            p("Sorry, the page you are looking for ain't there.")
            )
        
        )
  }
}