package datastore

import com.googlecode.objectify.ObjectifyService
import com.googlecode.objectify.VoidWork

object Objectify {
  
  ObjectifyService.register(classOf[ResourceEntity])
  
  def load = ObjectifyService.ofy.load
  
  def save = ObjectifyService.ofy.save
  
  def delete = ObjectifyService.ofy.delete
  
  def transaction(work : VoidWork) = ObjectifyService.ofy.transactNew(100, work)
}