package jvm.builder

import scalajs.shared.util.JsLogger
import org.slf4j.LoggerFactory
import org.slf4j.Logger

object LoggerBuilder {
  
  def apply(log : Logger) = {
    new JsLogger {
      def info(message : String) = log.info(message)
      def debug(message : String) = log.debug(message)
      def warning(message : String) = log.warn(message)
      def warning(throwable : Throwable) = log.warn(throwable.getMessage, throwable)
      def error(message : String) = log.error(message)
      def error(throwable : Throwable) = log.error(throwable.getMessage, throwable)
    }
  }
}