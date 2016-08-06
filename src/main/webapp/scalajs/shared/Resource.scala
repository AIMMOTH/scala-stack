package shared

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll

@JSExportAll
sealed case class Resource(var x : Integer = 0, var y : Integer = 1) {
  def this() = this(x = 0, y = 0)
}
