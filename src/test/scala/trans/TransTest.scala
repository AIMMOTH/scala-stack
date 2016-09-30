package trans

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import com.google.appengine.tools.development.testing.LocalServiceTestHelper
import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity

import jvm.api.logic.BackendLogic
import jvm.datastore.Objectify
import scalajs.jquery.logic.FrontendLogic
import com.googlecode.objectify.ObjectifyService
import com.googlecode.objectify.util.Closeable
import org.slf4j.LoggerFactory
import com.google.gson.Gson
import scalajs.shared.util.OK
import scalajs.shared.util.KO
import jvm.builder.LoggerBuilder

class TransTest {
  
  class TestFrontendLogic extends FrontendLogic
  class TestBackendLogic extends BackendLogic
  
  lazy val gson = new Gson
  val logger = LoggerFactory.getLogger(getClass)
  implicit def jsLogger = LoggerBuilder(logger)
  val helper = new LocalServiceTestHelper()
  var closable : Closeable = null
  
  @Before def before : Unit = {
    helper.setUp();
    Objectify.registerClasses()
    closable = ObjectifyService.begin()
  }
  
  @After def after : Unit = {
    closable.close()
    helper.tearDown()
  }
  
  /**
   * Test
   * <ol>
   * <li>frontend post</li>
   * <li>backend save</li>
   * <li>frontend get</li>
   * <li>backend read</li>
   * </ol>
   */
  @Test
  def post : Unit = {
    val frontend = new TestFrontendLogic
    val backend = new TestBackendLogic
    
    frontend.post(22, resource => {
      backend.create(gson.toJson(resource)) match {
        case OK(resourceEntity) =>
          Assert.assertTrue(resourceEntity.r.x == 22)
          Assert.assertTrue(resourceEntity.id != null)
          
          val long = resourceEntity.id
          
          frontend.get(resourceEntity.id, long => ()) match {
            case OK(unit) => 
              backend.read(resourceEntity.id) match {
              case OK(entity) =>
              Assert.assertTrue(resourceEntity.id == entity.id)
              case KO(throwable) => throw throwable
              }
            case KO(throwable) => throw throwable
          }
          
        case KO(throwable) => throw throwable
      }
    })
  }
}