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
import shared.OK
import shared.KO

/**
 * Untested code. All logic is put into BackendLogic
 */
@Path("resource")
class JerseyRest extends BackendLogic {
  
  private lazy val gson = new Gson
  private lazy val logger = LoggerFactory.getLogger(getClass)
  
  /**
   * POST http://localhost:8080/api/v1/resource
   */
  @POST
  @Produces(Array(MediaType.APPLICATION_JSON))
  def post(@FormParam("x") json : String) = {
    
    create(json, logger) match {
      
      case OK(entity) => gson.toJson(entity.id).toString match {
        case serialized => Response.ok(serialized).build
      }
      case KO(throwable) => errorResponse(throwable)
    }
  }
  
  /**
   * GET http://localhost:8080/api/v1/resource/123
   */
  @GET
  @Path("{id}")
  @Produces(Array(MediaType.APPLICATION_JSON))
  def get(@PathParam("id") id : java.lang.Long) = {
    
    logger.info("Get!")
    
     read(id) match {
      case OK(entity) => gson.toJson(entity.r).toString match {
          case json => Response.ok(json).build
        }
      case KO(throwable) => errorResponse(throwable)
    }
  }

  private def errorResponse(throwable : Throwable) : Response = {
    throwable.printStackTrace()
    errorResponse(throwable.getMessage)
  }
  
  private def errorResponse(message : String) : Response = {
    Response.serverError().entity(message).build
  }
  
}