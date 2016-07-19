package servlet

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import scalatags.Text.all._
import scalatags.Text.all.{ `type` => type_ }
import scalatags.stylesheet._
import scalatags.Text.tags2.{ style => style2, title => title2 }

import javax.servlet.annotation.WebServlet

object Html {

  def apply(css: String, minified: Boolean = false) = {
    val min = if (minified) ".min" else ""
    html(
      head(
        title2("All Scala!"),
        /*
         * All css files should be read from resources and bundled into one.
         */
        link(rel := "stylesheet", href := s"/css/foundation$min.css"), // http://foundation.zurb.com/sites/docs/kitchen-sink.html
        link(rel := "stylesheet", href := s"/css/foundation-icons.css"), // http://zurb.com/playground/foundation-icon-fonts-3
        style2(type_ := "text/css")(css)),
      body(attr("ng-app") := "app")(
        div(cls := "row")(
          div(cls := "large-12 columns")(
            div(cls := "callout alert", id := "javascriptAlert")(
              h5("Compiling Scala JS to JavaScript ..."),
              p("Backend is now compiling Scala JS source code into a JavaScript, it should take a few seconds. Buttons are disabled in the meanwhile.")),
          h1("ISO Scala"),
          h2("POST Resource"),
          p("Enter number and create a resource!")(
            input(type_ := "number", value := 1, id := "resourcePost"),
            input(type_ := "button", onclick := "example.Rest().post()", value := "POST", cls := "button", disabled := true, id := "resourcePostButton")),
          h2("GET Resource"),
          p("Enter an id (already filled in if you recently posted one resource) and GET it")(
            input(type_ := "number", id := "resourceGet"),
            input(type_ := "button", onclick := "example.Rest().get()", value := "GET", cls := "button", disabled := true, id := "resourceGetButton"),
            textarea(disabled := true, id := "resourceOutput")),

        /*
       * Footer
       * All javascript should be read from resources and bundled into one file.
       */
        script(src := "/js/vendor/jquery.js"),
        script(src := "/js/vendor/what-input.js"),
        script(src := s"/js/vendor/foundation$min.js"),
        script(src := "javascript.js"),
        /*
       * Start Foundation and Angular
       */
        script()("example.Main().start();")))))
  }
}

object Stylisch extends StyleSheet {

  val yellowColor = cls(color := "yellow")

  val redBackground = cls(backgroundColor := "red")

  val hidden = cls(display := "none")
}

@WebServlet(name = "htmlBuilder", urlPatterns = Array("/index.scala", "/index.html"))
class Builder extends HttpServlet {

  private val style = Stylisch

  override def doGet(request: HttpServletRequest, response: HttpServletResponse) =
    response.getWriter().print(Html(style.styleSheetText).render)

}