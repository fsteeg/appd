package unit

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import controllers.Notes
import scala.xml.XML
import controllers.Application
import play.api.Logger
import Helpers._

@RunWith(classOf[JUnitRunner])
class PostsSpec extends Specification {
  "Posts" should {
    "be an RSS feed, sorted from latest to oldest" in new WithApplication {
      val home = route(FakeRequest(GET, "/posts").withHeaders(("Accept", "application/rss+xml"))).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "application/rss+xml")
      XML.loadString(contentAsString(home)) must equalTo(Notes.rss(Application.data("posts/")))
      val pubDates = XML.loadString(contentAsString(home)) \\ "pubDate"
      val firstDate = Notes.rssDateFormat.parse(pubDates.head.text)
      val lastDate = Notes.rssDateFormat.parse(pubDates.last.text)
      Logger.trace(s"First date: $firstDate, last date: $lastDate")
      firstDate.after(lastDate) must beTrue
    }
    "support text/html content" in {
      "text/html" must beAcceptedBy(controllers.Posts.index())
    }
    "return HTML that's valid XML" in {
      XML.loadString(contentAsString(controllers.Posts.index().apply(FakeRequest().
        withHeaders(("Accept", "text/html"))))) must not beNull
    }
    "support application/rss+xml content" in {
      "application/rss+xml" must beAcceptedBy(controllers.Posts.index())
    }
    "return RSS that's valid XML" in {
      XML.loadString(contentAsString(controllers.Posts.index().apply(FakeRequest().
        withHeaders(("Accept", "application/rss+xml"))))) must not beNull
    }
  }
}
