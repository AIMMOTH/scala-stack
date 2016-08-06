package trans

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import com.google.appengine.tools.development.testing.LocalServiceTestHelper
import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity

import api.BackendLogic
import datastore.Objectify
import scalajs.FrontendLogic
import com.googlecode.objectify.ObjectifyService
import com.googlecode.objectify.util.Closeable

class TransTest {
  
  class TestFrontendLogic extends FrontendLogic
  class TestBackendLogic extends BackendLogic
  
  private val helper = new LocalServiceTestHelper()
  private var closable : Closeable = null
  
  @Before def before : Unit = {
      helper.setUp();
    Objectify.registerClasses()
    closable = ObjectifyService.begin()
  }
  
  @After def after : Unit = {
    closable.close()
    helper.tearDown()
  }
  
  @Test
  def post : Unit = {
    val frontend = new TestFrontendLogic
    val backend = new TestBackendLogic
    
    frontend.create(() => 22, resource => {
      backend.create(resource, entity => Objectify.save.entity(entity).now, println) match {
        case resourceEntity =>
          Assert.assertTrue(resourceEntity.r.x == 22)
          Assert.assertTrue(resourceEntity.id != null)
      }
    })
  }
}