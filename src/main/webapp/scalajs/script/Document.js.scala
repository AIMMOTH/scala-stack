package com.github.aimmoth.script

import scala.scalajs.js._
import scala.scalajs.js.annotation.JSExport

@JSExport
class Document {
  
  @JSExport
  def ready() = {
    println("Compiled Scala JS started...")
    
    // Easy way to start foundation without Scala JS facade
    eval("$(document).foundation();")
    
  }
}