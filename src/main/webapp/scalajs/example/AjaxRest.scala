package example

import scala.scalajs.js.Dynamic
import scala.scalajs.js.Dynamic._
import scala.scalajs.js.{ Any => JsAny }
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.JSON
import scala.scalajs.js
import org.scalajs.dom.window._
import org.scalajs.jquery._
import org.scalajs.jquery.JQueryAjaxSettings
import shared._

@JSExport
class AjaxRest {

  @JSExport
  def post() = {
    val number = jQuery("#resourcePost").`val`()
    
    val test = new Resource()
    test.x = number.asInstanceOf[String].toInt
    ResourceValidator.apply(test)
    
    jQuery.ajax("/api/v1/resource/post", settings = Dynamic.literal(
      data = "x=" + number,
      method = "POST",
      success = { (data: JsAny, textStatus: String, jqXHR: JQueryXHR) =>

        jQuery("#resourceGet").`val`(JSON.stringify(data))
        global.console.dir(data)
        alert("OK")
      },
      error = { (jqXhr: JQueryXHR, textStatus: String, errorThrown: String) =>

        global.console.dir(jqXhr)
        alert(s"${jqXhr.status}:${jqXhr.responseText}")
      }).asInstanceOf[JQueryAjaxSettings])
  }

  @JSExport
  def get() = {
    val id = jQuery("#resourceGet").`val`()
    jQuery.ajax("/api/v1/resource/get", settings = Dynamic.literal(
      data = "id=" + id,
      success = { (data: JsAny, textStatus: String, jqXHR: JQueryXHR) =>

        jQuery("#resourceOutput").`val`(JSON.stringify(data))
        global.console.dir(data)
        alert("OK")
      },
      error = { (jqXhr: JQueryXHR, textStatus: String, errorThrown: String) =>

        global.console.dir(jqXhr)
        alert(s"${jqXhr.status}:${jqXhr.responseText}")
      }).asInstanceOf[JQueryAjaxSettings])
  }
}