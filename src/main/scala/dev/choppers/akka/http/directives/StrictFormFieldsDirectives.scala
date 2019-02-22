package dev.choppers.akka.http.directives

import akka.http.scaladsl.server._
import akka.http.scaladsl.server.directives.BasicDirectives

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.{FiniteDuration, _}

trait StrictFormFieldsDirectives {
  this: BasicDirectives =>

  def toStrict(timeout: FiniteDuration = 1 second): Directive[Unit] = {
    def toStrict0(inner: Unit ⇒ Route): Route = {
      extractMaterializer { implicit materializer =>
        val result: RequestContext ⇒ Future[RouteResult] = c ⇒ {
          // call entity.toStrict (returns a future)
          c.request.entity.toStrict(timeout).flatMap { strict ⇒
            // modify the context with the strictified entity
            val c1 = c.withRequest(c.request.withEntity(strict))
            // call the inner route with the modified context
            inner()(c1)
          }
        }
        result
      }
    }
    Directive[Unit](toStrict0)
  }

}
