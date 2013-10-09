package unit

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class ProjectsSpec extends Specification {
  "Projects" should {
    "appear on the index page for criterion=priorities" in new WithApplication {
      val home = route(FakeRequest(GET, "/projects?criterion=priorities")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      val content = contentAsString(home)
      content must contain("Projects")
      content must contain("Location")
      content must contain("Score")
      content must contain("9959")
      content must contain("9889")
      content must contain("Priorities")
      content must contain("Attributes")
    }
    "appear on the index page for criterion=skills" in new WithApplication {
      val home = route(FakeRequest(GET, "/projects?criterion=skills")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      val content = contentAsString(home)
      content must contain("Projects")
      content must contain("Location")
      content must contain("Score")
      content must contain("91")
      content must contain("59")
      content must contain("Skills")
      content must contain("Attributes")
    }
  }
}
