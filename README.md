# iso-scala

HTML, JavaScript and CSS from Scala. [Live](http://iso-scala.appspot.com/)

## Build

Maven is used to run the project and SBT to build the JavaScript

### JavaScript

Build the JavaScript with SBT. The output will be placed in src/main/webapp

1. $ sbt
2. $ fastOptJS

### Webapp

Run webapp

1. $ mvn appengine:devserver
