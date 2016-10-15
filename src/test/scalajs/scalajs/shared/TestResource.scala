package scalajs.shared

import scalajs.shared._
import org.junit.Test
import org.junit.Assert
import scala.scalajs.js.annotation.JSExportAll

class TestResource {
  
  @Test
  def dynamic : Unit = {
    val r = new Resource(x = 10)
    Assert.assertTrue(r.x == 10)
  }
}