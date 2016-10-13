package scalajs.shared.util

/**
 * Useful parser when using HttpServletRequest.getRequestUri()
 * 
 * @see javax.servlet.http.HttpServletRequest.getRequestUri()
 */
class RequestUriParser extends UrlParser {

  private val requestUriParser = someRequest

  def someRequest = "/" ~> opt(path) ~ opt("?" ~> valuePairs) ^^ { case path ~ query => (path, query) }

  def applyOnUri(uri : String) = {
    parseAll(requestUriParser, uri) match {
      case Success(result, _)  => Right(result)
      case Failure(failure, _) => Left(failure)
      case Error(error, _)     => Left(error)
    }
  }
}

object RequestUriParser {
  lazy val requestUriParser = new RequestUriParser

  def apply(uri : String) = requestUriParser.applyOnUri(uri)
}