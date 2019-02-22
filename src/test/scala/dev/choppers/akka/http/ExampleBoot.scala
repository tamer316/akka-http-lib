package dev.choppers.akka.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{Directives, ExceptionHandler, RejectionHandler}
import spray.json.{DefaultJsonProtocol, JsObject, JsString}

/**
  * Example of booting an Akka Http microservice
  */
object ExampleBoot extends App with AkkaHttpBoot {
  implicit val config = AkkaHttpConfig()

  boot(ExampleRouting1, ExampleRouting2)
}

/**
  * Example of booting an Akka Http microservice using custom failure handling
  */
object ExampleBootWithFailureHandling extends App with AkkaHttpBoot with SprayJsonSupport with DefaultJsonProtocol with Directives {
  val rejectionHandler = RejectionHandler.newBuilder()
    .handleNotFound {
      complete(NotFound -> JsObject("error" -> JsString("Whoops")))
    }
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

/**
  * Routing example 1
  * <pre>
  * curl http://localhost:9100/example1
  * </pre>
  */
object ExampleRouting1 extends ExampleRouting1

trait ExampleRouting1 extends Routing with SprayJsonSupport with DefaultJsonProtocol {
  val route =
    pathPrefix("example1") {
      pathEndOrSingleSlash {
        get {
          complete {
            OK -> JsObject("status" -> JsString("Congratulations 1"))
          }
        }
      }
    }
}

/**
  * Routing example 2
  * <pre>
  * curl http://localhost:9100/example2
  * </pre>
  */
object ExampleRouting2 extends ExampleRouting2

trait ExampleRouting2 extends Routing with SprayJsonSupport with DefaultJsonProtocol {
  val route =
    pathPrefix("example2") {
      pathEndOrSingleSlash {
        get {
          complete {
            JsObject("status" -> JsString("Congratulations 2"))
          }
        }
      }
    }
}

/**
  * Routing example to see failure handling
  * <pre>
  * curl http://localhost:9100/example-error
  * </pre>
  */
object ExampleRoutingExceptionHandler extends ExampleRoutingExceptionHandler

trait ExampleRoutingExceptionHandler extends Routing {
  val route =
    pathPrefix("example-error") {
      pathEndOrSingleSlash {
        get {
          complete {
            throw new TestException("This sounds daft, but your error was a success!")
          }
        }
      }
    }
}

class TestException(s: String) extends Exception(s)