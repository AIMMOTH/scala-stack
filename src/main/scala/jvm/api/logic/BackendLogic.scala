package jvm.api.logic

import jvm.datastore.entity.ResourceEntity
import scalajs.shared._
import jvm.datastore.Objectify
import com.google.gson.Gson
import com.googlecode.objectify.Key
import scalajs.shared.util.Validated
import scalajs.shared.util.OK
import scalajs.shared.util.KO
import scalajs.shared.Resource
import scalajs.shared.util.JsLogger

/**
 * Logic to test
 */
trait BackendLogic {

  private lazy val gson = new Gson

  def create(json: String)(implicit log : JsLogger) : Validated[Throwable, ResourceEntity] = {

    log.info("Post!")
    
    try {
      val r = gson.fromJson(json, classOf[Resource])
      ResourceValidator(r)
  
      val entity = new ResourceEntity()
      entity.r = r
  
      log.debug("Saving ...")
    
      Objectify.save.entity(entity).now
  
      OK(entity)
    } catch {
      case t : Throwable => KO(t)
    }
  }
  
  def read(id : java.lang.Long) = {
    try {
      OK(Objectify.load.key(Key.create(classOf[ResourceEntity], id)).safe)
    } catch {
      case throwable : Throwable => KO(throwable)
    }
  }
}