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

@RunWith(classOf[JUnitRunner])
class PostsSpec extends Specification {
  "Posts" should {
    "be an RSS feed, sorted from latest to oldest" in new WithApplication {
      val home = route(FakeRequest(GET, "/posts")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "application/rss+xml")
      XML.loadString(contentAsString(home)) must equalTo(Notes.notesAsRss(Application.data("posts/")))
      val pubDates = XML.loadString(contentAsString(home)) \\ "pubDate"
      val firstDate = Notes.rssDateFormat.parse(pubDates.head.text)
      val lastDate = Notes.rssDateFormat.parse(pubDates.last.text)
      Logger.trace(s"First date: $firstDate, last date: $lastDate")
      firstDate.after(lastDate) must beTrue
    }
  }
}
