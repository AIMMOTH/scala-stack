package jvm.datastore

import jvm.datastore.entity._
import com.googlecode.objectify.ObjectifyService
import com.googlecode.objectify.VoidWork

object Objectify {
  
  ObjectifyService.register(classOf[ResourceEntity])
  
  // To register classes
  def registerClasses() = {}
  
  def load = ObjectifyService.ofy.load
  
  def save = ObjectifyService.ofy.save
  
  def delete = ObjectifyService.ofy.delete
  
  def transaction(work : VoidWork) = ObjectifyService.ofy.transactNew(100, work)
}