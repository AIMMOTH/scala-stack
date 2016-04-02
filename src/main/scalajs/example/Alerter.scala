package example

import scala.scalajs.js.annotation.JSExport

import org.scalajs.dom.window.alert

@JSExport
class Alerter {
  
  @JSExport
  def click() : Unit = {
    alert("Hello!")
  }
}