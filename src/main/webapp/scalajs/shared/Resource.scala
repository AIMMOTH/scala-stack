package shared

import scala.scalajs.js

@js.native
trait Resource extends js.Object {
  
  var x : Int = js.native
}

object Resource {
  
  def apply(x : Int) = js.Dynamic.literal(x = x).asInstanceOf[Resource] 
  
  def fromString(json : String) = Resource(Integer.parseInt(json))
}