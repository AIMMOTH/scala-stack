package scalajs.shared.util

import scala.scalajs.js.Dynamic

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
      def info(message: String): Unit = console.info(message)
      def debug(message: String): Unit = console.log(message)
      def warning(message: String): Unit = console.warn(message)
      def warning(throwable: Throwable): Unit = console.trace(throwable.getMessage)
      def error(message: String): Unit = console.error(message)

    }
  }
}