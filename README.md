# SCALA STACK

Backend serve HTML, JavaScript and CSS from Scala! No frontend build (gulp, grun), no files (js, css, html, sass, less)!

## Description

Backend Scala servlets serve a HTML file build with ScalaTags and ScalaCSS, and a JavaScript hot compiled from Scala JS source read from webapp. 

## Environment

Maven project with Google Fleixble Environment with Java 8, Servlet 3.1 and Scala 2.11.

## Live

Check out [live](https://iso-scala-us.appspot.com/)

## Run and Deploy

This project is dependent on a second project [scalajs-compiler-jetty](https://github.com/AIMMOTH/scalajs-compiler-jetty). Either clone and install it with maven or use copy of JAR included in src/main/resources.

Use maven and run $ mvn gcloud:run or $ mvn gcloud:deploy. Make sure to have GCloud installed and Python on path!

Another way is to compile (use IDE like eclipse) and copy dependencies into webapp, and use the python to run "dev_appserver.py" included in GCloud SDK.
