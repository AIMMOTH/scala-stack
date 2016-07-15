package example

import org.junit.Test
import org.junit.Assert

class TestMain {
  
  @Test
  def testAlert : Unit = {
    val foo = new Foo(2)
    Assert.assertEquals(foo.toString, "Foo(2)")
  }
}