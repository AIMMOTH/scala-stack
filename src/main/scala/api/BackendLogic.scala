package api

import datastore.entity.ResourceEntity
import shared._

trait BackendLogic {

  def create(r: Resource, save: ResourceEntity => Unit, log : org.slf4j.Logger) : Result[Throwable, ResourceEntity] = {

    log.info("Post!")
    
    try {
      ResourceValidator(r)
      
  
      val entity = new ResourceEntity()
      entity.r = r
  
      log.debug("Saving ...")
    
      save(entity)
  
      Success(entity)
    } catch {
      case t : Throwable =>
        Failure(t)
    }
  }
}