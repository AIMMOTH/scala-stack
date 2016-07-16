package example

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import com.google.gson.Gson
import javax.ws.rs.QueryParam

@Path("/api/v1/resource")
class JerseyRest {
  
  val gson = new Gson
  
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  def get(@QueryParam("x") x : Int) = gson.toJson(new Resource(x)).toString
}