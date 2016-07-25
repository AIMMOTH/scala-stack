package com.github.aimmoth.script

import scala.scalajs.js._
import scala.scalajs.js.annotation.JSExport
import org.scalajs.jquery.jQuery
import shared.Html
import shared.Stylisch

@JSExport
class Document {
  
  @JSExport
  def ready() = {
    println("Compiled Scala JS started...")
    
    // Easy way to start foundation without Scala JS facade
    eval("$(document).foundation();")
    
    jQuery(s"#${Html.javascriptAlert}").addClass("scalatags-hidden")
    jQuery(s"#${Html.resourcePostButton}").prop("disabled", false)
    jQuery(s"#${Html.resourceGetButton}").prop("disabled", false)
  }
}