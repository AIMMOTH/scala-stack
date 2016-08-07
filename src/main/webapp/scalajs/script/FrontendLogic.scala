package scalajs

import shared._

trait FrontendLogic {
  
  def post(value : Integer, postAction : Resource => Unit) : Validated[Throwable, Unit] = {
    
    try {
      val test = new Resource(x = value)
      
      // Validated with shared logic
      ResourceValidator.apply(test)
      
      OK(postAction(test))
    } catch {
      case t : Throwable => KO(t)
    }
  }
  
  def get(id : Long, getAction : Long => Unit) =  {
    try {
      OK(getAction(id))
    } catch {
      case t : Throwable => KO(t)
    }
  }
}