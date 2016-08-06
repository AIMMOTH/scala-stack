package shared

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll

@JSExportAll
sealed case class Resource(val x : Integer = 0, val y : Integer = 1)
