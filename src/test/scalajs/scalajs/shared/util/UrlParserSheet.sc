package scalajs.shared.util

object UrlParserSheet {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  UrlParser("stuff://subdomain.domain.topdomain/path1/path2/#local=stuff") match {
  	case Right(result) =>
  		println(result)
  		case Left(error) =>
  		println(error)
  		}                                 //> UrlTokens(Some(stuff),None,List(subdomain, domain, topdomain),None,Some(List
                                                  //| (path1, path2, )),None,Some(List((local,Some(stuff)))))
}