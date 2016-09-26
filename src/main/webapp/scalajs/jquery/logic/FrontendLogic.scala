package jquery.logic

import shared._
import shared.util.Validated
import shared.util.OK
import shared.util.KO
import shared.util.JsLogger

trait FrontendLogic {
  
  def post(value : Integer, postAction : Resource => Unit)(implicit logger : JsLogger) : Validated[Throwable, Unit] = {
    
    logger.info("POST action")
    
    try {
      val test = new Resource(x = value)
      
      // Validated with shared logic
      ResourceValidator.apply(test)
      
      OK(postAction(test))
    } catch {
      case t : Throwable =>
        logger.error("POST fail")
        KO(t)
    }
  }
  
  def get(id : Long, getAction : Long => Unit)(implicit logger : JsLogger) =  {
    
    logger.info("GET action")
    
    try {
      OK(getAction(id))
    } catch {
      case t : Throwable =>
        logger.error("GET fail")
        KO(t)
    }
  }
}