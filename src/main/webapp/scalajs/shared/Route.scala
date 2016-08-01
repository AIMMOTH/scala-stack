package shared

object Route {

  def apply(route: Option[String] = None) = {
    route match {
      case None | Some("") | Some("/") | Some("/index.html") => Html(new Stylisch)
      case route => throw new IllegalArgumentException(s"No route match '$route'!")
    }
  }
}