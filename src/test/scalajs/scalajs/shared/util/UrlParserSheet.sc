package scalajs.shared.util

object UrlParserSheet {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  scalajs.shared.util.UrlParser("scheme://user:password@subdomain.domain.topdomain:123/path/subpath?key=value&haj#key2=value2&more") match {
    case Right(result) => println(result)
    case Left(error) => println(error)
  }                                               //> Right(UrlTokens(scheme,Some((user,Some(password))),Some(subdomain.domain.top
                                                  //| domain),Some(123),path/subpath,Some(key=value&haj),Some(key2=value2&more)))
                                                  //| 
}