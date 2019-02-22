Akka - Reusable functionality
=============================
Akka reusable functionality such as booting Akka Http.

Project built with the following (main) technologies:

- Scala

- SBT

- Akka

- Specs2

Introduction
------------
Useful functionality related to Akka e.g.
- Booting an Akka Http microservice

Prerequisites
-------------
The following applications are installed and running:

* [Scala 2.11.8](http://www.scala-lang.org/)
* [SBT](http://www.scala-sbt.org/)
    - For Mac:
      ```
      brew install sbt
      ```

Publishing
-------
- Publish to your local repository
  ```
  sbt publish-local
  ```
  
Testing
---------
- Run Unit tests
  ```
  sbt test
  ```
  
- Run one test
  ```
  sbt test-only *AkkaHttpBootSpec
  ```
  
Example Usage
-------------

- Create Akka Http routings - HTTP contract/gateway to your microservice:
```scala
object ExampleRouting1 extends ExampleRouting1

trait ExampleRouting1 extends Routing with SprayJsonSupport with DefaultJsonProtocol {
  val route =
    pathPrefix("example1") {
      pathEndOrSingleSlash {
        get {
          complete { OK -> JsObject("status" -> JsString("Congratulations 1")) }
        }
      }
    }
}

object ExampleRouting2 extends ExampleRouting2

trait ExampleRouting2 extends Routing with SprayJsonSupport with DefaultJsonProtocol {
  val route =
    pathPrefix("example2") {
      pathEndOrSingleSlash {
        get {
          complete { JsObject("status" -> JsString("Congratulations 2")) }
        }
      }
    }
}
```

- Create your application (App) utilitising your routings (as well as anything else e.g. configuration and booting/wiring Akka actors):
```scala
object ExampleBoot extends App with AkkaHttpBoot with Directives {
  val rejectionHandler = RejectionHandler.newBuilder()
    .handleNotFound { complete(NotFound -> JsObject("error" -> JsString("Whoops"))) }
    .result()

  val exceptionHandler = ExceptionHandler {
    case _: TestException =>
      extractUri { uri =>
        complete(UnprocessableEntity -> "I'm sorry but this does not work")
      }
  }

  implicit val akkaHttpConfig = AkkaHttpConfig(rejectionHandler = Some(rejectionHandler), exceptionHandler = Some(exceptionHandler))

  boot(ExampleRouting1, ExampleRouting2, ExampleRoutingExceptionHandler)
}
```

Code Coverage
-------------
SBT-scoverage a SBT auto plugin: https://github.com/scoverage/sbt-scoverage
- Run tests with coverage enabled by entering:
  ```
  sbt clean coverage test
  ```

After the tests have finished, find the coverage reports inside target/scala-2.11/scoverage-report
