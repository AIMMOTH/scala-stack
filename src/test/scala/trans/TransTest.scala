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
import org.slf4j.LoggerFactory
import com.google.gson.Gson
import shared.OK
import shared.KO

class TransTest {
  
  class TestFrontendLogic extends FrontendLogic
  class TestBackendLogic extends BackendLogic
  
  private lazy val gson = new Gson
  private val logger = LoggerFactory.getLogger(getClass)
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
      backend.create(gson.toJson(resource), logger) match {
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