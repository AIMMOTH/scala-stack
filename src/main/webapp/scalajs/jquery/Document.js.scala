package jquery

import scala.scalajs.js._
import scala.scalajs.js.annotation.JSExport
import org.scalajs.jquery.jQuery
import shared._
import shared.html.Id

@JSExport
class Document {
  
  @JSExport
  def ready() = {
    println("Compiled Scala JS started...")
    
    // Easy way to start foundation without Scala JS facade
    eval("$(document).foundation();")
    
    // Use this to get names of styling classes
    val styling = new Stylisch
    
    jQuery(s"#${Id.javascriptAlert.toString}").addClass(s"${styling.hidden.name}")
    jQuery(s"#${Id.resourcePostButton.toString}").prop("disabled", false)
    jQuery(s"#${Id.resourceGetButton.toString}").prop("disabled", false)
  }
}