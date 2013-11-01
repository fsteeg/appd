package unit

import org.specs2.mutable.Specification
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import controllers.Application
import play.api.libs.ws.WS
import scala.concurrent.Await

@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {
  "Application" should {
    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/boum")) must beNone
    }
    "render the index page" in {
      running(TestServer(3333)) {
        val r = play.api.test.Helpers.await(WS.url("http://localhost:3333").get)
        r.status must equalTo(OK)
        r.header("Content-Type") must beSome.which(_ contains "text/html")
        r.body must contain("Public")
        r.body must contain("Private")
        r.body must contain("Notes RSS feed")
      }
    }
    "provide access to the Appd data location" in {
      Application.data("") must endWith("data/")
    }
  }
}
