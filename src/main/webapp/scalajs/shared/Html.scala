package shared

import scalatags.Text.all._
import scalatags.Text.all.{ `type` => type_ }
import scalatags.stylesheet._
import scalatags.Text.tags2.{ style => style2, title => title2 }

object Id extends Enumeration {
  type Id = Value
  val javascriptAlert, resourcePostButton, resourceGetButton, resourcePost, resourceGet, resourceOutput = Value
}

object Html {
  
  def apply(css: Stylisch, language : Languages.Language = Languages.default, minified: Boolean = false) = {
  
    // From reflection get "package.Class().method()"
    def methodPath(klass : Class[_], methodName : String) = s"${klass.getCanonicalName}().$methodName()"
    
    val (postFunction, getFunction) = classOf[scalajs.AjaxRest] match {
      case restClass =>
        (methodPath(restClass, "post"), methodPath(restClass, "get"))
      }
    val documentReady = methodPath(classOf[scalajs.Document], "ready") // scalajs.Document().ready()
    
    val min = if (minified) ".min" else ""

    html(
      head(
        title2("All Scala!"),
        /*
         * TODO: All css files could be read from resources and bundled into one.
         */
        link(rel := "stylesheet", href := s"/css/foundation$min.css"), // http://foundation.zurb.com/sites/docs/kitchen-sink.html
        link(rel := "stylesheet", href := s"/css/foundation-icons.css"), // http://zurb.com/playground/foundation-icon-fonts-3
        style2(type_ := "text/css")(css.styleSheetText)),
        
      body()(
        div(cls := "row")(
          div(cls := "large-12 columns")(
              
            div(cls := "callout alert", id := Id.javascriptAlert.toString())(
              h5("Compiling Scala JS to JavaScript ..."),
              p("Backend is now compiling Scala JS source code into a JavaScript, it should take a few seconds. Buttons are disabled in the meanwhile.")),
              
            h1(Translations.documentHeader.get(language)),
            
            h2("POST Resource"),
            p("Enter number and create a resource!")(
              input(type_ := "number", value := 1, id := Id.resourcePost.toString()),
              input(type_ := "button", onclick := postFunction, value := "POST", cls := "button", disabled := true, id := Id.resourcePostButton.toString())),
              
            h2("GET Resource"),
            p("Enter an id (already filled in if you recently posted one resource) and GET it")(
              input(type_ := "number", id := Id.resourceGet.toString()),
              input(type_ := "button", onclick := getFunction, value := "GET", cls := "button", disabled := true, id := Id.resourceGetButton.toString()),
              textarea(disabled := true, id := Id.resourceOutput.toString())),
              
            p("Source at ")(a(target := "_blank", href := "https://github.com/AIMMOTH/scala-stack/tree/jquery")("GitHub")),

            /*
       * TODO: All javascript could be read from resources and bundled into one file.
       */
            script(src := "/js/vendor/jquery.js"),
            script(src := "/js/vendor/what-input.js"),
            script(src := s"/js/vendor/foundation$min.js"),
            script(src := "javascript.js"),
            /*
       * Start Foundation
       */
            script()(documentReady)))))
  }
}