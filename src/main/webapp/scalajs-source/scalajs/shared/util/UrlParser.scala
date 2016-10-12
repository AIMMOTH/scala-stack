package scalajs.shared.util

import scala.util.parsing.combinator.RegexParsers

case class UrlTokens(
    scheme : Option[String],
    authorization : Option[(String, Option[String])],
    domains : List[String],
    port : Option[Int],
    path : Option[List[String]],
    query : Option[List[(String, Option[String])]]
  )

/**
 * <p>
 * From Wikipedia:
 * <pre>
 * scheme:[//[user:password@]host[:port]][/]path[?query][#fragment]
 * </pre>
 * </p>
 * 
 * <p>
 * Parsing an URL:
 * </p>
 * <ol>
 * <li>Divide full URL at optional "://" to get scheme and "domain and" (the rest)</li>
 * <li>Divide "domain and" at optional "/" and optional "?" to get domain and/or path and/or query</li>
 * <li>Divide domain to optional authorization, domains and optional port</li>
 * <li>Divide path by "/"</li>
 * <li>Divide query by "&" and divide each pair at "=" to get key and optional value</li>
 * <ol>
 * 
 */
class UrlParser extends RegexParsers {

  def expression = all ^^ {
    case (scheme, ((authorization, domains, port), path, query )) =>
      new UrlTokens(scheme, authorization, domains, port, path, query)
  }
  
  val notSlash = """[^\/]+""".r 
  val notDot = """[^\.]+""".r
  val notColon = """[^:]+""".r
  val notDotOrColon = """[^:\.]+""".r
  val notAt = """[^@]+""".r
  val notSlashOrQuestionmark = """[^\/\?]+""".r
  val numbers = """\d+""".r
  val encoded = """[^\=\&\/]+""".r
  
  def all = opt(notColon <~ "://") ~ domainAnd ^^ { case optionalScheme ~ domainAnd => (optionalScheme, domainAnd) }
  
  def domainAnd = domain ~ opt("/" ~> path) ~ opt("?" ~> query) ^^ { case domain ~ optionalPath ~ optionalQuery => (domain, optionalPath, optionalQuery) }
  
  def domain = opt(authorization <~ "@") ~ domains ~ opt(":" ~> port) ^^ { case optionalAuthorization ~ domains ~ optionalPort => (optionalAuthorization, domains, optionalPort) }

  def authorization = notColon ~ opt(":" ~> notAt) ^^ { case user ~ optionalPassword => (user, optionalPassword) }
  def domains = repsep(notDotOrColon, ".")
  def port = numbers ^^ { case number => number.toInt }
  
  def path = repsep(notSlashOrQuestionmark, "/")
  def query = repsep(pair, "&")
  def pair = encoded ~ "=" ~ opt(encoded) ^^ { case key ~ equal ~ optionalValue => (key, optionalValue) }
  
  def apply(url : String) = {
    parseAll(expression, url) match {
      case Success(result, _) => Right(result)
      case Failure(error, _) => Left(error)
      case Error(error, _) => Left(error)
    }
  }
}

object UrlParser {
  lazy val urlParser = new UrlParser
  
  def apply(url : String) = urlParser(url)
}