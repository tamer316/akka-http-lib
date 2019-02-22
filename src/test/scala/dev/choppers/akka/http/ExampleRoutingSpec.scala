package dev.choppers.akka.http

import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{ExceptionHandler, MethodRejection, RejectionHandler, Route}
import org.specs2.mutable.Specification

class ExampleRoutingSpec extends Specification with RouteSpecification with ExampleRouting {

  private final case class Response(status: String)

  private final case class Error(error: String)

  private implicit def responseFormat = jsonFormat1(Response.apply)

  private implicit def errorFormat = jsonFormat1(Error.apply)

  "Example routing" should {
    "be available" in {
      Get("/example") ~> route ~> check {
        status mustEqual OK
        contentType.mediaType mustEqual `application/json`
        (responseAs[Response]).status mustEqual "Congratulations"
      }
    }

    "indicate when a required route is not recognised using default rejection" in {
      Get("/example/non-existing") ~> route ~> check {
        rejection must beAnInstanceOf[MethodRejection]
      }
    }

    "indicate when a required route is not recognised using default rejection" in {
      Get("/example/non-existing") ~> Route.seal(route) ~> check {
        status mustEqual MethodNotAllowed
        contentType.mediaType mustEqual `text/plain`
        responseAs[String] mustEqual "HTTP method not allowed, supported methods: POST"
      }
    }

    "indicate when a required route is not recognised using custom rejection" in {
      implicit val rejectionHandler = RejectionHandler.newBuilder()
        .handleAll[MethodRejection] { _ =>
        complete(MethodNotAllowed -> Error("Whoops"))
      }.result()

      Get("/example/non-existing") ~> Route.seal(route) ~> check {
        status mustEqual MethodNotAllowed
        contentType.mediaType mustEqual `application/json`
        (responseAs[Error]).error mustEqual "Whoops"
      }
    }

    "use default exception handling" in {
      Get("/example/error") ~> route ~> check {
        status mustEqual InternalServerError
      }
    }

    "use custom exception handling" in {
      implicit val exceptionHandler = ExceptionHandler {
        case _: IllegalStateException =>
          extractUri { uri =>
            complete(UnprocessableEntity -> "I'm sorry but this does not work")
          }
      }

      Get("/example/error") ~> route ~> check {
        status mustEqual UnprocessableEntity
      }
    }
  }
}