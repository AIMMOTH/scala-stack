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
import datastore.entity.ResourceEntity
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
      
      create(r, entity => Objectify.save.entity(entity).now, logger) match {
        case shared.Success(entity) =>
          Response.ok(gson.toJson(entity.id).toString).build
        case shared.Failure(throwable) =>
          errorResponse(throwable)
      }
      
      
    } catch {
      case e : Throwable =>
        errorResponse(e)
    }
  }
  
  private def errorResponse(throwable : Throwable) : Response = {
    throwable.printStackTrace()
    errorResponse(throwable.getMessage)
  }
  
  private def errorResponse(message : String) : Response = {
    Response.serverError().entity(message).build
  }
  
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  def get(@QueryParam("id") id : java.lang.Long) = {
    
    logger.info("Get!")
    
    Objectify.load.key(Key.create(classOf[ResourceEntity], id)).now match {
      case null =>
        errorResponse("Could not find entity.")
      case entity =>
        Response.ok(gson.toJson(entity.r).toString).build
    }
  }
}