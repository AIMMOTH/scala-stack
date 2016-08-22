package shared.util

trait JsLogger {

  def info(message: String): Unit
  def debug(message: String): Unit
  def warning(message: String): Unit
  def warning(throwable: Throwable): Unit
  def error(message: String): Unit
}

object JsLogger {
  /**
   * Use with org.scalajs.dom.window.global.console
   */
  def apply(console: Dynamic) = {
    new JsLogger {
      def info(message: String): Unit = console.log(message)
      def debug(message: String): Unit = console.log(message)
      def warning(message: String): Unit = console.log(message)
      def warning(throwable: Throwable): Unit = console.log(throwable.getMessage)
      def error(message: String): Unit = console.error(message)

    }
  }
}