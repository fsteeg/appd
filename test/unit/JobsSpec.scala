package unit

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class JobsSpec extends Specification {
  "Jobs" should {
    "render the index page for criterion=priorities" in new WithApplication {
      val home = route(FakeRequest(GET, "/jobs?criterion=priorities")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      val content = contentAsString(home)
      content must contain("Jobs")
      content must contain("Score")
      content must contain("hbz")
      content must contain("Priorities")
    }
    "render the index page for criterion=skills" in new WithApplication {
      val home = route(FakeRequest(GET, "/jobs?criterion=skills")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      val content = contentAsString(home)
      content must contain("Jobs")
      content must contain("Score")
      content must contain("hbz")
      content must contain("Skills")
    }
  }
}
