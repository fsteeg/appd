package unit

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import controllers.Application

@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {
  "Application" should {
    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boum")) must beNone
    }
    "render the index page" in new WithApplication{
      val home = route(FakeRequest(GET, "/")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain ("Notes")
      contentAsString(home) must contain ("A sample note")
      contentAsString(home) must contain ("A sample post")
    }
    "provide access to the Appd data location" in  {
      Application.data("") must endWith ("data/")
    }
  }
}
