package scalajs

import shared._

trait FrontendLogic {
  
  def create(getPostValue : () => Integer, postAction : Resource => Unit) : Result[Throwable, Long] = {
    
    try {
      val number = getPostValue()
      
      val test = new Resource(x = number)
      
      // Validated with shared logic
      ResourceValidator.apply(test)
      
      postAction(test)
    } catch {
      case t : Throwable => 
        Failure(t)
    }
  }
}