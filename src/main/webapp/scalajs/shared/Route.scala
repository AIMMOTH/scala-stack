package shared

import org.slf4j.LoggerFactory

object Route {

  private val log = LoggerFactory.getLogger(getClass)

  def apply(route: Option[String] = None) = {
    log.info(s"Matching route '$route'")
    route match {
      case None | Some("") | Some("/") | Some("/index.html") => Html(new Stylisch)
      case route => throw new IllegalArgumentException(s"No route match '$route'!")
    }
  }
}