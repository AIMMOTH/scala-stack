package scalajs.shared

object Languages {
  final case class Language(val name : String, val defaultName : String, val code : String)
  
  val default = Language("Default", "Default language (English)", "en-gb")
  val svenska = Language("Svenska", "Swedish", "se-sv")
  
  lazy val all = List(default, svenska)
}

object Translations {
  
  final case class Translation(default : String, svenska : String) {
    val get = Map(Languages.default -> default, Languages.svenska -> svenska)
  }
  
  val documentHeader = Translation("Scala Stack!", "Scala Stacken!")
  val resourceValidationError = Translation("x must be positive!", "x måste vara positivt!")
  
}