package dev.choppers.akka.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsObject, JsString}

object ExampleRouting extends ExampleRouting

trait ExampleRouting extends Routing with SprayJsonSupport with DefaultJsonProtocol {
  val route =
    pathPrefix("example") {
      path("error") {
        get {
          throw new IllegalStateException("Oh dear")
        }
      } ~
        pathEndOrSingleSlash {
          get {
            complete {
              JsObject("status" -> JsString("Congratulations"))
            }
          }
        } ~
        post {
          path("submission") {
            complete("blah")
          }
        }
    }
}