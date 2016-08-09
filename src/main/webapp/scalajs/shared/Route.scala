package shared

import scala.Left
import scala.Right

import scalatags.Text.TypedTag
import shared.html.ClientError
import shared.html.Index
import shared.html.ServerError

object Route {

  /**
   * Either redirects or responds with an HTML page
   */
  def apply(uri: String)(implicit logger: String => Unit = println) : Either[String, TypedTag[String]] = {

    /*
     * 1 Read uri until '?' 
     * 2 Split by '/'
     * 3 Drop first empty string since split on "/path" gives Array("", "path")
     * 4 To list 
     */
    val path = uri.takeWhile(_ != '?').split("/").drop(1).toList

    logger(s"Routing ${path.mkString(",")}")

    // Find the bug!
    path match {
      case Nil => Left(s"/${Languages.default.code}/index.min.html") // Redirect
      case language :: file :: Nil if file.startsWith("index") =>
        (file.endsWith(".min.html"), Languages.all.find(_.code == language)) match {
          case (minified, Some(language)) => Right(Index(new Stylisch, minified, language))
          case (minified, None)           => Left(s"/${Languages.default.code}/index.min.html") // Unknown language, redirect
        }
      case "404" :: Nil => Right(ClientError.NotFound())
      case "5xx" :: Nil => Right(ServerError.InternalServerError())
      case _            => Left("/404") // Redirect
    }
    // Answer /en-gb/index.stuff.min.html is the same as /en-gb/index.min.html
  }
}