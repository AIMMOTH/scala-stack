package datastore

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Cache
import com.googlecode.objectify.annotation.Id
import shared.Resource

/**
 * Scala JS can only handle one constructor which needs to be empty to make this class serializable.
 */
@Entity
@Cache
class ResourceEntity {
  
  @Id var id : java.lang.Long = null
  var r : Resource = null
  
}