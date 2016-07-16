package example

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import com.google.gson.Gson
import javax.ws.rs.QueryParam

@Path("resource")
class JerseyRest {
  
  val gson = new Gson
  
  /*
   * http://localhost:8080/api/v1/resource?x=3
   */
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  def get(@QueryParam("x") x : Int) = gson.toJson(new Resource(x)).toString
}