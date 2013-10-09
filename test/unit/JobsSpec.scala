package unit

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class JobsSpec extends Specification {
  "Jobs" should {
    "appear on the index page for criterion=priorities" in new WithApplication {
      val home = route(FakeRequest(GET, "/jobs?criterion=priorities")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      val content = contentAsString(home)
      content must contain("Jobs")
      content must contain("Location")
      content must contain("Score")
      content must contain("9998")
      content must contain("9980")
      content must contain("Priorities")
      content must contain("Attributes")
    }
    "appear on the index page for criterion=skills" in new WithApplication {
      val home = route(FakeRequest(GET, "/jobs?criterion=skills")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      val content = contentAsString(home)
      content must contain("Jobs")
      content must contain("Location")
      content must contain("Score")
      content must contain("96")
      content must contain("91")
      content must contain("Skills")
      content must contain("Attributes")
    }
  }
}
