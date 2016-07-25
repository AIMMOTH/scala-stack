package shared

import scalatags.Text.all._
import scalatags.Text.all.{ `type` => type_ }
import scalatags.stylesheet._
import scalatags.Text.tags2.{ style => style2, title => title2 }

object Html {

  def apply(css: StyleSheet with Stylisch, minified: Boolean = false) = {
    
    // From reflection get "package.Class().method()"
    def methodPath(klass : Class[_], methodName : String) = s"${klass.getCanonicalName}().$methodName()"
    
    val (postFunction, getFunction) = classOf[com.github.aimmoth.script.AjaxRest] match {
      case restClass =>
        (methodPath(restClass, "post"), methodPath(restClass, "get"))
      }
    val documentReady = methodPath(classOf[com.github.aimmoth.script.Document], "ready")
    
    val min = if (minified) ".min" else ""

    html(
      head(
        title2("All Scala!"),
        /*
         * All css files could be read from resources and bundled into one.
         */
        link(rel := "stylesheet", href := s"/css/foundation$min.css"), // http://foundation.zurb.com/sites/docs/kitchen-sink.html
        link(rel := "stylesheet", href := s"/css/foundation-icons.css"), // http://zurb.com/playground/foundation-icon-fonts-3
        style2(type_ := "text/css")(css.styleSheetText),
        
      body(attr("ng-app") := "app")(
        div(cls := "row")(
          div(cls := "large-12 columns")(
            div(cls := "callout alert", id := "javascriptAlert")(
              h5("Compiling Scala JS to JavaScript ..."),
              p("Backend is now compiling Scala JS source code into a JavaScript, it should take a few seconds. Buttons are disabled in the meanwhile.")),
            h1("Scala Stack"),
            h2("POST Resource"),
            p("Enter number and create a resource!")(
              input(type_ := "number", value := 1, id := "resourcePost"),
              input(type_ := "button", onclick := postFunction, value := "POST", cls := "button", disabled := true, id := "resourcePostButton")),
            h2("GET Resource"),
            p("Enter an id (already filled in if you recently posted one resource) and GET it")(
              input(type_ := "number", id := "resourceGet"),
              input(type_ := "button", onclick := getFunction, value := "GET", cls := "button", disabled := true, id := "resourceGetButton"),
              textarea(disabled := true, id := "resourceOutput")),
            p("Source at ")(a(href := "https://github.com/AIMMOTH/scala-stack/tree/jquery")("GitHub")),

            /*
       * All javascript could be read from resources and bundled into one file.
       */
            script(src := "/js/vendor/jquery.js"),
            script(src := "/js/vendor/what-input.js"),
            script(src := s"/js/vendor/foundation$min.js"),
            script(src := "javascript.js"),
            /*
       * Start Foundation
       */
            script()(documentReady))))))
  }
}