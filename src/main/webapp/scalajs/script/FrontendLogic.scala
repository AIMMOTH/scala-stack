package webapp.script

import shared._

trait FrontendLogic {
  
  def create(getPostValue : () => Integer, postAction : Resource => Unit) = {
    val number = getPostValue()
    
    val test = new Resource()
    /*
     * This could be replaced by implicit def (if you dare):
      implicit def dynamicToInt(d : Dynamic) : Int = d.asInstanceOf[String].toInt
      test.x = number
     * 
     */
    test.x = number
    
    // Validated with shared logic
    ResourceValidator.apply(test)
    
    postAction(test)
    
  }
}