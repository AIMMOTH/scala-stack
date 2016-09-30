package scalajs.shared.html

import scalatags.Text.all._
import scalatags.Text.tags2.{ title => title2 }
import scalajs.shared.Languages.Language
import scalajs.shared.Translations

object ServerError {
  
  def InternalServerError(implicit language : Language) = {
    html(
        head(
            title2("5xx")
            ),
        body(
            h1("5xx"),
            p(Translations.internalServerError.get)
            )
        
        )
  }
}