package api

import javax.ws.rs._
import javax.ws.rs.core._
import com.google.gson.Gson
import com.googlecode.objectify.Key
import shared.ResourceValidator
import datastore.Objectify
import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Entity
import shared.Resource
import datastore.ResourceEntity

@Path("resource")
class JerseyRest {
  
  private lazy val gson = new Gson
  
  /*
   * POST http://localhost:8080/api/v1/resource
   */
  @POST
  @Produces(Array(MediaType.APPLICATION_JSON))
  def post(@FormParam("x") x : Int) = {
    try {
      
      val r = new Resource
      r.x = x
      ResourceValidator(r)
      
      val entity = new ResourceEntity()
      entity.r = r
      
      Objectify.save.entity(entity).now
      
      Response.ok(gson.toJson(entity.id).toString).build
      
    } catch {
      case e : Throwable =>
        e.printStackTrace()
        Response.serverError().entity(e.getMessage).build
    }
  }
  
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  def get(@QueryParam("id") id : java.lang.Long) = {
    
    val e = Objectify.load.key(Key.create(classOf[ResourceEntity], id)).now
    
    Response.ok(gson.toJson(e.r).toString).build
  }
}