package dev.choppers.akka.http.json

import java.time.{Instant, LocalDate, LocalDateTime, LocalTime}

import spray.json._

trait JavaDateTimeJsonProtocol {

  implicit object LocalDateJsonFormat extends RootJsonFormat[LocalDate] {

    def write(ld: LocalDate): JsValue = JsString(ld.toString)

    def read(value: JsValue): LocalDate = value match {
      case JsString(date) => LocalDate.parse(date)
      case _ => throw new DeserializationException("Invalid Date")
    }
  }

  implicit object LocalDateTimeJsonFormat extends RootJsonFormat[LocalDateTime] {

    def write(ldt: LocalDateTime): JsValue = JsString(ldt.toString)

    def read(value: JsValue): LocalDateTime = value match {
      case JsString(date) => LocalDateTime.parse(date)
      case _ => throw new DeserializationException("Invalid Date")
    }
  }

  implicit object LocalTimeJsonFormat extends RootJsonFormat[LocalTime] {

    def write(lt: LocalTime): JsValue = JsString(lt.toString)

    def read(value: JsValue): LocalTime = value match {
      case JsString(time) => LocalTime.parse(time)
      case _ => throw new DeserializationException("Invalid Time")
    }
  }

  implicit object InstantJsonFormat extends RootJsonFormat[Instant] {

    def write(i: Instant): JsValue = JsString(i.toString)

    def read(value: JsValue): Instant = value match {
      case JsString(date) => Instant.parse(date)
      case _ => throw new DeserializationException("Invalid Instant")
    }
  }

}
