package unit

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json
import play.api.libs.json.Json._
import play.api.libs.json.JsValue
import models.Job
import models.User
import Helpers._

@RunWith(classOf[JUnitRunner])
class ProfileSpec extends Specification {
  "Profile" should {
    "appear on the index page" in new WithApplication {
      val home = route(FakeRequest(GET, "/profile")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      val content = contentAsString(home)
      content must contain("Profile")
      content must contain("Priorities")
      content must contain("Skills")
      content must contain("Attributes")
    }
    "support text/html content" in {
      "text/html" must beAcceptedBy(controllers.Profile.index())
    }
    "support application/json content" in {
      "application/json" must beAcceptedBy(controllers.Profile.index())
    }
    "return valid JSON" in {
      controllers.Profile.index() must returnValidJson
    }
  }
}
