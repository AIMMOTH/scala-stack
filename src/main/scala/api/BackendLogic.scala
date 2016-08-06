package api

import datastore.entity.ResourceEntity
import shared._

trait BackendLogic {

  def create(r: Resource, save: ResourceEntity => Unit, log : String => Unit) : Result[Throwable, ResourceEntity] = {

    log("Post!")
    
    try {
      ResourceValidator(r)
      
  
      val entity = new ResourceEntity()
      entity.r = r
  
      save(entity)
  
      Success(entity)
    } catch {
      case t : Throwable =>
        Failure(t)
    }
  }
}