package webapp.script

import shared._

import upickle.default._

trait FrontendLogic {
  
  def create(getPostValue : () => Integer, postAction : Resource => Unit) = {
    val number = getPostValue()
    
    val test = new Resource
    test.x = number
    
    // Validated with shared logic
    ResourceValidator.apply(test)
    
    postAction(test)
    
  }
}