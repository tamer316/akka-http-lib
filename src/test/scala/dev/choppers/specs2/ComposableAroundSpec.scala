package dev.choppers.specs2

import org.specs2.mutable.Specification

class ComposableAroundSpec extends Specification {
  "Composition of an example" should {
    "be allowed" in new ComposableAround {
      ok
    }
  }
}