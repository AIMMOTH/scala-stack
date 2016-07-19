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
    
    jQuery("#javascriptAlert").addClass("servlet-Stylisch-hidden")
    jQuery("#resourcePostButton").prop("disabled", false)
    jQuery("#resourceGetButton").prop("disabled", false)
  }
}