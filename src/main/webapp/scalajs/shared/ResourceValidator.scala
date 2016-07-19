package shared

object ResourceValidator {
  
  def apply(r : Resource) = if (r.x <= 0) throw new IllegalArgumentException("x must be positive!")
}