package scalajs.shared.util

// https://github.com/scala/scala-parser-combinators
import scala.util.parsing.combinator.RegexParsers

case class UrlTokens(
    scheme : String,
    authorization : Option[(String, Option[String])],
    domain : Option[String],
    port : Option[Int],
    path : String,
    query : Option[String],
    fragment : Option[String]) extends RegexParsers {

  lazy val splitPath = HelperParser.splitPathBySlash(path)
  lazy val splitQueryToValuePairs = query map (HelperParser.splitIntoValuePairs)
  lazy val splitFragmentToValuePairs = fragment map (HelperParser.splitIntoValuePairs)
}

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

  def urlParser = scheme ~ opt(domain) ~ path ~ opt(query) ~ opt(fragment) ^^ {
    case scheme ~ domain ~ path ~ query ~ fragment => domain match {
      case Some((authorization, domain, port)) =>
        Right(new UrlTokens(scheme, authorization, Some(domain), port, path, query, fragment))
      case None =>
        Right(new UrlTokens(scheme, None, None, None, path, query, fragment))
    }
  }

  val notSlash = """[^\/]+""".r
  val notDot = """[^\.]+""".r
  val notColon = """[^:]+""".r
  val notAt = """[^@]+""".r
  val notColonOrSlash = """[^:\/]+""".r
  val numbers = """\d+""".r
  val notQuestionmarkOrHash = """[^\?#]*""".r
  val notHash = """[^\#]*""".r
  val any = """.*""".r

  def scheme = notColon <~ ":"

  def domain = "//" ~> opt(authorization <~ "@") ~ notColonOrSlash ~ opt(":" ~> port) <~ "/" ^^ {
    case optionalAuthorization ~ domains ~ optionalPort => (optionalAuthorization, domains, optionalPort)
  }
  def authorization = notColon ~ opt(":" ~> notAt) ^^ { case user ~ optionalPassword => (user, optionalPassword) }
  def port = numbers ^^ { case number => number.toInt }

  def path = notQuestionmarkOrHash
  def query = "?" ~> notHash
  def fragment = "#" ~> any

  def apply(url : String) = toResult(url, urlParser)
  def applyWith(url : String, parser : Parser[_]) = toResult(url, parser)

  def toResult(text : String, parser : Parser[_]) = {
    parseAll(parser, text) match {
      case Success(result, _) => Right(result)
      case Failure(error, _)  => Left(error)
      case Error(error, _)    => Left(error)
    }
  }
}

object HelperParser extends RegexParsers {

  implicit def parserToOption[T](parserResult : ParseResult[T]) = parserResult match {
    case Success(result, _) => Some(result)
    case _                  => None
  }

  val notDot = """[^\.]+""".r
  def domainsByDot = repsep(notDot, ".")
  def splitDomainByDot(domain : String) : Option[List[String]] = parseAll(domainsByDot, domain)

  val notSlash = """[^\/]*""".r
  def pathBySlash = repsep(notSlash, "/")
  def splitPathBySlash(path : String) : Option[List[String]] = parseAll(pathBySlash, path)

  val notEquals = """[^\=]+""".r
  def valuePairs = repsep(pair, "&")
  def pair = notEquals ~ "=" ~ opt(notEquals) ^^ {
    case key ~ equal ~ optionalValue => (key, optionalValue)
  }
  /**
   * Split by ambersand (&) and then equals (=)
   */
  def splitIntoValuePairs(text : String) : Option[List[(String, Option[String])]] = parseAll(valuePairs, text)
}

object UrlParser {
  lazy val urlParser = new UrlParser

  def apply(url : String) = urlParser(url)
}

