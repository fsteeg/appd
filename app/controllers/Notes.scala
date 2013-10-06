package controllers

import play.api._
import play.api.mvc._
import java.io.File
import scala.io.Source
import org.clapper.markwrap.MarkWrap
import org.clapper.markwrap.MarkupType
import java.text.SimpleDateFormat
import java.text.DateFormat
import java.util.Locale

object Notes extends Controller {

  def index = Action {
    Ok(notesAsRss(Application.data("notes/"))).as("application/rss+xml")
  }

  def notesAsRss(root: String) = {
    val data = (loadAllFiles(root) sortBy (_._1))(Ordering[String].reverse)
    Logger.info("data: " + data.toList)
    rssFor(data).toString
  }

  def loadAllFiles(root: String) = {
    (for (file <- new File(root).listFiles; src = Source.fromFile(file))
      yield (file.getName().split("\\.")(0), parseToHtml(src.mkString)))
  }

  def parseToHtml(source: String) =
    MarkWrap.parserFor(MarkupType.Textile).parseToHTML(source)

  def rssFor(data: Seq[(String, String)]) = {
    val latestRssTime = parseToRssTime(data.head._1)
    <rss version="2.0">
      <channel>
        <title>Notes RSS feed</title>
        <description>Feed description</description>
        <link>http://www.example.com/rss</link>
        <lastBuildDate>{ latestRssTime }</lastBuildDate>
        <pubDate>{ latestRssTime }</pubDate>
        {
          for ((time, text) <- data; rssTime = parseToRssTime(time)) yield {
            <item>
              <title>{ rssTime }</title>
              <description>{ text }</description>
              <link>http://www.example.com/item</link>
              <guid isPermaLink="false">{ rssTime }</guid>
              <pubDate>{ rssTime }</pubDate>
            </item>
          }
        }
      </channel>
    </rss>
  }

  def parseToRssTime(time: String) = {
    val inputFormat = new SimpleDateFormat("yyyy-mm-dd")
    val outputFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z")
    outputFormat.format(inputFormat.parse(time));
  }

}