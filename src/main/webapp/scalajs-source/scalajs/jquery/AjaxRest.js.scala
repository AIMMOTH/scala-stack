package scalajs.jquery

import scala.scalajs.js.{ Any => JsAny }
import scala.scalajs.js.Dynamic
import scala.scalajs.js.Dynamic.{global, literal}
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.JSExport

import org.scalajs.dom.window.{ console, alert }
import org.scalajs.jquery.{ jQuery, JQueryXHR }
import org.scalajs.jquery.JQueryAjaxSettings

import scalajs.shared.Resource
import scalajs.shared.util.OK
import scalajs.shared.util.KO
import scalajs.jquery.logic.FrontendLogic
import scalajs.shared.html.ElementId
import scalajs.shared.util.JsLogger
import upickle.default.write

/**
 * Untested code. Put all logic into FrontendLogic and test it there.
 */
@JSExport
class AjaxRest {

  private lazy implicit val logger = JsLogger(global.console)

  private implicit def dynamicToXhr(literal : Dynamic) = literal.asInstanceOf[JQueryAjaxSettings]

  @JSExport
  def doPost() = {

    val value = jQuery("#" + ElementId.resourcePost).`val`().toString.toInt

    /*
     * Post action is using jQuery ajax and defining success and error.
     */
    val postAction : Resource => Unit = resource => {

      jQuery.ajax(
        "/api/v1/resource",
        settings = literal(
          data = "resource=" + write(resource),
          method = "POST",
          success = { (data : JsAny, textStatus : String, jqXHR : JQueryXHR) =>

            jQuery("#" + ElementId.resourceGet).`val`(JSON.stringify(data))
            alert("OK")
          },
          error = { (jqXhr : JQueryXHR, textStatus : String, errorThrown : String) =>

            alert(jqXhr.status + ":" + jqXhr.statusText)
          }))
    }

    FrontendLogic.post(value, postAction) match {
      case KO(throwable) =>
        alert(throwable.getMessage)
      case OK(unit) =>
    }
  }

  @JSExport
  def doGet() = {

    val id = jQuery("#" + ElementId.resourceGet).`val`().toString.toLong

    val getAction : Long => Unit = id => jQuery.ajax(
      "/api/v1/resource/" + id,
      settings = literal(
        success = { (data : JsAny, textStatus : String, jqXHR : JQueryXHR) =>

          jQuery("#" + ElementId.resourceOutput).`val`(JSON.stringify(data))
          alert("OK")
        },
        error = { (jqXhr : JQueryXHR, textStatus : String, errorThrown : String) =>

          alert(jqXhr.status + ":" + jqXhr.responseText)
        }))

    FrontendLogic.get(id, getAction) match {
      case KO(throwable) =>
        alert(throwable.getMessage)
      case OK(unit) =>
    }
  }
}