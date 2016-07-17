package example

import biz.enef.angulate.Controller

class SimpleController extends Controller {
  
  var simpleInt = 0
  
  def inc() = simpleInt += 1
  def dec() = simpleInt -= 1
}