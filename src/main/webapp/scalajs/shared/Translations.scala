package shared

object Languages extends Enumeration {
  final case class Language(val name : String, val defaultName : String, val code : String) extends Val
  
  val default = Language("Default", "Default language (English)", "en-GB")
  val svenska = Language("Svenska", "Swedish", "se-SV")
}

object Translations extends Enumeration {
  
  final case class Translation(default : String, svenska : String) extends Val {
    val get = Map(Languages.default -> default, Languages.svenska -> svenska)
  }
  
  val documentHeader = Translation("Scala Stack!", "Scala Stacken!")
  val resourceValidationError = Translation("x must be positive!", "x måste vara positivt!")
  
}