package com.github.aimmoth.script

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
import shared.Html

@JSExport
class AjaxRest {

  @JSExport
  def post() = {
    // Values from HTML elements are Dynamic
    val number = jQuery(s"#${Html.resourcePost}").`val`()
    
    val test = new Resource()
    /*
     * This could be replaced by implicit def (if you dare):
      implicit def dynamicToInt(d : Dynamic) : Int = d.asInstanceOf[String].toInt
      test.x = number
     * 
     */
    test.x = number.asInstanceOf[String].toInt
    // Validated with shared logic
    ResourceValidator.apply(test)
    
    jQuery.ajax("/api/v1/resource", settings = Dynamic.literal(
      data = "x=" + number,
      method = "POST",
      success = { (data: JsAny, textStatus: String, jqXHR: JQueryXHR) =>

        jQuery(s"#${Html.resourceGet}").`val`(JSON.stringify(data))
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
    val id = jQuery(s"#${Html.resourceGet}").`val`()
    jQuery.ajax("/api/v1/resource", settings = Dynamic.literal(
      data = "id=" + id,
      success = { (data: JsAny, textStatus: String, jqXHR: JQueryXHR) =>

        jQuery(s"#${Html.resourceOutput}").`val`(JSON.stringify(data))
        global.console.dir(data)
        alert("OK")
      },
      error = { (jqXhr: JQueryXHR, textStatus: String, errorThrown: String) =>

        global.console.dir(jqXhr)
        alert(s"${jqXhr.status}:${jqXhr.responseText}")
      }).asInstanceOf[JQueryAjaxSettings])
  }
}