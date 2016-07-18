package example

import org.junit.Test
import org.junit.Assert

class TestSimpleController {
  
  @Test
  def testController : Unit = {
    val controller = new SimpleController()
    Assert.assertTrue(controller.simpleInt == 0)
    controller.inc()
    Assert.assertTrue(controller.simpleInt == 1)
    controller.dec()
    Assert.assertTrue(controller.simpleInt == 0)
  }
}