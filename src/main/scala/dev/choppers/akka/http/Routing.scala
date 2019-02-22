package dev.choppers.akka.http

import akka.http.scaladsl.server.{Directives, Route}

/**
  * Mix this trait into your "Routing" to define endpoints.
  */
trait Routing extends Directives {
  def route: Route
}

object Routing {

  implicit class RoutingOps(routing: Routing) {
    def ~(routings: Seq[Routing]) = routing +: routings

    def ~(anotherRouting: Routing) = Seq(routing, anotherRouting)
  }

  implicit class SeqRoutingOps(routings: Seq[Routing]) {
    def ~(otherRoutings: List[Routing]) = routings ++ otherRoutings

    def ~(anotherRouting: Routing) = routings :+ anotherRouting
  }

}