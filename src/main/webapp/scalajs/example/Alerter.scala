package example

import scala.scalajs.js.{JSON, Any => JsAny}
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom.window.alert

@JSExport
class Alerter {

  @JSExport
  def hello() = {
    alert("Hello!")
  }
  
  @JSExport
  def show(something : JsAny) = {
    alert(JSON.stringify(something))
  }
}