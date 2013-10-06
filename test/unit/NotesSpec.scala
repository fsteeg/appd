package unit

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import controllers.Notes
import scala.xml.XML
import play.api.Logger
import controllers.Application

@RunWith(classOf[JUnitRunner])
class NotesSpec extends Specification {

  "Notes" should {
    "return an RSS feed, sorted from latest to oldest" in new WithApplication {
      val home = route(FakeRequest(GET, "/notes")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "application/rss+xml")
      contentAsString(home) must equalTo(Notes.notesAsRss(Application.data("notes/")))
      val pubDates = XML.loadString(contentAsString(home)) \\ "pubDate"
      val firstDate = Notes.rssDateFormat.parse(pubDates.head.text)
      Logger.trace(firstDate.toString())
      val lastDate = Notes.rssDateFormat.parse(pubDates.last.text)
      Logger.trace(lastDate.toString())
      firstDate.after(lastDate) must beTrue
    }
  }
}
