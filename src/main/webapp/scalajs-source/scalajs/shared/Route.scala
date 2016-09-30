package scalajs.shared

import scala.Left
import scala.Right

import scalatags.Text.TypedTag
import scalajs.shared.html.ClientError
import scalajs.shared.html.Index
import scalajs.shared.html.ServerError
import scalajs.shared.util.JsLogger

object Route {

  /**
   * Finds a redirect or an HTML page.
   */
  def apply(uri : String)(implicit logger : JsLogger) : Option[Either[String, TypedTag[String]]] = {

    logger.debug(s"Routing $uri")

    implicit def htmlToSomeRight(html : TypedTag[String]) = Some(Right(html))
    implicit def stringToSomeLeft(redirect : String) = Some(Left(redirect))

    /*
     * 1 Read uri until '?' 
     * 2 Split by '/'
     * 3 Drop first empty string since split on "/path" gives Array("", "path")
     * 4 To list 
     */
    uri.takeWhile(_ != '?').split("/").drop(1).toList match {

      case Nil => Some(Left(s"/${Languages.default.code}/index.min.html")) // Redirect

      case subfolder :: subfolders => subfolder match {
        case "api"                                   => None
        case "favicon.ico"                           => None
        case "js"                                    => None
        case "css"                                   => None

        case languageCode => (Languages.all.find(_.code == languageCode), subfolders.head.split(".").toList) match {
          case (Some(language), fileparts) => fileparts match {
            case "index" :: _ => Index(new Stylisch, fileparts(0) == "min", language)
            case "404" :: _   => ClientError.NotFound(language)
            case "5xx" :: _   => ServerError.InternalServerError(language)
            case _            => s"/$languageCode/404" // Redirect
          }
          case (None, Nil) => s"/${Languages.default.code}/index.min.html" // Unknown language, redirect to index
          case _           => s"/${Languages.default.code}/404" // Unknown language, redirect
        }
      }
    }
  }
}