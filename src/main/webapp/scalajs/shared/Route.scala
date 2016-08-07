package shared

object Route {

  def apply(route: Option[String] = None) = {
    route match {
      case Some(route) if route == "/" | route.startsWith("/index") => Html(new Stylisch, route.endsWith(".min.html"))
      case _ => Four04()
    }
  }
}