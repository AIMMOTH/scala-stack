package api

import datastore._
import shared._

trait BackendLogic {

  def create(r: Resource, save: ResourceEntity => Unit, info : String => Unit) = {

    info("Post!")
    
    ResourceValidator(r)

    val entity = new ResourceEntity()
    entity.r = r

    save(entity)

    entity
  }
}