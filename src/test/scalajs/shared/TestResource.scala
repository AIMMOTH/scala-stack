package shared

import shared._
import scala.scalajs.js.Dynamic
import org.junit.Test
import org.junit.Assert

class TestResource {
  
  @Test
  def dynamic : Unit = {
    val r = new Resource
    r.x = 10
    Assert.assertTrue(r.x == 10)
  }
}