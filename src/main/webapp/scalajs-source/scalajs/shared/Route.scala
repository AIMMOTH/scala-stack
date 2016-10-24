package scalajs.shared

import scala.scalajs.js.annotation.JSExport

import scalajs.shared.Languages.Language
import scalajs.shared.html.ClientError
import scalajs.shared.html.Index
import scalajs.shared.util.HelperParser
import scalajs.shared.util.JsLogger
import scalajs.shared.util.RequestUriParser
import scalajs.shared.util.RequestUriTokens
import scalatags.Text.TypedTag

@JSExport
object Route {

  lazy val pathTo404 = s"/${Languages.default.code}/404"

  trait RouteResult
  final case class Redirect(val uri : String, val language : Language = Languages.default) extends RouteResult {
    lazy val path = s"/${language.code}/$uri/"
  }
  final case class Html(file : TypedTag[String]) extends RouteResult
  final case class JavascriptCompiler() extends RouteResult

  lazy val javascriptCompiler = new JavascriptCompiler
  lazy val redirect404 = new Redirect("404")

  /**
   * Can find a redirect or an HTML page.
   */
  //  @JSExport
  def apply(path : String)(implicit logger : JsLogger) : Option[RouteResult] = {

    logger.debug(s"Routing $path")

    implicit def resultToOption(result : RouteResult) = Some(result)

    RequestUriParser(path) match {
      case Right(RequestUriTokens(Some(path), _)) => HelperParser.splitPathBySlash(path).getOrElse(Nil) match {
        case javascript :: _ if javascript.startsWith("javascript") => javascriptCompiler
        case "api" :: _ => None
        case "css" :: _ => None
        case "favicon.ico" :: _ => None
        case "js" :: _ => None
        case languageCode :: file :: _ => (Languages.all.find(_.code == languageCode), file.split("""\.""").toList) match {
          case (Some(language), "index" :: _) => new Html(Index(new Stylisch, file.endsWith(".min.html"), language))
          case (Some(language), "404" :: _)   => new Html(ClientError.NotFound(language))
          case (Some(language), _)            => new Redirect("404", language)
          case _                              => redirect404
        }
        case _ => redirect404
      }
      case _ => redirect404
    }
  }
}