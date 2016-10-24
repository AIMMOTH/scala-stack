package scalajs.shared.util

object UrlParserSheet {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  scalajs.shared.util.UrlParser("scheme://domain/path") match {
    case Right(result) => println(result)
    case Left(error) => println(error)
  }                                               //> Right(UrlTokens(scheme,None,Some(domain),None,path,None,None))
}