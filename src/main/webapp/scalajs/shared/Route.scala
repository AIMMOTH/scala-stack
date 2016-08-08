package shared

import shared.html._
import scalatags.Text.TypedTag
import org.glassfish.jersey.uri.UriTemplate

object Route {
  
  def apply(uri: String)(implicit logger : String => Unit = println) : Either[String, TypedTag[String]] = {
    
    /*
     * 1 Read uri until '?' 
     * 2 Split by '/'
     * 3 Drop first empty string since split on "/path" gives Array("", "path")
     */
    val path = uri.takeWhile(_ != '?').split("/").drop(1)
    
    logger(s"Routing ${path.mkString(",")}")
    
    path match {
      case Array() => Left(s"/${Languages.default.code}/")
      case Array(language) => (uri.endsWith(".min.html"), Languages.all.find(_.code == language)) match {
        case (minified, Some(language)) => Right(Index(new Stylisch, minified, language))
        case (minified, None) => Right(Index(new Stylisch, minified, Languages.default))
      }
      case Array("404") => Right(ClientError.NotFound())
      case Array("5xx") => Right(ServerError.InternalServerError())
      case _ => Left("/404")
    }
  }
}