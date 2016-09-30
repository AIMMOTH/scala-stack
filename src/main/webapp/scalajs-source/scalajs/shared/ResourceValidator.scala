package scalajs.shared

object ResourceValidator {
  
  def apply(r : Resource)(implicit language : Languages.Language = Languages.default) = if (r.x <= 0) throw new IllegalArgumentException(Translations.resourceValidationError.get(language))
}