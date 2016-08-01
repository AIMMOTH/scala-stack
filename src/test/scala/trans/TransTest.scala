package trans

import org.junit.Test
import api.BackendLogic
import webapp.script.FrontendLogic
import datastore.Objectify
import org.junit.Assert

class TransTest {
  
  class TestFrontendLogic extends FrontendLogic
  class TestBackendLogic extends BackendLogic
  
  @Test
  def post : Unit = {
    val frontend = new TestFrontendLogic
    val backend = new TestBackendLogic
    
    frontend.create(() => 22, resource => {
      backend.create(resource, entity => Objectify.save.entity(entity).now, println) match {
        case resourceEntity =>
          Assert.assertTrue(resourceEntity.r == 22)
          Assert.assertTrue(resourceEntity.id != null)
      }
    })
    
  }
}