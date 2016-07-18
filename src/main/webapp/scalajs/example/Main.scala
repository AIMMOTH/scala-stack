package example

import scala.scalajs.js._
import scala.scalajs.js.annotation.JSExport
import org.scalajs.jquery.jQuery

@JSExport
class Main {
  
  @JSExport
  def start() = {
    println("Compiled Scala JS started...")
    
    // Easy way to start foundation without Scala JS facade
    eval("$(document).foundation();")
    
    // Start Angular
    val module = new AngularModule
    module.start()
    
    jQuery("#alertButton").prop("disabled", false)
    jQuery("#restButton").prop("disabled", false)
    jQuery("#increaseButton").attr("disabled", false)
    jQuery("#decreaseButton").attr("disabled", false)
    jQuery("#angularValue").removeClass("example-Stylisch-hidden")
    jQuery("#javascriptAlert").addClass("example-Stylisch-hidden")
  }
}