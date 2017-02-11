import org.scalatest.{FlatSpec, Matchers}

class PassingTests extends FlatSpec with Matchers {

  "Equality" should "always hold" in {
    1 shouldBe 1
  }

}
