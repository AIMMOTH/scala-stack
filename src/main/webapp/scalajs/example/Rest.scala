package example

import scala.scalajs.js.{Dynamic, Any => JsAny}
import scala.scalajs.js.annotation.JSExport
import org.scalajs.jquery._
import org.scalajs.jquery.JQueryAjaxSettings

@JSExport
class Rest {
  
  @JSExport
  def get() = {
    val number = jQuery("#resourceNumber").`val`()
    jQuery.ajax("/api/v1/resource", settings = Dynamic.literal(
      data = "x=" + number,
      success = { (data: JsAny, textStatus: String, jqXHR: JQueryXHR) =>
        val alerter = new Alerter
        alerter.show(data)
      }
    ).asInstanceOf[JQueryAjaxSettings])
  }
}