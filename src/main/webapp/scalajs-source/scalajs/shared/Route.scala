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

    /*
     * 1 Read uri until '?' 
     * 2 Split by '/'
     * 3 Drop first empty string since split on "/path" gives Array("", "path")
     * 4 To list 
     */
    uri.takeWhile(_ != '?').split("/").drop(1).toList match {

      case Nil => Some(Left(s"/${Languages.default.code}/index.min.html")) // Redirect

      case subfolder :: subfolders => subfolder match {
        case "javascript"  => None
        case "api"         => None
        case "favicon.ico" => None
        case "js"          => None
        case "css"         => None
        case "404"         => Some(Right(ClientError.NotFound()))
        case "5xx"         => Some(Right(ServerError.InternalServerError()))

        case languageCode => (Languages.all.find(_.code == languageCode), subfolders.head.split(".").toList) match {
          case (Some(language), file) => file match {
            case "index" :: fileparts => Some(Right(Index(new Stylisch, fileparts(0) == "min", language)))
            case _                    => Some(Left("/404")) // Redirect
          }
          case (None, Nil)  => Some(Left(s"/${Languages.default.code}/index.min.html")) // Unknown language, redirect to index
          case (None, file) => Some(Left(s"/${Languages.default.code}/$file")) // Unknown language, redirect
        }
      }
    }
  }
}