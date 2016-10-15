package scalajs.shared.util

object UrlParserSheet {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

	val parser = new UrlParser                //> parser  : scalajs.shared.util.UrlParser = scalajs.shared.util.UrlParser@5cc7
                                                  //| c2a6

  parser.apply("hitta://user:password@subdomain.domain.topdomain:123///path?haj=då#saj=kaj") match {
    case Right(result) => println(result)
    case Left(error) => println(error)
  }                                               //> Right(UrlTokens(Some(hitta),Some((user,Some(password))),List(subdomain, doma
                                                  //| in, topdomain),Some(123),Some(List(, , path)),Some(List((haj,Some(dÃ¥)))),So
                                                  //| me(List((saj,Some(kaj))))))
}