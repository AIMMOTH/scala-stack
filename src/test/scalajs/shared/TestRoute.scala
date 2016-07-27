package shared

import org.junit.Test
import org.junit.Assert

class TestRoute {
  
  @Test
  def testRoute : Unit = {
    val routes = List(Route(None), Route(Some("")), Route(Some("/")), Route(Some("/index.html")))
    
    Assert.assertTrue(routes.tail.forall(_ == routes.head))
  }
}