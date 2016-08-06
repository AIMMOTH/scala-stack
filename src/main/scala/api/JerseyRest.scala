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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("resource")
class JerseyRest extends BackendLogic {
  
  private lazy val gson = new Gson
  private lazy val logger = LoggerFactory.getLogger(getClass)
  
  /*
   * POST http://localhost:8080/api/v1/resource
   */
  @POST
  @Produces(Array(MediaType.APPLICATION_JSON))
  def post(@FormParam("x") json : String) = {
    
    try {
      
      val r = gson.fromJson(json, classOf[Resource])
      
      val entity = create(r, entity => Objectify.save.entity(entity).now, logger.info)
      
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
    
    logger.info("Get!")
    
    val e = Objectify.load.key(Key.create(classOf[ResourceEntity], id)).now
    
    Response.ok(gson.toJson(e.r).toString).build
  }
}