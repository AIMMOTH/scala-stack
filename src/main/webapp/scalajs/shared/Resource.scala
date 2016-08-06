package shared

import scala.scalajs.js

sealed class Resource {
  var x : Integer = 0
}

trait ResourceObject extends js.Object {
  
  var x : Integer = js.native
}

object ResourceObject {

  def apply(r : Resource) : ResourceObject = {
    js.Dynamic.literal(x = r.x).asInstanceOf[ResourceObject]  
  }
}
