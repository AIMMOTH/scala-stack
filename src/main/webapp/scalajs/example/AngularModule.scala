package example

import scala.scalajs.js.annotation.JSExport
import biz.enef.angulate.angular

@JSExport
class AngularModule {

  @JSExport
  def start() = {
    // Start angular
    angular.createModule("app", Seq( /* "ngRoute" */ )) match {
      case app =>
        app.controllerOf[SimpleController]("SimpleController")
    }
  }
}