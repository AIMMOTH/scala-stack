package api

import datastore.entity.ResourceEntity
import shared._
import datastore.Objectify
import com.google.gson.Gson
import com.googlecode.objectify.Key

/**
 * Logic to test
 */
trait BackendLogic {

  private lazy val gson = new Gson

  def create(json: String, log : org.slf4j.Logger) : Validated[Throwable, ResourceEntity] = {

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