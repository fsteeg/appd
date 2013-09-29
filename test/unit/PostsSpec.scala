package unit

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class PostsSpec extends Specification {

  "Posts" should {

    "render the index page" in new WithApplication {
      val home = route(FakeRequest(GET, "/posts")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "application/rss+xml")
      contentAsString(home) must contain("Posts RSS")
    }
  }
}
