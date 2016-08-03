package webapp.script

import scala.scalajs.js.Dynamic
import scala.scalajs.js.Dynamic._
import scala.scalajs.js.{ Any => JsAny }
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.JSON
import scala.scalajs.js
import org.scalajs.dom.window._
import org.scalajs.jquery._
import org.scalajs.jquery.JQueryAjaxSettings
import shared.ResourceValidator
import shared.Resource
import shared._

@JSExport
class AjaxRest extends FrontendLogic {

  @JSExport
  def post() = {

    create(() => jQuery(s"#${Id.resourcePost.toString}").`val`().toString.toInt, resource => {

      val stringified = JSON.stringify(Dynamic.literal(x = resource.x))
      
      jQuery.ajax("/api/v1/resource", settings = Dynamic.literal(
        data = "x=" + stringified,
        method = "POST",
        success = { (data: JsAny, textStatus: String, jqXHR: JQueryXHR) =>

          jQuery(s"#${Id.resourceGet.toString}").`val`(JSON.stringify(data))
          global.console.dir(data)
          alert("OK")
        },
        error = { (jqXhr: JQueryXHR, textStatus: String, errorThrown: String) =>

          global.console.dir(jqXhr)
          alert(s"${jqXhr.status}:${jqXhr.responseText}")
        }).asInstanceOf[JQueryAjaxSettings])
    })
  }

  @JSExport
  def get() = {
    val id = jQuery(s"#${Id.resourceGet.toString}").`val`()
    jQuery.ajax("/api/v1/resource", settings = Dynamic.literal(
      data = "id=" + id,
      success = { (data: JsAny, textStatus: String, jqXHR: JQueryXHR) =>

        jQuery(s"#${Id.resourceOutput.toString}").`val`(JSON.stringify(data))
        global.console.dir(data)
        alert("OK")
      },
      error = { (jqXhr: JQueryXHR, textStatus: String, errorThrown: String) =>

        global.console.dir(jqXhr)
        alert(s"${jqXhr.status}:${jqXhr.responseText}")
      }).asInstanceOf[JQueryAjaxSettings])
  }
}