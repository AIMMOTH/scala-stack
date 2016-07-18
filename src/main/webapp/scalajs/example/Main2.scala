package example

import scala.scalajs.js._
import scala.scalajs.js.annotation.JSExport

@JSExport
class Main {
  
  @JSExport
  def start() = {
    // Easy way to start foundation without Scala JS facade
    eval("$(document).foundation();")
    
    // Start Angular
    val module = new AngularModule
    module.start()
  }
}