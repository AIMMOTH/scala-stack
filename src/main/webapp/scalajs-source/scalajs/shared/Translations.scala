package scalajs.shared

import scalajs.shared.Languages.Language

object Languages {
  final case class Language(val name : String, val defaultName : String, val code : String, default : Boolean = false)
  
  val english = Language("Default", "Default language (English)", "en-gb", true)
  val svenska = Language("Svenska", "Swedish", "se-sv")
  
  lazy val all = List(english, svenska)
  lazy val default = all.find(_.default).getOrElse(english)
}

object Translations {
  
  final case class Translation(default : String, svenska : String) {
    private val map = Map(Languages.default -> default, Languages.svenska -> svenska)
    def get(implicit language : Language) = map(language)
  }
  
  val documentHeader = Translation("Scala Stack!", "Scala Stacken!")
  val resourceValidationError = Translation("x must be positive!", "x måste vara positivt!")
  val internalServerError = Translation("Internal server error!", "Internt serverfel!")
  val notFoundError = Translation("Sorry, the page you are looking for ain't there.", "Tyvärr, sidan du söker finns inte.")
}