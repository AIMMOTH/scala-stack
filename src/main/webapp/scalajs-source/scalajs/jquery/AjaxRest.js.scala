package scalajs.jquery

import scala.scalajs.js.{ Any => JsAny }
import scala.scalajs.js.Dynamic
import scala.scalajs.js.Dynamic.global
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.JSExport

import org.scalajs.dom.window.{console, alert}
import org.scalajs.jquery.{jQuery, JQueryXHR}
import org.scalajs.jquery.JQueryAjaxSettings

import scalajs.shared.Resource
import scalajs.shared.util.OK
import scalajs.shared.util.KO
import scalajs.jquery.logic.FrontendLogic
import scalajs.shared.html.Id
import scalajs.shared.util.JsLogger
import upickle.default.write

/**
 * Untested code. Put all logic into FrontendLogic and test it there.
 */
@JSExport
class AjaxRest extends FrontendLogic {

  private lazy implicit val logger = JsLogger(global.console)
  
  @JSExport
  def doPost() = {

    val value = jQuery(s"#${Id.resourcePost.toString}").`val`().toString.toInt
    
    /*
     * Post action is using jQuery ajax and defining success and error.
     */
    val postAction : Resource => Unit = resource => {

      // Serialize shared resource with Scala JS code
      val s = write(resource)
      global.console.log(s)

      jQuery.ajax("/api/v1/resource", settings = Dynamic.literal(
        data = "x=" + s,
        method = "POST",
        success = { (data: JsAny, textStatus: String, jqXHR: JQueryXHR) =>

          jQuery(s"#${Id.resourceGet.toString}").`val`(JSON.stringify(data))
          global.console.dir(data)
          alert("OK")
        },
        error = { (jqXhr: JQueryXHR, textStatus: String, errorThrown: String) =>

          global.console.dir(jqXhr)
          alert(s"${jqXhr.status}:${jqXhr.statusText}")
        }).asInstanceOf[JQueryAjaxSettings])
    }
    
    post(value, postAction) match {
      case KO(throwable) => alert(throwable.getMessage)
      case OK(unit) =>
    }
  }

  @JSExport
  def doGet() = {
    
    val id = jQuery(s"#${Id.resourceGet.toString}").`val`().toString.toLong
    
    val getAction : Long => Unit = id => jQuery.ajax("/api/v1/resource/" + id, settings = Dynamic.literal(
      success = { (data: JsAny, textStatus: String, jqXHR: JQueryXHR) =>

        jQuery(s"#${Id.resourceOutput.toString}").`val`(JSON.stringify(data))
        global.console.dir(data)
        alert("OK")
      },
      error = { (jqXhr: JQueryXHR, textStatus: String, errorThrown: String) =>

        global.console.dir(jqXhr)
        alert(s"${jqXhr.status}:${jqXhr.responseText}")
      }).asInstanceOf[JQueryAjaxSettings])
      
    get(id, getAction) match {
      case KO(throwable) => alert(throwable.getMessage)
      case OK(unit) =>
    }
  }
}