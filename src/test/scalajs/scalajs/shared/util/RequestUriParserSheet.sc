package scalajs.shared.util

object RequestUriParserSheet {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  RequestUriParser("/path1/subpath?key1=value1&value2=") match {
  case Right(result) => println(result)
  case Left(fail) => println(fail)
  }                                               //> (Some(List(path1, subpath)),Some(List((key1,Some(value1)), (value2,None))))
                                                  //| 
}