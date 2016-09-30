package scalajs.jquery.logic

import scalajs.shared._
import scalajs.shared.util.Validated
import scalajs.shared.util.OK
import scalajs.shared.util.KO
import scalajs.shared.util.JsLogger

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