package scalajs.angular

import biz.enef.angulate.angular

class AngularModule {
  
  def init() = {
    angular.createModule("app", Seq("ngRoute")) match {
      case app => 
//        app.controllerOf[SimpleController]("SimpleController")
    }  
  }
}