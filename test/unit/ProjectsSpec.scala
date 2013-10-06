package unit

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class ProjectsSpec extends Specification {
  "Projects" should {
    "render the index page for criterion=priorities" in new WithApplication {
      val home = route(FakeRequest(GET, "/projects?criterion=priorities")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      val content = contentAsString(home)
      content must contain("Projects")
      content must contain("Score")
      content must contain("Zest")
      content must contain("Priorities")
    }
    "render the index page for criterion=skills" in new WithApplication {
      val home = route(FakeRequest(GET, "/projects?criterion=skills")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      val content = contentAsString(home)
      content must contain("Projects")
      content must contain("Score")
      content must contain("Zest")
      content must contain("Skills")
    }
  }
}
