package scalajs.shared

object TestRoutSheet {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  val test = new TestRoute                        //> test  : scalajs.shared.TestRoute = scalajs.shared.TestRoute@617c74e5
  test.testRoute                                  //> java.lang.Exception: No match
                                                  //| 	at scalajs.shared.TestRoute.testRoute(TestRoute.scala:26)
                                                  //| 	at scalajs.shared.TestRoutSheet$$anonfun$main$1.apply$mcV$sp(scalajs.sha
                                                  //| red.TestRoutSheet.scala:7)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$$anonfun$$exe
                                                  //| cute$1.apply$mcV$sp(WorksheetSupport.scala:76)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.redirected(W
                                                  //| orksheetSupport.scala:65)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.$execute(Wor
                                                  //| ksheetSupport.scala:75)
                                                  //| 	at scalajs.shared.TestRoutSheet$.main(scalajs.shared.TestRoutSheet.scala
                                                  //| :3)
                                                  //| 	at scalajs.shared.TestRoutSheet.main(scalajs.shared.TestRoutSheet.scala)
                                                  //| 
}