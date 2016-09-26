package jquery

import scala.scalajs.js.{ Any => JsAny }
import scala.scalajs.js.Dynamic
import scala.scalajs.js.Dynamic._
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.JSExport

import org.scalajs.dom.window._
import org.scalajs.jquery._
import org.scalajs.jquery.JQueryAjaxSettings

import shared._
import shared.Resource
import shared.util.OK
import shared.util.KO
import jquery.logic.FrontendLogic
import shared.html.Id
import shared.util.JsLogger

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
      val r = resource.asInstanceOf[Dynamic]
      val s = JSON.stringify(r) // {"x$1":1}
      val s2 = s.replaceAll("""(\$1":)""", """":""") // Replace "x$1": with "x":
      
      jQuery.ajax("/api/v1/resource", settings = Dynamic.literal(
        data = "x=" + s2,
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