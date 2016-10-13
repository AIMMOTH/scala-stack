package scalajs.shared.util

// https://github.com/scala/scala-parser-combinators
import scala.util.parsing.combinator.RegexParsers

case class UrlTokens(
  scheme : Option[String],
  authorization : Option[(String, Option[String])],
  domains : List[String],
  port : Option[Int],
  path : Option[List[String]],
  query : Option[List[(String, Option[String])]],
  fragment : Option[List[(String, Option[String])]])

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
 * <li>Divide "domain and" at optional "/" and optional "?" and optional "#" to get domain and/or path and/or query and/or fragment</li>
 * <li>Divide domain to optional authorization, domains and optional port</li>
 * <li>Divide path by "/"</li>
 * <li>Divide query and fragment by "&" and divide each pair at "=" to get key and optional value</li>
 * <ol>
 *
 * @see http://www.scala-lang.org/files/archive/api/2.11.2/scala-parser-combinators/#scala.util.parsing.combinator.RegexParsers
 */
class UrlParser extends RegexParsers {

  private val urlParser = someUrl ^^ {
    case (scheme, ((authorization, domains, port), path, query, fragment)) =>
      new UrlTokens(scheme, authorization, domains, port, path, query, fragment)
  }

  val notSlash = """[^\/]+""".r
  val notDot = """[^\.]+""".r
  val notColon = """[^:]+""".r
  val notDotOrColonOrSlashOrQuestionmarkOrHash = """[^:\.\/\?\#]+""".r
  val notAt = """[^@]+""".r
  val notSlashOrQuestionmarkOrHash = """[^\/\?\#]+""".r
  val numbers = """\d+""".r
  val notEqualsOrAmpersandOrHash = """[^\=\&\/\#]+""".r

  def someUrl = opt(notColon <~ "://") ~ domainAnd ^^ { case optionalScheme ~ domainAnd => (optionalScheme, domainAnd) }

  def domainAnd = domain ~ opt("/" ~> path) ~ opt("?" ~> valuePairs) ~ opt("#" ~> valuePairs) ^^ { case domain ~ optionalPath ~ optionalQuery ~ optionalFragment => (domain, optionalPath, optionalQuery, optionalFragment) }

  def domain = opt(authorization <~ "@") ~ domains ~ opt(":" ~> port) ^^ { case optionalAuthorization ~ domains ~ optionalPort => (optionalAuthorization, domains, optionalPort) }

  def authorization = notColon ~ opt(":" ~> notAt) ^^ { case user ~ optionalPassword => (user, optionalPassword) }
  def domains = repsep(notDotOrColonOrSlashOrQuestionmarkOrHash, ".")
  def port = numbers ^^ { case number => number.toInt }

  def path = repsep(notSlashOrQuestionmarkOrHash | "", "/")
  def valuePairs = repsep(pair, "&")
  def pair = notEqualsOrAmpersandOrHash ~ "=" ~ opt(notEqualsOrAmpersandOrHash) ^^ { case key ~ equal ~ optionalValue => (key, optionalValue) }

  def apply(url : String) = {
    parseAll(urlParser, url) match {
      case Success(result, _) => Right(result)
      case Failure(error, _)  => Left(error)
      case Error(error, _)    => Left(error)
    }
  }
}

object UrlParser {
  lazy val urlParser = new UrlParser

  def apply(url : String) = urlParser(url)
}