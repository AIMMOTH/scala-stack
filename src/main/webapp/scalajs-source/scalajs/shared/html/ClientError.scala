package scalajs.shared.html

import scalatags.Text.all._
import scalatags.Text.tags2.{ title => title2 }
import scalajs.shared.Languages.Language
import scalajs.shared.Translations.Translation
import scalajs.shared.Translations

object ClientError {
  
  def NotFound(implicit language : Language) = {
    html(
        head(
            title2("404")
            ),
        body(
            h1("404"),
            p(Translations.notFoundError.get)
            )
        
        )
  }
}